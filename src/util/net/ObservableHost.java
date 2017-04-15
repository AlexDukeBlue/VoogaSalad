package util.net;

import util.io.Serializer;
import util.io.Unserializer;
import util.net.requests.ErrorRequest;
import util.net.requests.HeartbeatRequest;
import util.net.requests.ModifierRequest;
import util.net.requests.SerializableObjectRequest;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * This class provides a basic implementation of a host process that connects to and communicates with a remote host.
 * <p>
 * It modifies a state of type T, and notifies listeners of changes to the state.
 * <p>
 * The shared network state should model the distributed networked state of your program, and contain information that needs to be replicated on each of the clients.
 *
 * @param <T> The type of variable used to represent network shared state.
 * @author Created by th174 on 4/1/2017.
 * @see Request,Modifier,ObservableServer,ObservableServer.ServerDelegate,ObservableClient,ObservableHost
 */
public abstract class ObservableHost<T> implements Runnable {
	public static final Duration NEVER_TIMEOUT = Duration.ZERO;
	public static final String LOCALHOST = "127.0.0.1";
	private final Serializer<? super T> serializer;
	private final Unserializer<? extends T> unserializer;
	private final Collection<Consumer<? super T>> stateUpdateListeners;
	private final Duration timeout;
	private final Map<Class<? extends Request>, Consumer<? super Request>> requestHandlers;
	private final Map<Class<? extends Request>, Predicate<? super Request>> requestValidators;
	private volatile int commitIndex;
	private volatile T state;

	/**
	 * @param serializer   Converts the state to a Serializable form, so that it can be sent to the client
	 * @param unserializer Converts the Serializable form of the state back into its original form of type T
	 * @param timeout      Socket timeout duration
	 */
	protected ObservableHost(Serializer<? super T> serializer, Unserializer<? extends T> unserializer, Duration timeout) {
		this.serializer = serializer;
		this.unserializer = unserializer;
		this.stateUpdateListeners = new CopyOnWriteArrayList<>();
		this.commitIndex = 0;
		this.timeout = timeout;
		this.requestHandlers = new HashMap<>();
		this.requestValidators = new HashMap<>();
		setRequestHandler(ModifierRequest.class, request -> handle(((ModifierRequest<T>) request).get()));
		setRequestHandler(SerializableObjectRequest.class, request -> handle(getUnserializer().unserialize(((SerializableObjectRequest) request).get())));
		setRequestValidator(SerializableObjectRequest.class, request -> request.getCommitIndex() >= this.getCommitIndex());
	}

	@Override
	public abstract void run();

	/**
	 * @return Returns the state on the local machine, which should match the network's shared state
	 */
	protected final T getState() {
		return state;
	}

	/**
	 * Sets the state on the local host.
	 *
	 * @param state State on local host, should match networked state
	 */
	protected void setState(T state) {
		this.state = state;
	}

	/**
	 * @return Returns current local commit index
	 */
	protected final int getCommitIndex() {
		return commitIndex;
	}

	protected final void setCommitIndex(int value) {
		commitIndex = value;
	}

	protected final void incrementCommitIndex() {
		commitIndex++;
	}

	/**
	 * This method is invoked on all requests received from the remote host
	 * <p>
	 * If a request validator exists for the type of incoming request, then the request is checked for errors.
	 * <p>
	 * If a request handler exists for the type of incoming request, then the request by the request handler
	 * <p>
	 * Else, the request is handled as an error.
	 *
	 * @param request Incoming request received from remote host
	 * @return Returns true if the incoming request was valid
	 */
	protected boolean handleRequest(Request request) {
		if (requestValidators.getOrDefault(request.getClass(), r -> true).test(request)) {
			Consumer<? super Request> handler = requestHandlers.get(request.getClass());
			if (Objects.nonNull(handler)) {
				setCommitIndex(request.getCommitIndex());
				handler.accept(request);
				return true;
			}
		}
		handleError(request);
		return false;
	}

	/**
	 * Invoked when the remote host sends a request indicating that it is in an error state
	 *
	 * @param request The invalid request to be handled
	 */
	protected abstract void handleError(Request request);

	/**
	 * Adds a requestValidator for a type of incoming request.
	 * <p>
	 * If the requestValidator returns true, then the request handler is triggered.
	 * <p>
	 * If no requestValidator has been specified, then it will default to true, and the request handler will be triggered.
	 *
	 * @param requestType      New type of request
	 * @param requestValidator Predicate that returns true if the give request is valid
	 */
	public final void setRequestValidator(Class<? extends Request> requestType, Predicate<? super Request> requestValidator) {
		requestValidators.put(requestType, requestValidator);
	}

	/**
	 * Adds a request handler for a type of incoming request
	 *
	 * @param requestType    New type of request
	 * @param requestHandler Consumer that handles the new type of request
	 */
	public final void setRequestHandler(Class<? extends Request> requestType, Consumer<? super Request> requestHandler) {
		requestHandlers.put(requestType, requestHandler);
	}

	/**
	 * Invoked when the local host receives a modifier sent from the remote host
	 *
	 * @param modifier modifier receieved from remote host
	 */
	protected void handle(Modifier<T> modifier) {
		this.state = modifier.modify(state);
		fireStateUpdatedEvent();
	}

	/**
	 * Invoked when the local host receives a new state from the remote host
	 *
	 * @param state state received from remote host
	 */
	protected void handle(T state) {
		this.state = state;
		fireStateUpdatedEvent();
	}

	/**
	 * Sends a request to the remote host
	 *
	 * @param request Request to be sent to the remote host
	 * @return Returns true if request was sent successfully
	 */
	protected abstract boolean send(Request request);

	/**
	 * @param state New state object to be serialized and sent to the remote host
	 * @return Returns a request that contains a serialized version of the state.
	 */
	protected final Request getRequest(T state) {
		return new SerializableObjectRequest<>(getSerializer().serialize(state));
	}

	/**
	 * Sends a serialized new state object to the remote host
	 *
	 * @param state New state object to be serialized and sent to the remote host
	 * @return Returns true if the request was sent successfully
	 */
	protected boolean send(T state) {
		return send(getRequest(state));
	}

	/**
	 * @param modifier Modifier and sent to the remote host
	 * @return Returns a request that contains a modification of the state
	 */
	public final Request getRequest(Modifier<? super T> modifier) {
		return new ModifierRequest<>(modifier);
	}

	/**
	 * Sends a modifier on the state to the remote host
	 *
	 * @param modifier Modifier to be sent and applied to the state
	 * @return Returns true if the request was sent successfully
	 */
	protected boolean send(Modifier<T> modifier) {
		return send(getRequest(modifier));
	}

	/**
	 * @return Returns a heartbeat request notifying the remote host that the local host is in a normal operation state.
	 */
	protected final Request getHeartBeatRequest() {
		return new HeartbeatRequest();
	}

	/**
	 * Sends a heartbeat request notifying the remote host that the local host is in a normal operation state.
	 *
	 * @return Returns true if the request was sent successfully
	 */
	protected final boolean sendHeartBeatRequest() {
		return send(getHeartBeatRequest());
	}

	/**
	 * @return Returns an error request notifying the remote host that the local host is currently in an error state.
	 */
	protected final Request getErrorRequest() {
		return new ErrorRequest(-1);
	}

	/**
	 * Adds a listener which will be notified whenever the local state is updated.
	 *
	 * @param stateUpdateListener The listener to register
	 */
	public final void addListener(Consumer<? super T> stateUpdateListener) {
		stateUpdateListeners.add(stateUpdateListener);
	}

	/**
	 * Removes the given listener from the list of listeners, that are notified whenever the local state is updated.
	 *
	 * @param stateUpdateListener The listener to remove
	 */
	public final void removeListener(Consumer<? super T> stateUpdateListener) {
		stateUpdateListeners.remove(stateUpdateListener);
	}

	private void fireStateUpdatedEvent() {
		if (Objects.nonNull(state)) {
			stateUpdateListeners.forEach(e -> e.accept(getState()));
		}
	}

	/**
	 * @return Returns true while the connection associated with this object is active
	 */
	public abstract boolean isActive();

	/**
	 * @return Returns the connection timeout duration for this host
	 */
	protected Duration getTimeout() {
		return timeout;
	}

	/**
	 * @return Returns the serializer currently used by this host
	 */
	protected final Serializer<? super T> getSerializer() {
		return serializer;
	}

	/**
	 * @return Returns the serializer currently usd by this host
	 */
	protected final Unserializer<? extends T> getUnserializer() {
		return unserializer;
	}

	/**
	 * Wraps exceptions thrown from in socket I/O
	 */
	public static class RemoteConnectionException extends RuntimeException {
		protected RemoteConnectionException(Exception e) {
			super("Error connecting to remote host: " + e.getMessage(), e);
		}
	}

	/**
	 * Thrown when an invalid request is received by this host
	 */
	public static class InvalidRequestException extends RuntimeException {
		protected InvalidRequestException(String s) {
			super("Invalid request received: " + s);
		}
	}
}
