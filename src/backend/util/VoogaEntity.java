package backend.util;

import java.io.Serializable;

/**
 * @author Created by th174 on 3/31/2017.
 */
public interface VoogaEntity extends Serializable {
	<U extends VoogaEntity> U copy();

	String getName();

	default String getFormattedName() {
		return getName();
	}

	String getDescription();

	String getImgPath();
}
