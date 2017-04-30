package frontend.factory.conditionspane;

import controller.Controller;
import frontend.ClickHandler;
import javafx.beans.value.ObservableValue;

/**
 * A ConditionBox that holds a Turn Requirement by name. When the checkbox is activated,
 * the Controller is used to activate the Turn Requirement, and vice versa.
 *
 * @author Stone Mathers
 * Created 4/25/2017
 */
public class TurnRequirementBox extends ConditionBox {

	public TurnRequirementBox(String turnRequirementName, String category, Controller controller,
			ClickHandler clickHandler) {
		super(turnRequirementName, category, controller, clickHandler);
	}

	@Override
	protected void checkBoxAction(ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) {
		this.actInAuthoringMode(this, null, getClickHandler(), null);
		if (newValue) {
			System.out.println("add req");
			System.out.println(getController().getAuthoringGameState().getActiveTurnRequirements().size());
			getController().activateTurnRequirement(getName());
			System.out.println(getController().getAuthoringGameState().getActiveTurnRequirements().size());
		} else {
			System.out.println("remove req");
			System.out.println(getController().getAuthoringGameState().getActiveTurnRequirements().size());
			getController().deactivateTurnRequirement(getName());
			System.out.println(getController().getAuthoringGameState().getActiveTurnRequirements().size());
		}
	}
}
