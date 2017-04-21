package frontend.factory.worldview.layout;

public class GridLayoutDelegateFactory implements GridLayoutDelegate{
	
	GridLayoutDelegate layoutManager;

	@Override
	public void layoutCell(CellViewLayoutInterface cell, double scaleFactor, double min, double max) {
		if (cell.getCoordinateTuple().dimension() == 2){
			layoutManager = new SquareLayoutDelegate();
		} else {
			layoutManager = new HexagonalGridLayoutDelegate();
		}
		layoutManager.layoutCell(cell, scaleFactor, min, max);
	}
	

}
