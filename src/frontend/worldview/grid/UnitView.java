package frontend.worldview.grid;

import backend.grid.CoordinateTuple;
import frontend.View;
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
public class UnitView extends Sprite {
	private final ImageView unitView;
	private final String unitName;
	private final CoordinateTuple unitLocation;

	/**
	 * Creates a new UnitView. Sets all values to default.
	 */
	public UnitView(String unitName, CoordinateTuple unitLocation, String unitImgPath, UnitViewDelegate delegate) {
		this.unitName = unitName;
		this.unitLocation = unitLocation;
		unitView = new ImageView(View.getImg(unitImgPath));
		unitView.setOnMouseClicked(event -> delegate.unitClicked(this));
	}

	public String getUnitName() {
		return unitName;
	}

	public CoordinateTuple getUnitLocation() {
		return unitLocation;
	}

	/**
	 * Returns an object that allows the user to see the Unit object on a grid
	 *
	 * @return Region object representing the visualization of the Unit
	 */
	@Override
	public ImageView getObject() {
		return unitView;
	}

	/**
	 * @return String holding the type of list the Sprite belongs to.
	 */
	@Override
	public String getListType() {
		return null;
	}

}
