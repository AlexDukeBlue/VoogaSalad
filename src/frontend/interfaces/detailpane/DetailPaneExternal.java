package frontend.interfaces.detailpane;

import backend.util.VoogaEntity;
import frontend.ClickHandler;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Region;
import util.polyglot.Polyglot;

/**
 * The externally-facing interface for the DetailPane class. This allows the
 * DetailPane class to be accessed from external classes using only the
 * specified methods, in order to prevent access to methods that client code
 * should not use.
 * 
 * @author Dylan Peters
 *
 */
public interface DetailPaneExternal {

	/**
	 * Returns the Node of the object that is being managed by this WorldView.
	 * This is the node that should be displayed to the user.
	 * 
	 * @return the Node of the object that is being managed by this WorldView.
	 */
	Region getNode();

	/**
	 * Sets the detailPane to display the details of the specified VoogaEntity
	 * 
	 * @param entity
	 *            the VoogaEntity which the DetailPane will display the details
	 *            of.
	 */
	void setContent(VoogaEntity entity);

	/**
	 * Returns the polyglot object that is used by this Detailpane to translate
	 * its text.
	 * 
	 * @return the polyglot object that is used by this DetailPane to translate
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
	 * Sets the DetailPane to author mode, which allows the user to edit some
	 * VoogaEntities.
	 */
	void setAuthorMode();

	/**
	 * Sets the DetailPane to play mode, which prevents the user form editing
	 * VoogaEntities.
	 */
	void setPlayMode();

	/**
	 * Returns an ObjectProperty that contains the stylesheet that is used to
	 * style this DetailPane. This allows other classes to change, bind or
	 * listen to changes in the StyleSheet.
	 * 
	 * @return an ObjectProperty that contains the stylesheet that is used to
	 *         style this DetailPane
	 */
	ObjectProperty<String> getStyleSheet();

}
