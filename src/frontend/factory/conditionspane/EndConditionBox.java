package frontend.factory.conditionspane;

import controller.Controller;
import frontend.ClickHandler;
import javafx.beans.value.ObservableValue;

/**
 * A ConditionBox that holds a. End Condition by name. When the checkbox is
 * activated, the Controller is used to activate the End Condition. When the
 * checkbox is deactivated, the Controller is used to deactivate the End
 * Condition.
 *
 * @author Stone Mathers Created 4/25/2017
 */
public class EndConditionBox extends ConditionBox {

	/**
	 * Constructs an EndConditionBox representing the EndCondition with the
	 * passed endConditionName.
	 * 
	 * @param endConditionName
	 *            The name that represents an End Condition.
	 * @param category
	 *            Category that the End Condition belongs to.
	 * @param controller
	 *            Controller through which the End Condition is
	 *            activated/deactivated.
	 * @param clickHandler
	 *            ClickHandler that the EndConditionBox belongs to.
	 */
	public EndConditionBox(String endConditionName, String category, Controller controller, ClickHandler clickHandler) {
		super(endConditionName, category, controller, clickHandler);
	}

	@Override
	protected void checkBoxAction(ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) {
		this.actInAuthoringMode(this, null, getClickHandler(), null);
		if (newValue) {
			getController().activateEndCondition(getName());
		} else {
			getController().deactivateEndCondition(getName());
		}
	}
}
