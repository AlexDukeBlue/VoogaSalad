package scripting;

/**
 * @author Created by th174 on 4/7/2017.
 */
public class VoogaScriptException extends RuntimeException {
	VoogaScriptException(Exception e) {
		super(e);
	}

	VoogaScriptException(String s) {
		super(s);
	}
}
