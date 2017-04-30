/**
 * 
 */
package frontend.factory.conditionspane;

import controller.Controller;
import frontend.ClickHandler;
import javafx.beans.value.ObservableValue;

/**
 * @author Stone Mathers
 * Created 4/25/2017
 */
public class EndConditionBox extends ConditionBox {

	public EndConditionBox(String resultantName, String category, Controller controller, ClickHandler clickHandler) {
		super(resultantName, category, controller, clickHandler);
	}

	@Override
	protected void checkBoxAction(ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) {
		this.actInAuthoringMode(this, null, getClickHandler(), null);
		if(newValue){
			getController().activateEndCondition(getName());
		} else {
			getController().deactivateEndCondition(getName());
		}
	}
}
