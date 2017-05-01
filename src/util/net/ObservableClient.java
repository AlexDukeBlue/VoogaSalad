package util.net;

import util.io.Serializer;
import util.io.Unserializer;
import util.net.requests.HeartbeatRequest;
import util.net.requests.ModifierRequest;

import java.io.IOException;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class provides a simple implementation of a client that connects to a server with a given server name and port.
 * <p>
 * The client can request changes to the state, but cannot modify the state directly unless instructed to do so by the server.
 * <p>
 *
 * @param <T> The type of variable used to represent network shared state.
 * @author Created by th174 on 4/1/2017.
 * @see Request,Modifier,ObservableServer,ObservableServer.ServerDelegate,ObservableClient,ObservableHost
 */
public class ObservableClient<T> extends ObservableHost<T> {
	private final SocketConnection connection;
	private final BlockingQueue<Request> outbox;

	/**
	 * Creates a client connected to a server located at host:port, and starts listening for requests sent from the server
	 *
	 * @param port {@inheritDoc}
	 * @throws IOException {@inheritDoc}
	 */
	public ObservableClient(int port) throws IOException {
		this(LOCALHOST, port, Serializer.NONE, (Unserializer<? extends T>) Unserializer.NONE);
	}

	/**
	 * Creates a client connected to a server located at host:port, and starts listening for requests sent from the server
	 *
	 * @param host {@inheritDoc}
	 * @param port {@inheritDoc}
	 * @throws IOException {@inheritDoc}
	 */
	public ObservableClient(String host, int port) throws IOException {
		this(host, port, Serializer.NONE, (Unserializer<? extends T>) Unserializer.NONE);
	}

	/**
	 * Creates a client connected to a server located at host:port, and starts listening for requests sent from the server
	 *
	 * @param host         {@inheritDoc}
	 * @param port         {@inheritDoc}
	 * @param serializer   {@inheritDoc}
	 * @param unserializer {@inheritDoc}
	 * @throws IOException {@inheritDoc}
	 */
	public ObservableClient(String host, int port, Serializer<? super T> serializer, Unserializer<? extends T> unserializer) throws IOException {
		this(host, port, serializer, unserializer, NEVER_TIMEOUT);
	}

	/**
	 * Creates a client connected to a server located at host:port, and starts listening for requests sent from the server
	 *
	 * @param host         {@inheritDoc}
	 * @param port         {@inheritDoc}
	 * @param serializer   {@inheritDoc}
	 * @param unserializer {@inheritDoc}
	 * @throws IOException {@inheritDoc}
	 */
	public ObservableClient(String host, int port, Serializer<? super T> serializer, Unserializer<? extends T> unserializer, Duration timeout) throws IOException {
		super(serializer, unserializer, timeout);
		setCommitIndex(Integer.MIN_VALUE);
		this.connection = new SocketConnection(new Socket(host, port), getTimeout());
		outbox = new LinkedBlockingQueue<>();
		setRequestValidator(ModifierRequest.class, request -> request.getCommitIndex() == this.getCommitIndex() + 1);
		setRequestHandler(HeartbeatRequest.class, request -> handleHeartBeat());
		setRequestValidator(HeartbeatRequest.class, request -> request.getCommitIndex() == this.getCommitIndex());
	}

	@Override
	public void run() {
		connection.listen(this::handleRequest);
	}

	@Override
	protected boolean handleRequest(Request request) {
		if (super.handleRequest(request)) {
			try {
				send(outbox.take().setCommitIndex(getCommitIndex()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	protected void handle(Modifier<T> modifier) {
		super.handle(modifier);
	}

	@Override
	protected void handle(T state) {
		super.handle(state);
	}

	/**
	 * Responds the heartbeat requests sent by the server by sending back another heartbeat request
	 */
	private void handleHeartBeat() {
		send(getHeartBeatRequest());
	}

	/**
	 * Notifies the server that the client is in an error state
	 */
	protected void handleError(Request request) {
		send(getErrorRequest());
	}

	@Override
	protected boolean send(Request request) {
		return connection.send(request.setCommitIndex(getCommitIndex()));
	}

	@Override
	public boolean isActive() {
		return connection.isActive();
	}

	@Override
	protected boolean send(Modifier<T> modifier) {
		return super.send(modifier);
	}

	/**
	 * Queues up a request containing a modifier to be sent to the server
	 *
	 * @param modifier Modifier to be sent to the server
	 */
	public void addToOutbox(Modifier<T> modifier) {
		outbox.add(getRequest(modifier));
	}

	/**
	 * Queues up a request containing a serialized state to be sent to the server
	 *
	 * @param state Modifier to be sent to the server
	 */
	public void addToOutbox(T state) {
		outbox.add(getRequest(state));
	}
}

