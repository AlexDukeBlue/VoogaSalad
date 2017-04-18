package util.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * This class provides a interface to for hosts to communicate by sending and receiving requests over a socket.
 * <p>
 * It can listen to a socket's input stream and send requests to a socket's output stream.
 *
 * @author Created by th174 on 4/5/2017.
 * @see Request,Modifier,ObservableServer,ObservableServer.ServerDelegate,ObservableClient,ObservableHost
 */
public class SocketConnection {
	private final Socket socket;
	private final ObjectOutputStream outputStream;
	private final ExecutorService executor;

	/**
	 * Creates a socket connection from a socket
	 *
	 * @param socket  Socket that this connection is attached to
	 * @param timeout Duration to wait for activity on the socket before it times out
	 * @throws ObservableHost.RemoteConnectionException Thrown when an error occurs in opening the socket for listening
	 */
	public SocketConnection(Socket socket, Duration timeout) throws ObservableHost.RemoteConnectionException {
		try {
			this.socket = socket;
			this.socket.setSoTimeout((int) timeout.toMillis());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.outputStream.flush();
			this.executor = Executors.newCachedThreadPool();
		} catch (Exception e) {
			throw new ObservableHost.RemoteConnectionException(e);
		}
	}

	/**
	 * Continuously listens for requests sent over the socket, and handles them with a request handler
	 *
	 * @param requestHandler Consumer that handles requests through the socket
	 * @throws ObservableHost.RemoteConnectionException Thrown when an invalid request from the remote host is received.
	 */
	public void listen(Consumer<Request> requestHandler) throws ObservableHost.RemoteConnectionException {
		try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
			while (isActive()) {
				Request request = (Request) inputStream.readObject();
//				System.out.println("Receive" + request);
				executor.execute(() -> requestHandler.accept(request));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ObservableHost.RemoteConnectionException(e);
		} finally {
			shutDown();
		}
	}

	/**
	 * Sends a request through the socket
	 *
	 * @param request Request to be sent through the socket
	 * @return Returns true if the request was sent successfully
	 */
	public synchronized boolean send(Request request) {
//		System.out.println("Send " + request);
		try {
			outputStream.writeObject(request);
			return isActive();
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Closes the connection
	 */
	private void shutDown() {
		try {
			socket.close();
			executor.shutdown();
			System.out.println("Connection closed: " + socket);
		} catch (IOException e) {
			throw new ObservableHost.RemoteConnectionException(e);
		}
	}

	/**
	 * @return Returns true if this connection is currently active
	 */
	public boolean isActive() {
		return socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown() && socket.isBound();
	}

	@Override
	public String toString() {
		return "Connection on " + socket;
	}
}

