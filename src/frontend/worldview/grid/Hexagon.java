/**
 * A Hexagon is a Polygon whose points are, by default, set as a regular Hexagon.
 * 
 * @author Stone Mathers
 * Created 4/11/2017
 */
package frontend.worldview.grid;

import javafx.scene.shape.Polygon;

public class Hexagon extends Polygon {

	public static final double FULL_CIRCLE = Math.PI * 2;

	private double myMinWidth, myMaxWidth;

	/**
	 * @param minWidth
	 *            Minimum width
	 * @param maxWidth
	 *            Maximum width
	 * @param scale
	 *            Scale relative to minWidth and maxWidth. Must be greater than
	 *            0.0 and less than 1.0.
	 */
	public Hexagon(double minWidth, double maxWidth, double scale) {
		setPoints(minWidth, maxWidth, scale);
	}

	/**
	 * Sets the points of the Hexagon with a minimum width, maximum width, and
	 * scale.
	 * 
	 * @param scale
	 *            Scale relative to minWidth and maxWidth. Must be greater than
	 *            0.0 and less than 1.0.
	 * @param minWidth
	 *            Minimum width
	 * @param maxWidth
	 *            Maximum width
	 */
	public void setPoints(double scale, double minWidth, double maxWidth) {
		myMinWidth = minWidth;
		myMaxWidth = maxWidth;
		setPoints(scale);
	}

	/**
	 * Sets the points of the Hexagon with a new minimum width, maximum width,
	 * and scale.
	 * 
	 * @param scale
	 *            Scale relative to minWidth and maxWidth. Must be greater than
	 *            0.0 and less than 1.0.
	 */
	public void setPoints(double scale) {
		getPoints().clear();
		double width = myMinWidth + ((myMaxWidth - myMinWidth) * scale);
		double radius = width / (Math.cos(FULL_CIRCLE / 12) - Math.cos((FULL_CIRCLE / 12) * 5));

		for (int i = 1; i < 12; i += 2) {
			getPoints().add(radius * Math.cos((FULL_CIRCLE / 12) * i));
			getPoints().add(radius * Math.sin((FULL_CIRCLE / 12) * i));
		}
	}

}
