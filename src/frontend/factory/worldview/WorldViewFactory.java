package frontend.factory.worldview;

import controller.Controller;
import frontend.ClickHandler;
import frontend.interfaces.worldview.WorldViewExternal;

/**
 * The WorldViewFactory specifies methods for creating WorldViewExternal objects
 * in order to hide the WorldView classes.
 * 
 * @author Dylan Peters
 *
 */
public class WorldViewFactory {

	/**
	 * Creates and returns a new WorldView object, given the parameters passed
	 * in.
	 * 
	 * @param controller
	 *            the Controller that the WorldView will interact with.
	 * @param clickHandler
	 *            the ClickHandler to pass to the WorldView to allow it to
	 *            determine behavior on clicks
	 * @return the WorldView object.
	 */
	public static WorldViewExternal newWorldView(Controller controller, ClickHandler clickHandler) {
		return new WorldView(controller, clickHandler);
	}

}
