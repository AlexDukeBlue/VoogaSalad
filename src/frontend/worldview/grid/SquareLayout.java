package frontend.worldview.grid;

public class SquareLayout implements LayoutManager {

	private double xOffset;
	private double yOffset;
	private CellView cell;
	
	@Override
	public void layoutCell(CellView cellIn, double scaleFactor, double min, double max) {
		cell = cellIn;
		double size = (max - min) * scaleFactor + min;
		convertToRect(size);
		addVertex(xOffset, yOffset);
		addVertex(xOffset + size, yOffset);
		addVertex(xOffset + size, yOffset + size);
		addVertex(xOffset, yOffset + size);
	}
	
	private void convertToRect(double size) {
		xOffset = cell.getCoordinateTuple().get(0) * size;
		yOffset = cell.getCoordinateTuple().get(1) * size;
	}
	
	private void addVertex(double xV, double yV) {
		cell.getPolygon().getPoints().add(xV);
		cell.getPolygon().getPoints().add(yV);
	}
}