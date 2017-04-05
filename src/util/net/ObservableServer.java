package util.net;

import util.io.Serializer;
import util.io.Unserializer;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;

/**
 * This class provides a general server that allows multiple simultaneous client connections.
 * <p>
 * The server creates a child thread listening to each client.
 *
 * @param <T> The type of variable used to represent networked shared state.
 * @author Created by th174 on 4/1/2017.
 * @see Request,Modifier,ObservableServer, ServerConnection ,ObservableClient,ObservableHost,AbstractObservableHost,RemoteListener
 */
public class ObservableServer<T> extends AbstractObservableHost<T> {
    private final Collection<ClientConnection> connections;
    private final ServerSocket serverSocket;
    private volatile Instant mostRecentTimeStamp;
    private volatile T state;
    private volatile int commitIndex;

    /**
     * Constructs an instance of VoogaServer without serialization
     *
     * @param initialState The initial networked shared state.
     * @param port         Port to listen on for new client connections
     * @throws Exception Thrown if ServerSocket could not be created, or if exception is thrown in serialization
     */
    public ObservableServer(T initialState, int port) throws Exception {
        this(initialState, port, Serializer.NONE, Unserializer.NONE);
    }

    /**
     * Constructs an instance of VoogaServer
     *
     * @param initialState The initial networked shared state.
     * @param port         Port to listen on for new client connections
     * @param serializer   Converts the state to a Serializable form, so that it can be sent to the client
     * @param unserializer Converts the Serializable form of the state back into its original form of type T
     * @throws Exception Thrown if ServerSocket could not be created, or if exception is thrown in serialization
     */
    public ObservableServer(T initialState, int port, Serializer<T> serializer, Unserializer<T> unserializer) throws Exception {
        this(initialState, port, serializer, unserializer, NEVER_TIMEOUT);
    }

    /**
     * Constructs an instance of VoogaServer
     *
     * @param initialState The initial networked shared state.
     * @param port         Port to listen on for new client connections
     * @param serializer   Converts the state to a Serializable form, so that it can be sent to the client
     * @param unserializer Converts the Serializable form of the state back into its original form of type T
     * @param timeout      Timeout duration for all connections to the client
     * @throws Exception Thrown if ServerSocket could not be created, or if exception is thrown in serialization
     */
    public ObservableServer(T initialState, int port, Serializer<T> serializer, Unserializer<T> unserializer, Duration timeout) throws Exception {
        super(serializer, unserializer, timeout);
        this.state = initialState;
        this.mostRecentTimeStamp = Instant.now(Clock.systemUTC());
        this.connections = new HashSet<>();
        this.serverSocket = new ServerSocket(port);
    }

    /**
     * Listens for connections from clients.
     * <p>
     * For each client, this method creates a child thread that listens to the client in the background, and the child thread is added to a child thread pool.
     */
    @Override
    public void run() {
        commitIndex = 0;
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                ClientConnection child = new ClientConnection(serverSocket.accept());
                submitTask(child);
                connections.add(child);
            }
        } catch (Exception e) {
            throw new RemoteConnectionException(e);
        } finally {
            shutDown();
        }
    }

    @Override
    public T getState() {
        return state;
    }

    /**
     * Sends a serialized version of the state to all clients.
     *
     * @param state New state object to be serialized and propagated across all servers and clients
     * @return Returns true if state is sent successfully
     */
    @Override
    protected boolean send(T state) {
        for (ClientConnection e : connections) {
            e.send(state);
        }
        return isActive();
    }

    /**
     * Sets the server's local state, then sends serialized version of the state to all clients.
     *
     * @param state New state object to be serialized and propagated across all servers and clients
     * @return Returns true if state is sent successfully
     */
    public synchronized final boolean sendAndApply(T state) {
        this.state = state;
        incrementCommitIndex();
        return send(state);
    }

    /**
     * Sends a modifier to all clients to be applied.
     *
     * @param modifier Request to be applied to the networked state on all clients.
     * @return Returns true if the requests were sent successfully
     */
    @Override
    protected boolean send(Modifier<T> modifier) {
        connections.removeIf(e -> !e.send(modifier));
        return isActive();
    }

    /**
     * Applies a modifier to the server's local state, then sends the modifier to all clients to be applied.
     *
     * @param modifier Request to be applied to the networked state on all clients.
     * @return Returns true if the requests were sent successfully
     */
    public final synchronized boolean sendAndApply(Modifier<T> modifier) {
        this.state = modifier.modify(state);
        incrementCommitIndex();
        return send(modifier);
    }

    private void incrementCommitIndex() {
        commitIndex++;
    }

    @Override
    public synchronized void handle(T newState, Instant timeStamp) {
        sendAndApply(newState);
    }

    @Override
    public synchronized void handle(Modifier<T> stateModifier, Instant timeStamp) {
        sendAndApply(stateModifier);
    }

    @Override
    public boolean isActive() {
        return serverSocket.isBound() && !serverSocket.isClosed() && !connections.isEmpty();
    }

    @Override
    protected int getCommitIndex() {
        return commitIndex;
    }

    @Override
    protected boolean validateRequest(Request<?> request) {
        if (request.getTimeStamp().isAfter(mostRecentTimeStamp) && request.getCommitIndex() == this.commitIndex) {
            mostRecentTimeStamp = request.getTimeStamp();
            return true;
        }
        return false;
    }

    /**
     * This class listens to a client on a single socket and relays information between the main server and the client.
     *
     * @author Created by th174 on 4/1/2017.
     * @see Request,Modifier, ObservableServer , util.net.ServerThread ,Client,AbstractHost,Host,Listener
     */
    protected class ClientConnection extends ObservableHost<T> {
        /**
         * @param socket Socket to listen on for client requests.
         * @throws Exception Thrown if socket is not open for reading and writing, or if an exception is thrown in serialization
         */
        private ClientConnection(Socket socket) throws Exception {
            super(socket, ObservableServer.this.getSerializer(), ObservableServer.this.getUnserializer(), ObservableServer.this.getTimeout());
        }

        @Override
        public void run() {
            send(getState());
            submitRepeatedTask(this::sendHeartBeat, getTimeout().equals(NEVER_TIMEOUT) ? Duration.ofSeconds(30) : Duration.ofMillis(getTimeout().toMillis() / 2));
            super.run();
            System.out.println("Client Connected @ " + getSocket());
        }

        @Override
        public T getState() {
            return ObservableServer.this.getState();
        }

        @Override
        public void handle(Modifier<T> modifier, Instant timeStamp) {
            ObservableServer.this.handleAndNotify(modifier, timeStamp);
        }

        @Override
        protected void handleHeartBeat() {
        }

        @Override
        protected int getCommitIndex() {
            return ObservableServer.this.getCommitIndex();
        }

        @Override
        public void handle(T state, Instant timeStamp) {
            ObservableServer.this.handleAndNotify(state, timeStamp);
        }
    }
}
