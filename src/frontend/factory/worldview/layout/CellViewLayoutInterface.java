package frontend.factory.worldview.layout;

import backend.grid.CoordinateTuple;
import javafx.scene.shape.Polygon;

public interface CellViewLayoutInterface {

	double getX();

	void setX(double x);

	double getY();

	void setY(double y);

	/**
	 * returns the polygon that serves as the shape of the cell
	 *
	 * @return polygon
	 */
	Polygon getPolygon();

	/**
	 * sets the group to contain a different polygon
	 *
	 * @param polygon
	 *            Shape of cellview an instance of a cell
	 */
	void setPolygon(Polygon polygon);

	CoordinateTuple getLocation();

}
