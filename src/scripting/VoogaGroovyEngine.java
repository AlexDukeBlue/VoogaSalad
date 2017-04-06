package scripting;

import java.util.Map;

/**
 * @author Created by th174 on 4/6/2017.
 */
class VoogaGroovyEngine extends VoogaScriptEngine {

    static {
        VoogaScriptEngine.addEngine(new VoogaGroovyEngine(), "groovy", "groovyshell");
    }

    private VoogaGroovyEngine() {
        //TODO
    }

    @Override
    protected Object eval(Map<String, Object> bindings) {
        //TODO
        return null;
    }
}
