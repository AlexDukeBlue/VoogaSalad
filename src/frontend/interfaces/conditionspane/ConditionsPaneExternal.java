/**
 * 
 */
package frontend.interfaces.conditionspane;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Region;
import util.polyglot.Polyglot;

/**
 * The externally-facing interface for the ConditionsPane class. This allows the
 * ConditionsPane to be accessed from external classes using only the specified
 * methods, in order to prevent access to methods that client code should not
 * use.
 * 
 * @author Stone Mathers
 *
 */
public interface ConditionsPaneExternal {

	/**
	 * Returns the Node of the object that is being managed by this ConditionsPane.
	 * This is the node that should be displayed to the user.
	 * 
	 * @return the Node of the object that is being managed by this ConditionsPane.
	 */
	Region getNode();

	/**
	 * Returns the polyglot object that is used by this ConditionsPane to translate
	 * its text.
	 * 
	 * @return the polyglot object that is used by this ConditionsPane to translate
	 *         its text.
	 */
	Polyglot getPolyglot();

	/**
	 * Returns an ObjectProperty that contains the stylesheet that is used to
	 * style this ConditionsPane. This allows other classes to change, bind or listen
	 * to changes in the StyleSheet.
	 * 
	 * @return an ObjectProperty that contains the stylesheet that is used to
	 *         style this ConditionsPane
	 */
	ObjectProperty<String> getStyleSheet();

}
