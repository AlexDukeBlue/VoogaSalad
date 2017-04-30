/**
 * 
 */
package frontend.factory.conditionspane;

import backend.util.Event;
import controller.Controller;
import frontend.ClickHandler;
import javafx.beans.value.ObservableValue;

/**
 * @author Stone Mathers
 * Created 4/25/2017
 */
public class TurnActionBox extends ConditionBox {
	
	/**
	 * @param turnActionName
	 * @param controller
	 * @param clickHandler
	 */
	public TurnActionBox(String turnActionName, Controller controller, ClickHandler clickHandler) {
		super(turnActionName, controller, clickHandler);
	}

	@Override
	protected void checkBoxAction(ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) {
		if(newValue){
			getController().activateTurnAction(getName());
		} else {
			getController().deactivateTurnAction(getName());
		}
	}

}
