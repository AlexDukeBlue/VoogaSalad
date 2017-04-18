/**
 * Implement the functional interface LayoutManager. Manages the placement of a Hexagonal CellView into a grid.
 * Does so by translating the relative coordinates in the CellView's CoordinateTuple to absolute coordinate.
 * Absolute coordinates are calculated using the given scaling factor, minimum width, and maximum width.
 * These absolute coordinates are used to set the CellView's X and Y coordinates.
 *
 * @author Stone Mathers
 * Created 4/11/2017
 */
package frontend.worldview.grid;

import backend.grid.CoordinateTuple;

public class HexagonalManager implements LayoutManager {

	public static final int X_INDEX = 0;
	public static final int Y_INDEX = 1;
	public static final double FULL_CIRCLE = Math.PI * 2;

	@Override
	public void layoutCell(CellView cell, double scaleFactor, double minWidth, double maxWidth) {
		if (scaleFactor <= 0 || scaleFactor > 1 || minWidth <= 0) {
			throw new RuntimeException();
		}
		cell.setPolygon(new Hexagon(0, 0, 0));
		resizeHexagon((Hexagon) cell.getPolygon(), scaleFactor, minWidth, maxWidth);

		double width = minWidth + ((maxWidth - minWidth) * scaleFactor);
		double radius = width / (Math.cos(FULL_CIRCLE / 12) - Math.cos((FULL_CIRCLE / 12) * 5));

		CoordinateTuple rectCoord = cell.getCoordinateTuple().convertToRectangular();
		if ((rectCoord.get(Y_INDEX) % 2) == 0) {
			for (int i = 0; i < cell.getPolygon().getPoints().size(); i += 2) {
				cell.getPolygon().getPoints().set(i, cell.getPolygon().getPoints().get(i) + (rectCoord.get(X_INDEX) * width) + width / 2);
			}
//			cell.setX(rectCoord.get(X_INDEX) * width);
		} else {
			for (int i = 0; i < cell.getPolygon().getPoints().size(); i += 2) {
				cell.getPolygon().getPoints().set(i, cell.getPolygon().getPoints().get(i) + ((rectCoord.get(X_INDEX) * width) + width));
			}
//			cell.setX((rectCoord.get(X_INDEX) * width) + (width / 2));
		}
		for (int i = 1; i < cell.getPolygon().getPoints().size(); i += 2) {
			cell.getPolygon().getPoints().set(i, cell.getPolygon().getPoints().get(i) + (rectCoord.get(Y_INDEX) * (1.5 * radius)) + radius);
		}
//		cell.setY(rectCoord.get(Y_INDEX) * (1.5 * radius));
	}

	private void resizeHexagon(Hexagon hexagon, double scale, double min, double max) {
		hexagon.setPoints(scale, min, max);
	}
}
