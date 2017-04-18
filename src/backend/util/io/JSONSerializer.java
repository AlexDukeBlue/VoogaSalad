package backend.util.io;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.io.Serializer;
import util.io.Unserializer;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * @author Created by th174 on 4/9/2017.
 */
public class JSONSerializer<T> implements Serializer<T>, Unserializer<T> {
	private static Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.excludeFieldsWithModifiers(Modifier.TRANSIENT)
			.create();
	private final Type objType;

	public JSONSerializer(Type type) {
		this.objType = type;
	}

	@Override
	public String doSerialize(T obj) throws Exception {
		return gson.toJson(obj, objType);
	}

	@Override
	public T doUnserialize(Serializable obj) throws Exception {
		return gson.fromJson((String) obj, objType);
	}
}
