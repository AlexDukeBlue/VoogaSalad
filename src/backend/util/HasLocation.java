package backend.util;

import backend.grid.CoordinateTuple;

/**
 * @author Created by th174 on 4/22/17.
 */
public interface HasLocation extends VoogaEntity{
	CoordinateTuple getLocation();
}
