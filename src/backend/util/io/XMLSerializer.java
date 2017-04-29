package backend.util.io;

import com.thoughtworks.xstream.XStream;
import util.io.Serializer;
import util.io.Unserializer;

import java.io.Serializable;

/**
 * @author Created by th174 on 4/9/2017.
 */
public class XMLSerializer<T,U> implements Serializer<T>, Unserializer<U> {
	private static final XStream X_STREAM = new XStream();

	@Override
	public String doSerialize(T obj) throws Exception {
		return X_STREAM.toXML(obj);
	}

	@Override
	public U doUnserialize(Serializable obj) throws Exception {
		return (U) X_STREAM.fromXML((String) obj);
	}
}
