package frontend.factory.worldview.layout;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import javafx.scene.shape.Polygon;

class SquareLayoutDelegate implements GridLayoutDelegate {
	@Override
	public Polygon layoutCell(double scaleFactor, double min, double max, CoordinateTuple location, GameBoard gameBoard) {
		double size = (max - min) * scaleFactor + min;
		Polygon cell = new Polygon();
		addVertex(cell, -size / 2, -size / 2);
		addVertex(cell, size / 2, -size / 2);
		addVertex(cell, size / 2, size / 2);
		addVertex(cell, -size / 2, size / 2);
		double xOffset = (gameBoard.getRows() - 1) / 2;
		double yOffset = (gameBoard.getColumns() - 1) / 2;
		cell.setLayoutX((xOffset + location.get(0) + .5) * size);
		cell.setLayoutY((yOffset + location.get(1) + .5) * size);
		return cell;
	}

	private void addVertex(Polygon cell, double xV, double yV) {
		cell.getPoints().add(xV);
		cell.getPoints().add(yV);
	}
}