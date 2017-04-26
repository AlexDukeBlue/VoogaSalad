/**
 *
 */
package frontend.factory.conditionspane;

import controller.Controller;
import frontend.ClickHandler;
import javafx.beans.value.ObservableValue;

/**
 * @author Stone Mathers
 *         Created 4/25/2017
 */
public class TurnRequirementBox extends ConditionBox {

	public TurnRequirementBox(String turnRequirementName, Controller controller, ClickHandler clickHandler) {
		super(turnRequirementName, controller, clickHandler);
	}

	@Override
	protected void checkBoxAction(ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) {
		if(newValue){
			getController().activateTurnRequirement(getName());
		} else {
			getController().deactivateTurnRequirement(getName());
		}
	}
}
