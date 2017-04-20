package frontend.worldview.grid.layout_delegate.actual_classes;

import frontend.worldview.grid.actual_classes.oldclasses.CellView;
import frontend.worldview.grid.layout_delegate.interfaces.GridLayoutDelegate;

public class GridLayoutDelegateFactory implements GridLayoutDelegate{
	
	GridLayoutDelegate layoutManager;

	@Override
	public void layoutCell(CellView cell, double scaleFactor, double min, double max) {
		if (cell.getCoordinateTuple().dimension() == 2){
			layoutManager = new SquareLayoutDelegate();
		} else {
			layoutManager = new HexagonalGridLayoutDelegate();
		}
		layoutManager.layoutCell(cell, scaleFactor, min, max);
	}
	

}
