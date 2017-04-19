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
		addVertex(-size / 2, -size / 2);
		addVertex(size / 2, -size / 2);
		addVertex(size / 2, size / 2);
		addVertex(-size / 2, size / 2);
		cellIn.setX(xOffset);
		cellIn.setY(yOffset);
	}

	private void convertToRect(double size) {
		xOffset = (cell.getCoordinateTuple().get(0) + .5) * size;
		yOffset = (cell.getCoordinateTuple().get(1) + .5) * size;
	}

	private void addVertex(double xV, double yV) {
		cell.getPolygon().getPoints().add(xV);
		cell.getPolygon().getPoints().add(yV);
	}
}