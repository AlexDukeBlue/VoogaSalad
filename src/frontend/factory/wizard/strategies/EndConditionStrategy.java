package frontend.factory.wizard.strategies;

import backend.game_engine.Resultant;
import controller.Controller;
import javafx.beans.binding.StringBinding;

/**
 * EndConditionStrategy is a WizardStrategy that allows the user to create new
 * EndConditions
 * 
 * @author Dylan Peters
 *
 */
class EndConditionStrategy extends NameScriptBaseStrategy<Resultant> {

	/**
	 * Creates a new instance of EndConditionsStrategy
	 */
	public EndConditionStrategy(Controller controller) {
		super(controller, "EndConditionNamePageDescription", "EndConditionScriptingPageDescription");
		setScriptPrompt("EndCondition_Example_");
	}

	/**
	 * Returns a fully instantiated instance of Resultant
	 */
	@Override
	public Resultant finish() {
		return new Resultant(getScriptEngine(), getName(), getDescriptionBoxText(), getImgPath());
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("EndConditionTitle");
	}

}
