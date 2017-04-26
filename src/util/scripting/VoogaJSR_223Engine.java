package util.scripting;

import javax.script.*;
import java.util.Map;

/**
 * @author Created by th174 on 4/7/2017.
 */
public abstract class VoogaJSR_223Engine implements VoogaScriptEngine {
	private transient static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
	private transient final ScriptEngine engine;
	private CompiledScript compiledScript;

	protected VoogaJSR_223Engine() {
		engine = SCRIPT_ENGINE_MANAGER.getEngineByName(getEngineName());
	}

	protected abstract String getEngineName();

	@Override
	public VoogaJSR_223Engine setScript(String script) throws VoogaScriptException {
		try {
			compiledScript = ((Compilable) engine).compile(script);
		} catch (ScriptException e) {
			throw new VoogaScriptException(e);
		}
		return this;
	}

	@Override
	public Object eval(Map<String, Object> bindings) throws VoogaScriptException {
		try {
			return compiledScript.eval(new SimpleBindings(bindings));
		} catch (ScriptException e) {
			throw new VoogaScriptException(e);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ", powered by JSR-223 " + getEngineName() + " script engine.\n";
	}
}

