package frontend.interfaces.worldview;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
import frontend.util.GameBoardObjectView;

public interface CellViewExternal extends GameBoardObjectView {

	CoordinateTuple getLocation();

	@Override
	Cell getEntity();

	void darken();

	void unDarken();
}
