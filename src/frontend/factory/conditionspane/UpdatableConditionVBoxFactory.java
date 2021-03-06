package frontend.factory.conditionspane;

import java.util.ArrayList;
import java.util.Collection;
import backend.game_engine.Resultant;
import backend.util.Actionable;
import backend.util.Requirement;
import controller.Controller;
import frontend.ClickHandler;
import util.polyglot.Polyglot;

/**
 * UpdatableConditionVBoxFactory specifies methods for creating
 * UpdatableConditionVBox objects in order to encapsulate the logic needed to
 * determine how to instantiate an UpdatableConditionVBox for each type of
 * condition, primarily the logic of that Consumer that must be passed in.
 * <p>
 * The Factory pattern is used so that one need only know the String for the
 * type of condition that they want to create an UpdatableConditionVBox for, and
 * not the Controller logic that is needed to update it.
 * 
 * @author Stone Mathers Created 4/25/2017
 */
public class UpdatableConditionVBoxFactory {

	private Controller myController;
	private ClickHandler myClickHandler;

	/**
	 * Constructs an UpdatableConditionVBoxFactory using the given parameters.
	 * 
	 * @param controller
	 *            Controller that the UpdatableConditionVBox will use to
	 *            communicate with the Model.
	 * @param clickHandler
	 *            The ClickHandler that the UpdatableConditionVBox will use to
	 *            determine behavior on clicks.
	 */
	public UpdatableConditionVBoxFactory(Controller controller, ClickHandler clickHandler) {
		myController = controller;
		myClickHandler = clickHandler;
	}

	/**
	 * Creates and returns an UpdatableConditionVBox, determined by the passed
	 * type.
	 * 
	 * @param poly
	 *            PolyGlot used to receive a uniform String from the passed type
	 *            String.
	 * @param type
	 *            type of condition that will be held.
	 * @return The UpdatableConditionVBox object.
	 */
	public UpdatableVBox<ConditionBox> createConditionVBox(Polyglot poly, String type) {
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

	private UpdatableVBox<ConditionBox> createTurnRequirementsVBox(String type) {
		ArrayList<ConditionBox> condBoxes = new ArrayList<ConditionBox>();
		Collection<Requirement> requirements = (Collection<Requirement>) myController.getTemplatesByCategory(type);
		requirements.forEach(
				req -> condBoxes.add(new TurnRequirementBox(req.getName(), type, myController, myClickHandler)));

		return new UpdatableVBox<ConditionBox>(condBoxes, subBoxes -> {
			requirements.forEach(req -> {
				if (subBoxes.stream().map(box -> box.getName()).filter(boxName -> boxName.equals(req.getName()))
						.count() == 0) {
					subBoxes.add(new TurnRequirementBox(req.getName(), type, myController, myClickHandler));
				}
			});
			ArrayList<ConditionBox> boxesToRemove = new ArrayList<ConditionBox>();
			subBoxes.forEach(box -> {
				if (requirements.stream().map(req -> req.getName()).filter(reqName -> reqName.equals(box.getName()))
						.count() == 0) {
					boxesToRemove.add(box);
				}
			});
			subBoxes.removeAll(boxesToRemove);
		});
	}

	private UpdatableVBox<ConditionBox> createTurnActionsVBox(String type) {
		ArrayList<ConditionBox> condBoxes = new ArrayList<ConditionBox>();
		Collection<Actionable> actions = (Collection<Actionable>) myController.getTemplatesByCategory(type);
		actions.forEach(act -> condBoxes.add(new TurnActionBox(act.getName(), type, myController, myClickHandler)));

		return new UpdatableVBox<ConditionBox>(condBoxes, subBoxes -> {
			actions.forEach(act -> {
				if (subBoxes.stream().map(box -> box.getName()).filter(boxName -> boxName.equals(act.getName()))
						.count() == 0) {
					subBoxes.add(new TurnActionBox(act.getName(), type, myController, myClickHandler));
				}
			});
			ArrayList<ConditionBox> boxesToRemove = new ArrayList<ConditionBox>();
			subBoxes.forEach(box -> {
				if (actions.stream().map(act -> act.getName()).filter(actName -> actName.equals(box.getName()))
						.count() == 0) {
					boxesToRemove.add(box);
				}
			});
			subBoxes.removeAll(boxesToRemove);
		});
	}

	private UpdatableVBox<ConditionBox> createEndConditionsVBox(String type) {
		ArrayList<ConditionBox> condBoxes = new ArrayList<ConditionBox>();
		Collection<Resultant> resultants = (Collection<Resultant>) myController.getTemplatesByCategory(type);
		resultants
				.forEach(res -> condBoxes.add(new EndConditionBox(res.getName(), type, myController, myClickHandler)));

		return new UpdatableVBox<ConditionBox>(condBoxes, subBoxes -> {
			resultants.forEach(res -> {
				if (subBoxes.stream().map(box -> box.getName()).filter(boxName -> boxName.equals(res.getName()))
						.count() == 0) {
					subBoxes.add(new EndConditionBox(res.getName(), type, myController, myClickHandler));
				}
			});
			ArrayList<ConditionBox> boxesToRemove = new ArrayList<ConditionBox>();
			subBoxes.forEach(box -> {
				if (resultants.stream().map(res -> res.getName()).filter(resName -> resName.equals(box.getName()))
						.count() == 0) {
					boxesToRemove.add(box);
				}
			});
			subBoxes.removeAll(boxesToRemove);
		});
	}
}
