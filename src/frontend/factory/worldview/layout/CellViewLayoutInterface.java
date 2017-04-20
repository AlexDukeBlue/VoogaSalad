package frontend.factory.worldview.layout;

import backend.grid.CoordinateTuple;
import javafx.scene.shape.Polygon;

public interface CellViewLayoutInterface {

	public double getX();

	public void setX(double x);

	public double getY();

	public void setY(double y);

	/**
	 * returns the polygon that serves as the shape of the cell
	 *
	 * @return polygon
	 */
	public Polygon getPolygon();

	/**
	 * sets the group to contain a different polygon
	 *
	 * @param polygon
	 *            Shape of cellview an instance of a cell
	 */
	public void setPolygon(Polygon polygon);

	public CoordinateTuple getCoordinateTuple();

}
