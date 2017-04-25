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

	public EndConditionBox(String resultantName, Controller controller, ClickHandler clickHandler) {
		super(resultantName, controller, clickHandler);
	}

	@Override
	protected void checkBoxAction(ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) {
		if(newValue){
			getController().activateEndCondition(getName());
		} else {
			getController().deactivateEndCondition(getName());
		}
	}
}
