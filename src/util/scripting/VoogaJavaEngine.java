package util.scripting;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Created by th174 on 4/6/2017.
 */
public class VoogaJavaEngine implements VoogaScriptEngine {
	private static int fileCount = 0;
	private boolean hasCompiled = false;
	private String className;
	private String methodName;
	private String srcPath;
	private String fullSource;
	private Class<?>[] paramTypes;

	private void compile(Map<String, Object> bindings) throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		String[] paramNames = bindings.keySet().toArray(new String[bindings.size()]);
		Class<?>[] temp = bindings.values().stream().map(Object::getClass).collect(Collectors.toList()).toArray(new Class[bindings.size()]);
		String methodArgs = IntStream.range(0, temp.length)
				.mapToObj(i -> temp[i].getCanonicalName() + " " + paramNames[i])
				.collect(Collectors.joining(", "));
		String output = String.format(fullSource, methodArgs);
		Files.write(Paths.get(srcPath), output.getBytes());
		compiler.run(null, null, null,
				srcPath,
				"-d", getClass().getResource("..").getPath());
		paramTypes = temp;
		hasCompiled = true;
	}

	@Override
	public VoogaScriptEngine setScript(String script) throws VoogaScriptException {
		className = String.format(RESOURCES.getString("JavaUserScriptName"), fileCount);
		srcPath = String.format(RESOURCES.getString("JavaUserScriptPath"), className);
		methodName = RESOURCES.getString("FunctionName");
		this.fullSource = String.format(RESOURCES.getString("JavaClassTemplate"), className, methodName, "%s", script.replaceAll("(?m)^", "\t\t"));
		fileCount++;
		return this;
	}

	@Override
	public Object eval(Map<String, Object> bindings) throws VoogaScriptException {
		try {
			if (!hasCompiled) {
				compile(bindings);
			}
			return Class.forName("scripting." + className).getMethod(methodName, paramTypes).invoke(null, bindings.values().toArray());
		} catch (Exception e) {
			throw new Error(e);
		}
	}
}
