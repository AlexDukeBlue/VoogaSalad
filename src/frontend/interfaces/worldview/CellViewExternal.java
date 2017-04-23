package frontend.interfaces.worldview;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
import backend.util.HasLocation;
import frontend.util.GameBoardObjectView;

public interface CellViewExternal extends GameBoardObjectView {

	CoordinateTuple getLocation();

	Cell getCell();

	default HasLocation getEntity() {
		return getCell();
	}
}
