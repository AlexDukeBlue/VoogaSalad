package frontend.interfaces.worldview;

import frontend.ClickHandler;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Region;
import util.polyglot.Polyglot;

/**
 * The externally-facing interface for the WorldView class. This allows the
 * WorldClass to be accessed from external classes using only the specified
 * methods, in order to prevent access to methods that client code should not
 * use.
 * 
 * @author Dylan Peters
 *
 */
public interface WorldViewExternal {

	/**
	 * Returns a GridViewExternal object that is being manipulated by this
	 * WorldView.
	 * 
	 * @return A GridViewExternal object that is being manipulated by this
	 *         WorldView.
	 */
	GridViewExternal getGridView();

	/**
	 * Returns the Node of the object that is being managed by this WorldView.
	 * This is the node that should be displayed to the user.
	 * 
	 * @return the Node of the object that is being managed by this WorldView.
	 */
	Region getNode();

	/**
	 * Returns the polyglot object that is used by this WorldView to translate
	 * its text.
	 * 
	 * @return the polyglot object that is used by this WorldView to translate
	 *         its text.
	 */
	Polyglot getPolyglot();

	/**
	 * Sets the clickHandler that is used by this WorldView to respond to
	 * clicks.
	 * 
	 * @param clickHandler
	 *            the clickHandler that is used by this WorldView to respond to
	 *            clicks.
	 */
	void setClickHandler(ClickHandler clickHandler);

	/**
	 * Returns an ObjectProperty that contains the stylesheet that is used to
	 * style this WorldView. This allows other classes to change, bind or listen
	 * to changes in the StyleSheet.
	 * 
	 * @return an ObjectProperty that contains the stylesheet that is used to
	 *         style this WorldView
	 */
	ObjectProperty<String> getStyleSheet();
}
