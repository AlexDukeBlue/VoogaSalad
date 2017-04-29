package frontend.interfaces.worldview;

import java.util.Collection;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import frontend.ClickHandler;
import frontend.util.GameBoardObjectView;
import javafx.scene.control.ScrollPane;
import util.polyglot.Polyglot;

/**
 * The externally-facing interface for the GridView class. This allows the
 * GridView class to be accessed from external classes using only the specified
 * methods, in order to prevent access to methods that client code should not
 * use.
 * 
 * @author Dylan Peters
 *
 */
public interface GridViewExternal extends GameBoardObjectView {

	/**
	 * Returns the Node of the object that is being managed by this GridView.
	 * This is the node that should be displayed to the user.
	 * 
	 * @return the Node of the object that is being managed by this GridView.
	 */
	ScrollPane getNode();

	/**
	 * Sets the clickHandler that is used by this GridView to respond to clicks.
	 * 
	 * @param clickHandler
	 *            the clickHandler that is used by this GridView to respond to
	 *            clicks.
	 */
	void setClickHandler(ClickHandler clickHandler);

	/**
	 * Returns the polyglot object that is used by this GridView to translate
	 * its text.
	 * 
	 * @return the polyglot object that is used by this GridView to translate
	 *         its text.
	 */
	Polyglot getPolyglot();

	/**
	 * Highlights the cells at the specified locations.
	 * 
	 * @param highlightedCells
	 *            the cells to highlight.
	 */
	void highlightRange(Collection<CoordinateTuple> highlightedCells);

	/**
	 * Resets highlighting so that no cells are darkened or highlighted.
	 */
	void resetHighlighting();

	/**
	 * Gets the GameBoard object that has the data representing this GridView
	 * 
	 * @return the GameBoard object that has the data representing this GridView
	 */
	@Override
	GameBoard getEntity();

}
