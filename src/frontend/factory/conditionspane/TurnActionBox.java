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

	Event myEvent;
	
	/**
	 * @param turnActionName
	 * @param controller
	 * @param clickHandler
	 */
	public TurnActionBox(String turnActionName, Controller controller, ClickHandler clickHandler, Event event) {
		super(turnActionName, controller, clickHandler);
		myEvent = event;
	}

	@Override
	protected void checkBoxAction(ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue) {
		if(newValue){
			getController().activateTurnAction(myEvent, getName());
		} else {
			getController().deactivateTurnAction(myEvent, getName());
		}
	}

}
