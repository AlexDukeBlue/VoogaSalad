package util.net;

import util.io.Serializer;
import util.io.Unserializer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.time.Instant;

/**
 * This class provides a basic implementation for a host client or server communicating over TCP/IP.
 * <p>
 * It can send and receive requests from a remote host and modifies a network shared state.
 *
 * @param <T> The type of variable used to represent network shared state.
 * @author Created by th174 on 4/2/2017.
 * @see Request,Modifier,Server, Server.ServerThread ,Client, Host , AbstractHost ,Listener
 */
public abstract class Host<T> extends AbstractHost<T> {
    private final Socket socket;
    private final ObjectOutputStream outputStream;

    /**
     * Creates a new VoogaRemote server or client that listens on a socket for requests.
     *
     * @param socket Socket to listen on for client requests.
     * @throws IOException Thrown if socket is not open for reading and writing.
     */
    public Host(Socket socket) throws Exception {
        this(socket, NO_SERIALIZER, NO_UNSERIALIZER);
    }

    /**
     * Creates a new VoogaRemote server or client that listens on a socket for requests.
     *
     * @param socket       Socket to listen on for client requests.
     * @param serializer   Converts a state of type T into a Serializable form to be sent over the network.
     * @param unserializer Converts a Serializable form of a state into type T.
     * @throws IOException Thrown if socket is not open for reading and writing.
     */
    public Host(Socket socket, Serializer<T> serializer, Unserializer<T> unserializer) throws IOException {
        super(serializer, unserializer);
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.outputStream.flush();
        getSocket().setSoTimeout(DEFAULT_CONNECTION_TIMEOUT);
    }

    /**
     * Creates a separate thread to being listening to input sent over the network. When a request is found, handle is called on the content of the request.
     *
     * @throws IOException Thrown in socket is not open for listening
     */
    public void start() throws IOException {
        new Listener(socket, this::handleRequest).start();
    }

    /**
     * This method is called when a new request is received from the remote host.
     * <p>
     * It delegates handling of the new request to other handle methods.
     *
     * @param request Request received from remote host.
     * @throws RuntimeException Thrown when an error occurs while unserializing the request, or when an error occurs in processing the request.
     */
    protected synchronized void handleRequest(Request<? extends Serializable> request) {
        try {
            if (Modifier.class.isAssignableFrom(request.getContentType())) {
                handle((Modifier<T>) request.get(), request.getTimeStamp());
            } else {
                handle(getUnserializer().unserialize(request.get()), request.getTimeStamp());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isActive() {
        return getSocket().isConnected() && !getSocket().isClosed() && getSocket().isBound();
    }

    /**
     * @return Returns the socket this VoogaRemote is operating on
     */
    protected Socket getSocket() {
        return socket;
    }

    private boolean sendRequest(Request<? extends Serializable> request) {
        if (!isActive()) {
            return false;
        }
        try {
            outputStream.writeObject(request);
            return true;
        } catch (IOException e) {
            try {
                getSocket().close();
                System.out.println("Connection Closed:\t" + getSocket());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public abstract void handle(Modifier<T> modifier, Instant timeStamp) throws Exception;

    @Override
    public abstract void handle(T state, Instant timeStamp) throws Exception;

    @Override
    public boolean send(Modifier<T> modifier) throws Exception {
        return sendRequest(new Request<>(modifier));
    }

    @Override
    public boolean send(T state) throws Exception {
        return sendRequest(new Request<>(getSerializer().serialize(state)));
    }
}
