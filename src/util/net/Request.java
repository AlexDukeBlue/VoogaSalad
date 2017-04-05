package util.net;

import java.io.Serializable;

/**
 * This class provides contains all communications between the client and the server.
 * Each request contains a content as well as a timestamp of when the request was created.
 * The server uses the timestamp to determine whether or not to approve the request.
 *
 * @param <T> The type of the content contained in the request.
 * @author Created by th174 on 4/2/2017.
 * @see Request,Modifier,ObservableServer, ObservableServer.ServerDelegate ,ObservableClient,ObservableHost,AbstractObservableHost,RemoteListener
 */
public final class Request<T extends Serializable> implements Serializable {
    public static final Serializable ERROR = "ERROR";
    private static final Serializable HEARTBEAT = "HEARTBEAT";
    private final T content;
    private final int commitIndex;

    /**
     * Creates a new request with content and a timestamp of the creation time.
     *
     * @param content Content of request
     */
    public Request(T content) {
        this(content, 0);
    }

    /**
     * Creates a new request with content and a timestamp of the creation time.
     *
     * @param content     Content of request
     * @param commitIndex commitIndex of the sender of this request
     */
    public Request(T content, int commitIndex) {
        this.content = content;
        this.commitIndex = commitIndex;
    }

    public static Request heartbeatRequest(int commitIndex) {
        return new Request<>(HEARTBEAT, commitIndex);
    }

    public static boolean isHeartbeat(Request request) {
        return request.get().equals(HEARTBEAT);
    }

    public static boolean isError(Request request) {
        return request.get().equals("ERROR");
    }

    /**
     * @return Returns the content contained inside with this Request
     */
    public T get() {
        return content;
    }

    /**
     * @return Returns the type of content contained inside this Request
     */
    public Class<?> getContentType() {
        return content.getClass();
    }

    /**
     * @return Returns the commitIndex that this Request was sent with
     */
    public int getCommitIndex() {
        return commitIndex;
    }

    @Override
    public String toString() {
        return String.format("Request:\n\tContent:\t%s\n\tCommit:\t%d", content, commitIndex);
    }
}
