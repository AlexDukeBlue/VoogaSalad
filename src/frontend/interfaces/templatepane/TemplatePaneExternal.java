package frontend.interfaces.templatepane;

import frontend.ClickHandler;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Region;
import util.polyglot.Polyglot;

/**
 * The externally-facing interface for the TemplatePane class. This allows the
 * TemplatePane to be accessed from external classes using only the specified
 * methods, in order to prevent access to methods that client code should not
 * use.
 * 
 * @author Dylan Peters
 *
 */
public interface TemplatePaneExternal {

	/**
	 * Returns the Node of the object that is being managed by this
	 * TemplatePane. This is the node that should be displayed to the user.
	 * 
	 * @return the Node of the object that is being managed by this
	 *         TemplatePane.
	 */
	Region getNode();

	/**
	 * Returns the polyglot object that is used by this TemplatePane to translate
	 * its text.
	 * 
	 * @return the polyglot object that is used by this TemplatePane to translate
	 *         its text.
	 */
	Polyglot getPolyglot();

	/**
	 * Sets the clickHandler that is used by this TemplatePane to respond to
	 * clicks.
	 * 
	 * @param clickHandler
	 *            the clickHandler that is used by this TemplatePane to respond to
	 *            clicks.
	 */
	void setClickHandler(ClickHandler clickHandler);

	/**
	 * Returns an ObjectProperty that contains the stylesheet that is used to
	 * style this TemplatePane. This allows other classes to change, bind or listen
	 * to changes in the StyleSheet.
	 * 
	 * @return an ObjectProperty that contains the stylesheet that is used to
	 *         style this TemplatePane
	 */
	ObjectProperty<String> getStyleSheet();
}
