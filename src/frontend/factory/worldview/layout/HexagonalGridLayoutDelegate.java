/**
 * Implement the functional interface GridLayoutDelegate. Manages the placement of a Hexagonal CellView into a grid.
 * Does so by translating the relative coordinates in the CellView's CoordinateTuple to absolute coordinate.
 * Absolute coordinates are calculated using the given scaling factor, minimum width, and maximum width.
 * These absolute coordinates are used to set the CellView's X and Y coordinates.
 *
 * @author Stone Mathers
 * Created 4/11/2017
 */
package frontend.factory.worldview.layout;

import backend.grid.CoordinateTuple;
import controller.Controller;
import javafx.scene.shape.Polygon;

class HexagonalGridLayoutDelegate implements GridLayoutDelegate {

	public static final int X_INDEX = 0;
	public static final int Y_INDEX = 1;
	public static final double FULL_CIRCLE = Math.PI * 2;

	@Override
	public Polygon layoutCell(double scaleFactor, double minWidth, double maxWidth, CoordinateTuple location, Controller controller) {
		if (scaleFactor <= 0 || scaleFactor > 1 || minWidth <= 0) {
			throw new RuntimeException();
		}
		Hexagon hexagon = new Hexagon(0, 0, 0);
		hexagon.setPoints(scaleFactor, minWidth, maxWidth);
		double width = minWidth + ((maxWidth - minWidth) * scaleFactor);
		double radius = width / (Math.cos(FULL_CIRCLE / 12) - Math.cos((FULL_CIRCLE / 12) * 5));
		double xOffset = (controller.getGrid().getRows() - 1) / 2;
		double yOffset = (controller.getGrid().getColumns() - 1) / 2;
		CoordinateTuple rectCoord = location.convertToRectangular();
		if ((rectCoord.get(Y_INDEX) % 2) == 0) {
			hexagon.setLayoutX((xOffset + rectCoord.get(X_INDEX) + .5) * width);
		} else {
			hexagon.setLayoutX((xOffset + rectCoord.get(X_INDEX) + 1) * width);
		}
		hexagon.setLayoutY(yOffset + rectCoord.get(Y_INDEX) * (1.5 * radius) + radius);
		return hexagon;
	}
}
