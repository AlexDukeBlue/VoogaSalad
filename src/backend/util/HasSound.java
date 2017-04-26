package backend.util;

/**
 * @author Created by th174 on 4/22/17.
 */
public interface HasSound {
	String getSoundPath();

	<T extends HasSound> T setSoundPath(String path);
}
