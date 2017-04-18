/**
 * ListItems are visual items that are to be displayed in a list.
 */
package frontend.templatepane;

import frontend.util.ObjectManager;
import javafx.scene.Parent;

/**
 * @author Stone Mathers
 * Created 3/29/2017
 */
public interface ListItem extends ObjectManager<Parent> {

	/**
	 * Sets what is to be done when the ListItem is clicked.
	 */
	void setOnClick();
	
}
