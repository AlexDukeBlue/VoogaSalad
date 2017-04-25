package backend.util;

import backend.grid.CoordinateTuple;
import backend.grid.Shape;

/**
 * @author Created by th174 on 4/22/17.
 */
public interface HasLocation extends VoogaEntity, HasShape {
	CoordinateTuple getLocation();

	@Override
	default Shape getShape() {
		return CoordinateTuple.DIMENSION_SHAPE_MAP.get(getLocation().dimension());
	}
}
