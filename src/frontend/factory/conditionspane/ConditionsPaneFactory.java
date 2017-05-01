package frontend.factory.conditionspane;

import controller.Controller;
import frontend.ClickHandler;
import frontend.interfaces.conditionspane.ConditionsPaneExternal;

/**
 * The ConditionsPaneFactory specifies methods for creating
 * ConditionsPaneExternal objects in order to hide the ConditionsPane class.
 * 
 * @author Stone Mathers Created 4/20/2017
 */
public class ConditionsPaneFactory {

	/**
	 * Creates and returns a new ConditionPaneExternal given the passed
	 * parameters.
	 * 
	 * @param controller
	 *            The Controller that the ConditionsPanelExternal will interact
	 *            with.
	 * @param clickHandler
	 *            The ClickHandler to pass to the ConditionsPanelExternal to
	 *            allow it to determine behavior on clicks
	 * @return The ConditionsPaneExternal object.
	 */
	public static ConditionsPaneExternal newConditionsPane(Controller controller, ClickHandler clickHandler) {
		return new ConditionsPane(controller, clickHandler);
	}

}
