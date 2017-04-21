import backend.grid.GridPattern;
import util.scripting.VoogaScriptEngine;
import util.scripting.VoogaScriptEngineManager;

/**
 * @author Created by th174 on 4/9/2017.
 */
public class Test {
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		GridPattern pattern = GridPattern.HEXAGONAL_ADJACENT;
		String script = "return 5 + 5";
		long start = System.currentTimeMillis();
		VoogaScriptEngine engine = VoogaScriptEngineManager.read("lua", script);
		System.out.println("Compile time: " + (System.currentTimeMillis() - start) / 1000.0);
		for (int i = 0; i < 100; i++) {
			System.out.println(engine.modify(10, null, null, null));
		}
		System.out.println("Run time: " + (System.currentTimeMillis() - start) / 1000.0);
	}
}
