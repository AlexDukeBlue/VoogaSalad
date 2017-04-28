/**
 * 
 */
package frontend.factory.conditionspane;

import java.util.Collection;

import backend.game_engine.Resultant;
import backend.util.Actionable;
import backend.util.Event;
import backend.util.Requirement;
import controller.Controller;
import frontend.ClickHandler;
import javafx.scene.layout.VBox;
import polyglot.Polyglot;

/**
 * @author Stone Mathers
 * Created 4/25/2017
 */
public class ConditionVBoxFactory {

	private Controller myController;
	private ClickHandler myClickHandler;
	
	public ConditionVBoxFactory(Controller controller, ClickHandler clickHandler){
		myController = controller;
		myClickHandler = clickHandler;
	}
	
	public VBox createConditionVBox(Polyglot poly, String type){
		if(type.equals(poly.get("TurnRequirements").toString())){
			return createTurnRequirementsVBox();
		} else if(type.equals(poly.get("TurnActions").toString())){
			return createTurnActionsVBox();
		} else if(type.equals(poly.get("EndConditions").toString())){
			return createEndConditionsVBox();
		} else {
			return new VBox();
		}
	}

	private VBox createTurnRequirementsVBox() {
		VBox box = new VBox();	
		for(Requirement req: myController.getAuthoringGameState().getAvailableTurnRequirements()){
			box.getChildren().add((new TurnRequirementBox(req.getName(), myController, myClickHandler)).getNode());
		}		
		return box;
	}
	
	private VBox createTurnActionsVBox() {
		VBox box = new VBox();
		
		for(Event event: myController.getAuthoringGameState().getAvailableTurnActions().keySet()){
			for(Actionable act: myController.getAuthoringGameState().getAvailableTurnActions().get(event)){
				box.getChildren().add((new TurnActionBox(act.getName(), myController, myClickHandler, event)).getNode());
			}
		}
		
		return box;
	}
	
	private VBox createEndConditionsVBox() {
		VBox box = new VBox();		
		for(Resultant res: myController.getAuthoringGameState().getAvailableObjectives()){
			box.getChildren().add((new EndConditionBox(res.getName(), myController, myClickHandler)).getNode());
		}	
		return box;
	}

}
