/**
 * 
 */
package frontend.factory.conditionspane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import backend.game_engine.Resultant;
import backend.util.Actionable;
import backend.util.Requirement;
import controller.Controller;
import frontend.ClickHandler;
import util.polyglot.Polyglot;

/**
 * @author Stone Mathers Created 4/25/2017
 */
public class UpdatableConditionVBoxFactory {

	private Controller myController;
	private ClickHandler myClickHandler;

	public UpdatableConditionVBoxFactory(Controller controller, ClickHandler clickHandler) {
		myController = controller;
		myClickHandler = clickHandler;
	}

	public UpdatableConditionVBox createConditionVBox(Polyglot poly, String type) {
		if (type.equals(poly.get("TurnRequirements").get())) {
			return createTurnRequirementsVBox(type);
		} else if (type.equals(poly.get("TurnActions").get())) {
			return createTurnActionsVBox(type);
		} else if (type.equals(poly.get("EndConditions").get())) {
			return createEndConditionsVBox(type);
		} else {
			throw new RuntimeException(String.format(poly.get("NoConditionBoxError").get(), type));
		}
	}

	private UpdatableConditionVBox createTurnRequirementsVBox(String type) {
		ArrayList<ConditionBox> condBoxes = new ArrayList<ConditionBox>();
		Collection<Requirement> requirements = (Collection<Requirement>) myController.getTemplatesByCategory(type);
		requirements.forEach(req -> condBoxes.add(new TurnRequirementBox(req.getName(), myController, myClickHandler)));
		
		return new UpdatableConditionVBox(condBoxes, subBoxes -> {
			requirements.forEach(req -> {
				if(subBoxes.stream().map(box -> box.getName()).filter(boxName -> boxName.equals(req.getName())).count() == 0){
					subBoxes.add(new TurnRequirementBox(req.getName(), myController, myClickHandler));
				}
			});
			subBoxes.forEach(box -> {
				if(requirements.stream().map(req -> req.getName()).filter(reqName -> reqName.equals(box.getName())).count() == 0){
					subBoxes.remove(box);
				}
			});
		});
	}

	private UpdatableConditionVBox createTurnActionsVBox(String type) {
		ArrayList<ConditionBox> condBoxes = new ArrayList<ConditionBox>();
		Collection<Actionable> actions = (Collection<Actionable>) myController.getTemplatesByCategory(type);
		actions.forEach(act -> condBoxes.add(new TurnActionBox(act.getName(), myController, myClickHandler)));
		
		return new UpdatableConditionVBox(condBoxes, subBoxes -> {
			actions.forEach(act -> {
				if(subBoxes.stream().map(box -> box.getName()).filter(boxName -> boxName.equals(act.getName())).count() == 0){
					subBoxes.add(new TurnActionBox(act.getName(), myController, myClickHandler));
				}
			});
			subBoxes.forEach(box -> {
				if(actions.stream().map(act -> act.getName()).filter(actName -> actName.equals(box.getName())).count() == 0){
					subBoxes.remove(box);
				}
			});
		});
	}

	private UpdatableConditionVBox createEndConditionsVBox(String type) {
		ArrayList<ConditionBox> condBoxes = new ArrayList<ConditionBox>();
		Collection<Resultant> resultants = (Collection<Resultant>) myController.getTemplatesByCategory(type);
		resultants.forEach(res -> condBoxes.add(new EndConditionBox(res.getName(), myController, myClickHandler)));
		
		return new UpdatableConditionVBox(condBoxes, subBoxes -> {
			resultants.forEach(res -> {
				if(subBoxes.stream().map(box -> box.getName()).filter(boxName -> boxName.equals(res.getName())).count() == 0){
					subBoxes.add(new EndConditionBox(res.getName(), myController, myClickHandler));
				}
			});
			subBoxes.forEach(box -> {
				if(resultants.stream().map(res -> res.getName()).filter(resName -> resName.equals(box.getName())).count() == 0){
					subBoxes.remove(box);
				}
			});
		});
	}
}
