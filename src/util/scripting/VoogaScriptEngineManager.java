package util.scripting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Created by th174 on 4/6/2017.
 */
public final class VoogaScriptEngineManager {
	private static final HashMap<String, Class<? extends VoogaScriptEngine>> SCRIPT_ENGINE_IDENTIFIERS = new HashMap<>();

	private VoogaScriptEngineManager() {
		throw new Error("GTFO this is a singleton class");
	}

	public static Collection<String> getAllSupportedScriptingLanguages() {
		initScriptEngineIdentifiers();
		return SCRIPT_ENGINE_IDENTIFIERS.values().stream().map(Class::getSimpleName).map(e -> e.replaceAll("Vooga|Engine", "")).collect(Collectors.toSet());
	}

	public static VoogaScriptEngine read(String language, Path scriptFilePath) throws IOException {
		return read(language, new String(Files.readAllBytes(scriptFilePath)));
	}

	public static VoogaScriptEngine read(String langauge, String script) throws VoogaScriptException {
		initScriptEngineIdentifiers();
		try {
			return SCRIPT_ENGINE_IDENTIFIERS.get(langauge.replace(" ", "").toLowerCase()).newInstance().setScript(script);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new VoogaScriptException("Language not supported");
		}
	}

	private static void initScriptEngineIdentifiers() {
		if (SCRIPT_ENGINE_IDENTIFIERS.isEmpty()) {
			SCRIPT_ENGINE_IDENTIFIERS.putAll(Stream.of("javascript", "js", "nashorn", "ecmascript").collect(Collectors.toMap(e -> e, e -> VoogaJavaScriptEngine.class)));
			SCRIPT_ENGINE_IDENTIFIERS.putAll(Stream.of("groovy", "groovyshell", "apachegroovy").collect(Collectors.toMap(e -> e, e -> VoogaGroovyEngine.class)));
			SCRIPT_ENGINE_IDENTIFIERS.putAll(Stream.of("java").collect(Collectors.toMap(e -> e, e -> VoogaJavaEngine.class)));
			SCRIPT_ENGINE_IDENTIFIERS.putAll(Stream.of("lua", "luaj").collect(Collectors.toMap(e -> e, e -> VoogaLuaEngine.class)));
			SCRIPT_ENGINE_IDENTIFIERS.putAll(Stream.of("jruby", "ruby").collect(Collectors.toMap(e -> e, e -> VoogaRubyEngine.class)));
		}
	}
}