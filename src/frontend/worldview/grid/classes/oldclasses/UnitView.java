package frontend.worldview.grid.classes.oldclasses;

import backend.grid.CoordinateTuple;
import frontend.View;
import frontend.util.BaseUIManager;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

/**
 * A Unit object is an movable or immovable character/object that can be placed
 * on top of and move across Terrains.
 * <p>
 * The UnitView is the UI representation of the backend Unit class. It allows
 * the user to interact with the unit by dragging and dropping, selecting
 * abilities to use, and moving from cell to cell.
 * <p>
 * The UnitView extends Sprite, which extends BaseUIManager.
 *
 * @author dylanpeters
 */
public class UnitView extends BaseUIManager<Node> {
	private final ImageView unitView;
	private final String unitName;
	private final CoordinateTuple unitLocation;

	/**
	 * Creates a new UnitView. Sets all values to default.
	 */
	public UnitView(String unitName, CoordinateTuple unitLocation, String unitImgPath) {
		this.unitName = unitName;
		this.unitLocation = unitLocation;
		unitView = new ImageView(View.getImg(unitImgPath));
	}

	public String getUnitName() {
		return unitName;
	}

	public CoordinateTuple getUnitLocation() {
		return unitLocation;
	}

	public void setX(double x) {
		unitView.setX(x);
	}

	public double getX() {
		return unitView.getX();
	}

	public DoubleProperty xProperty() {
		return unitView.xProperty();
	}

	public void setY(double y) {
		unitView.setY(y);
	}

	public double getY() {
		return unitView.getY();
	}

	public DoubleProperty yProperty() {
		return unitView.yProperty();
	}

	public void setFitWidth(double fitWidth) {
		unitView.setFitWidth(fitWidth);
	}

	public double getFitWidth() {
		return unitView.getFitWidth();
	}

	public DoubleProperty fitWidthProperty() {
		return unitView.fitWidthProperty();
	}

	public void setFitHeight(double fitHeight) {
		unitView.setFitHeight(fitHeight);
	}

	public double getFitHeight() {
		return unitView.getFitHeight();
	}

	public DoubleProperty fitHeightProperty() {
		return unitView.fitHeightProperty();
	}

	/**
	 * Returns an object that allows the user to see the Unit object on a grid
	 *
	 * @return Region object representing the visualization of the Unit
	 */
	@Override
	public Node getObject() {
		return unitView;
	}

}
