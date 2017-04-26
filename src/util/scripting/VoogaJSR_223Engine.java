package util.scripting;

import javax.script.*;
import java.util.Map;
import java.util.Objects;

/**
 * @author Created by th174 on 4/7/2017.
 */
public abstract class VoogaJSR_223Engine implements VoogaScriptEngine {
	private transient static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
	private transient ScriptEngine engine;
	private transient CompiledScript compiledScript;
	private String script;

	protected VoogaJSR_223Engine() {
		engine = SCRIPT_ENGINE_MANAGER.getEngineByName(getEngineName());
	}

	protected abstract String getEngineName();

	@Override
	public VoogaJSR_223Engine setScript(String script) throws VoogaScriptException {
		try {
			this.script = script;
			compiledScript = ((Compilable) engine).compile(script);
		} catch (ScriptException e) {
			throw new VoogaScriptException(e);
		}
		return this;
	}

	protected final String getScript(){
		return script;
	}

	@Override
	public Object eval(Map<String, Object> bindings) throws VoogaScriptException {
		try {
			if (Objects.isNull(engine)) {
				engine = SCRIPT_ENGINE_MANAGER.getEngineByName(getEngineName());
				compiledScript = ((Compilable) engine).compile(getScript());
			}
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

