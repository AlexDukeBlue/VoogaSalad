package util.scripting;

import javax.script.*;
import java.util.Map;

/**
 * @author Created by th174 on 4/28/2017.
 */
public class VoogaPythonEngine extends VoogaJSR_223Engine {
	@Override
	protected String getEngineName() {
		return "jython";
	}

	@Override
	public Object eval(Map<String, Object> bindings) throws VoogaScriptException {
		super.eval(bindings);
		return getEngine().getBindings(ScriptContext.ENGINE_SCOPE).get("ret");
	}
}
