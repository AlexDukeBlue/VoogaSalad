package frontend.worldview.grid;

import backend.unit.Unit;
import frontend.View;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * A Unit object is an movable or immovable character/object that can be placed
 * on top of and move across Terrains.
 * 
 * The UnitView is the UI representation of the backend Unit class. It allows
 * the user to interact with the unit by dragging and dropping, selecting
 * abilities to use, and moving from cell to cell.
 * 
 * The UnitView extends Sprite, which extends BaseUIManager.
 * 
 * @author dylanpeters
 *
 */
public class UnitView extends Sprite {

	private ImageView unitView;
	
	/**
	 * Creates a new UnitView. Sets all values to default.
	 * 
	 * @param unitModel
	 *            the Unit object that this UnitView will be visualizing
	 */
	public UnitView(Unit unitModel) {
		unitView = new ImageView(View.getImg(unitModel.getImgPath()));
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
