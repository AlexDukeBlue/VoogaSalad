package frontend.interfaces.worldview;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import frontend.util.GameBoardObjectView;
import javafx.scene.layout.Pane;

/**
 * The externally-facing interface for the UnitView class. This allows the
 * UnitView class to be accessed from external classes using only the specified
 * methods, in order to prevent access to methods that client code should not
 * use.
 * 
 * @author Dylan Peters
 *
 */
public interface UnitViewExternal extends GameBoardObjectView {

	/**
	 * Returns the Node of the object that is being managed by this UnitView.
	 * This is the node that should be displayed to the user.
	 * 
	 * @return the Node of the object that is being managed by this UnitView.
	 */
	Pane getNode();

	/**
	 * Returns the name of the unit being displayed by this UnitView.
	 * 
	 * @return the name of the unit being displayed by this UnitView.
	 */
	String getUnitName();

	/**
	 * Returns the Unit class that has the data representation of this UnitView.
	 * 
	 * @return the Unit class that has the data representation of this UnitView.
	 */
	@Override
	Unit getEntity();

	/**
	 * Returns the CoordinateTuple that describes the location of this UnitView.
	 * 
	 * @return the CoordinateTuple that describes the location of this UnitView.
	 */
	CoordinateTuple getUnitLocation();
}
