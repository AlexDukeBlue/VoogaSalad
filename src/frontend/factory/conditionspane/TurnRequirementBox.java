package frontend.factory.conditionspane;

import controller.Controller;
import frontend.ClickHandler;

/**
 * A ConditionBox that holds a Turn Requirement by name. When the checkbox is
 * activated, the Controller is used to activate the Turn Requirement. When the
 * checkbox is deactivated, the Controller is used to deactivate the Turn
 * Requirement.
 *
 * @author Stone Mathers Created 4/25/2017
 */
public class TurnRequirementBox extends ConditionBox {

	/**
	 * Constructs a TurnRequirementBox representing the Turn Requirement with
	 * the passed turnRequirementName.
	 * 
	 * @param turnRequirementName
	 *            The name that represents a Turn Requirement.
	 * @param category
	 *            Category that the Turn Requirement belongs to.
	 * @param controller
	 *            Controller through which the Turn Requirement is
	 *            activated/deactivated.
	 * @param clickHandler
	 *            ClickHandler that the TurnRequirementBox belongs to.
	 */
	public TurnRequirementBox(String turnRequirementName, String category, Controller controller,
			ClickHandler clickHandler) {
		super(turnRequirementName, category, controller, clickHandler);
	}

	@Override
	protected void checkBoxAction(Boolean newValue) {
		this.actInAuthoringMode(this, null, getClickHandler(), null);
		if (newValue) {
			getController().activateTurnRequirement(getName());
		} else {
			getController().deactivateTurnRequirement(getName());
		}
	}
}
