package util.scripting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Created by th174 on 4/6/2017.
 */
public final class VoogaScriptEngineManager {
	private static final HashMap<String, Class<? extends VoogaScriptEngine>> SCRIPT_ENGINE_IDENTIFIERS = new HashMap<>();
	private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("resources/Scripting", Locale.US);

	private VoogaScriptEngineManager() {
		throw new AssertionError("GTFO you can't instantiate this. No instances for you!");
	}

	public static Collection<String> getAllSupportedScriptingLanguages() {
		return SCRIPT_ENGINE_IDENTIFIERS.values().stream().map(Class::getSimpleName).map(e -> e.replaceAll("Vooga|Engine", "")).collect(Collectors.toSet());
	}

	public static VoogaScriptEngine read(String language, Path scriptFilePath) throws IOException {
		return read(language, new String(Files.readAllBytes(scriptFilePath)));
	}

	public static VoogaScriptEngine read(String langauge, String script) throws VoogaScriptException {
		try {
			return SCRIPT_ENGINE_IDENTIFIERS.get(langauge.replace(" ", "").toLowerCase()).newInstance().setScript(script);
		} catch (InstantiationException | IllegalAccessException e) {
			try {
				return ((VoogaScriptEngine) Class.forName("util.scripting.Vooga" + langauge + "Engine").newInstance()).setScript(script);
			} catch (IllegalAccessException | InstantiationException | ClassNotFoundException e1) {
				throw new VoogaScriptException("Language not supported");
			}
		}
	}

	public static String getDefaultText(String language) {
		return RESOURCES.getString(language + "DefaultText");
	}

	static {
		SCRIPT_ENGINE_IDENTIFIERS.putAll(Stream.of("javascript", "js", "nashorn", "ecmascript").collect(Collectors.toMap(e -> e, e -> VoogaJavaScriptEngine.class)));
		SCRIPT_ENGINE_IDENTIFIERS.putAll(Stream.of("groovy", "groovyshell", "apachegroovy").collect(Collectors.toMap(e -> e, e -> VoogaGroovyEngine.class)));
		SCRIPT_ENGINE_IDENTIFIERS.putAll(Stream.of("java").collect(Collectors.toMap(e -> e, e -> VoogaJavaEngine.class)));
		SCRIPT_ENGINE_IDENTIFIERS.putAll(Stream.of("lua", "luaj").collect(Collectors.toMap(e -> e, e -> VoogaLuaEngine.class)));
		SCRIPT_ENGINE_IDENTIFIERS.putAll(Stream.of("jruby", "ruby").collect(Collectors.toMap(e -> e, e -> VoogaRubyEngine.class)));
		SCRIPT_ENGINE_IDENTIFIERS.putAll(Stream.of("jython", "python").collect(Collectors.toMap(e -> e, e -> VoogaPythonEngine.class)));
	}
}