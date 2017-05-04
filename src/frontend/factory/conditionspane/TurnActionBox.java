/**
 * 
 */
package frontend.factory.conditionspane;

import controller.Controller;
import frontend.ClickHandler;

/**
 * A ConditionBox that holds a Turn Action by name. When the checkbox is
 * activated, the Controller is used to activate the Turn Action. When the
 * checkbox is deactivated, the Controller is used to deactivate the Turn
 * Action.
 *
 * @author Stone Mathers Created 4/25/2017
 */
public class TurnActionBox extends ConditionBox {

	/**
	 * Constructs a TurnActionBox representing the Turn Action with the passed
	 * turnActionName.
	 * 
	 * @param turnActionName
	 *            The name that represents a Turn Action.
	 * @param category
	 *            Category that the Turn Action belongs to.
	 * @param controller
	 *            Controller through which the Turn Action is
	 *            activated/deactivated.
	 * @param clickHandler
	 *            ClickHandler that the TurnActionBox belongs to.
	 */
	public TurnActionBox(String turnActionName, String category, Controller controller, ClickHandler clickHandler) {
		super(turnActionName, category, controller, clickHandler);
	}

	@Override
	protected void checkBoxAction(Boolean newValue) {
		this.actInAuthoringMode(this, null, getClickHandler(), null);
		if (newValue) {
			getController().activateTurnAction(getName());
		} else {
			getController().deactivateTurnAction(getName());
		}
	}

}
