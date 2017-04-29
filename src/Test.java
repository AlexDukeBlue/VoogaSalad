import util.scripting.VoogaPythonEngine;

import java.io.IOException;

/**
 * @author Created by th174 on 4/9/2017.
 */
public class Test {
	public static void main(String[] args) throws IOException {
		String script = "" +
				"print \"hello world\"\n" +
				"ret = \"test\"";
		VoogaPythonEngine py = new VoogaPythonEngine();
		py.setScript(script);
		System.out.println(py.modify(10, null, null, null));
	}
}
