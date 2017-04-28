import backend.grid.GridPattern;
import backend.util.io.XMLSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Created by th174 on 4/9/2017.
 */
public class Test {
	public static void main(String[] args) throws IOException {
		XMLSerializer xml = new XMLSerializer();
		String path = System.getProperty("user.dir") + "/data/core/grid_patterns/";
		Files.createDirectories(Paths.get(path));
		GridPattern.getPredefinedGridPatterns().forEach(obj -> {
			try {
				Files.write(Paths.get(path + obj.getFormattedName()+".xml"), ((String) xml.serialize(obj)).getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
