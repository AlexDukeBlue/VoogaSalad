package util.io;

import java.io.Serializable;

/**
 * This functional interface creates an unserializable object from a serializable one
 *
 * @author Created by th174 on 4/1/2017.
 */
@FunctionalInterface
public interface Unserializer<T> {
    Unserializer<? extends Serializable> NONE = obj -> obj;

    /**
     * @param obj Object to be converted from serializable to unserializable form
     * @return Unserializable form of object
     * @throws UnserializationException Thrown if implementation throws exception
     */
    default T unserialize(Serializable obj) throws UnserializationException {
        try {
            return doUnserialize(obj);
        } catch (Exception e) {
            throw new UnserializationException(e);
        }
    }

    T doUnserialize(Serializable obj) throws Exception;

    class UnserializationException extends RuntimeException {
        private UnserializationException(Exception e) {
            super("Error occurred in unserialization: " + e.getMessage(), e);
        }
    }
}
