package frontend.worldview.grid;

public class LayoutManagerFactory implements LayoutManager{
	
	LayoutManager layoutManager;

	@Override
	public void layoutCell(CellView cell, double scaleFactor, double min, double max) {
		if (cell.getCoordinateTuple().dimension() == 2){
			layoutManager = new SquareLayout();
		} else {
			layoutManager = new HexagonalManager();
		}
		layoutManager.layoutCell(cell, scaleFactor, min, max);
	}
	

}
