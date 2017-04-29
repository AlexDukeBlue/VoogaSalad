package frontend.interfaces.worldview;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
import frontend.util.GameBoardObjectView;

/**
 * The externally-facing interface for the CellView class. This allows the
 * CellView class to be accessed from external classes using only the specified
 * methods, in order to prevent access to methods that client code should not
 * use.
 * 
 * @author Dylan Peters
 *
 */
public interface CellViewExternal extends GameBoardObjectView {

	/**
	 * Returns the CoordinateTuple representing the location of this CellView.
	 * 
	 * @return the CoordinateTuple representing the location of this CellView.
	 */
	CoordinateTuple getLocation();

	/**
	 * Returns the Cell object that represents the data of this CellView.
	 * 
	 * @return the Cell object that represents the data of this CellView.
	 */
	@Override
	Cell getEntity();

	/**
	 * Darkens the display of the cell. This can be used to highlight other
	 * cells, by virtue of darkening all cells that are not to be highlighted.
	 */
	void darken();

	/**
	 * Undarkens the display of the cell.
	 */
	void unDarken();

	/**
	 * Plays a sound associated with this cell. Can be used when moving units to
	 * cell.
	 */
	void playSound();
}
