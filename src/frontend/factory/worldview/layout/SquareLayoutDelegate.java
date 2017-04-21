package frontend.factory.worldview.layout;

class SquareLayoutDelegate implements GridLayoutDelegate {
	@Override
	public void layoutCell(CellViewLayoutInterface cellIn, double scaleFactor, double min, double max) {
		double size = (max - min) * scaleFactor + min;
		addVertex(cellIn, -size / 2, -size / 2);
		addVertex(cellIn, size / 2, -size / 2);
		addVertex(cellIn, size / 2, size / 2);
		addVertex(cellIn, -size / 2, size / 2);
		cellIn.setX((cellIn.getLocation().get(0) + .5) * size);
		cellIn.setY((cellIn.getLocation().get(1) + .5) * size);
	}

	private void addVertex(CellViewLayoutInterface cell, double xV, double yV) {
		cell.getPolygon().getPoints().add(xV);
		cell.getPolygon().getPoints().add(yV);
	}
}