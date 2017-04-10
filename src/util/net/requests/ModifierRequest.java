package util.net.requests;


import util.net.*;

/**
 * This class provides contains communications between the client and the server.
 * Each request has a commit message, which the host uses to determine whether or not the request is valid.
 * <p>
 * This request contains a Modifier, which is a unary operation on some object T
 *
 * @param <T> The type of the object that the modifier modifies.
 * @author Created by th174 on 4/2/2017.
 * @see Request,Modifier,ObservableServer,ObservableServer.ServerDelegate,ObservableClient,ObservableHost
 */
public class ModifierRequest<T> extends SerializableObjectRequest<Modifier<T>> {
	/**
	 * Creates a new request that contains a modifier and a commit index
	 *
	 * @param serializedObject
	 */
	public ModifierRequest(Modifier<T> serializedObject) {
		super(serializedObject);
	}

	/**
	 * Creates a new request that contains a modifier and a commit index
	 *
	 * @param commitIndex commitIndex of the sender of this request
	 */
	public ModifierRequest(Modifier<T> modifier, int commitIndex) {
		super(modifier);
	}
}
