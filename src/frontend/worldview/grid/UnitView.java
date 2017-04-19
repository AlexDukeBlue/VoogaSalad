package frontend.worldview.grid;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import controller.Controller;
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
public final class UnitView extends Sprite {

	private final ImageView unitView;
	private final Unit unitModel;
	private final String unitName;
	private final CoordinateTuple location;

	/**
	 * Creates a new UnitView. Sets all values to default.
	 *
	 * @param unitModel the Unit object that this UnitView will be visualizing
	 */

	public UnitView(Unit unitModel, UnitViewDelegate delegate) {
		this.unitModel = unitModel;
		this.location = unitModel.getLocation();
		this.unitName = unitModel.getName();
		unitView = new ImageView(View.getImg(unitModel.getImgPath()));
		unitView.setOnMouseClicked(event -> {
			delegate.unitClicked(this);
		});
	}

	public Unit getUnit(Controller controller) {
		return controller.getGameState()
				.getGrid()
				.get(location)
				.getOccupantByName(unitName);
		//return unitModel;
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
