package util.io;

import java.io.Serializable;

/**
 * This functional interface creates a serializable object from an unserializable one.
 *
 * @author Created by th174 on 4/1/2017.
 */
@FunctionalInterface
public interface Serializer<T> {
	Serializer<Object> NONE = Serializable.class::cast;

	/**
	 * @param obj Object to be converted to Serializable form
	 * @return Serializable form of obj
	 * @throws SerializationException Thrown if implementation throws exception
	 */
	default Serializable serialize(T obj) throws SerializationException {
		try {
			return doSerialize(obj);
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	/**
	 * @param obj Object to be converted to Serializable form
	 * @return Serializable form of obj
	 * @throws Exception Thrown if implementation throws exception
	 */
	Serializable doSerialize(T obj) throws Exception;

	/**
	 * Wraps exceptions thrown in doSerialize
	 */
	class SerializationException extends RuntimeException {
		private SerializationException(Exception e) {
			super("Error occurred in serialization: " + e.getMessage(), e);
		}
	}
}
