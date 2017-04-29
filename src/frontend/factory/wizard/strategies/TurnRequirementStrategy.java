package frontend.factory.wizard.strategies;

import backend.util.Requirement;
import controller.Controller;
import javafx.beans.binding.StringBinding;

/**
 * TurnRequirementStrategy allows the user to create new TurnRequirements.
 * 
 * @author Dylan Peters
 *
 */
class TurnRequirementStrategy extends NameScriptBaseStrategy<Requirement> {

	/**
	 * Creates a new instance of TurnReuirementStrategy.
	 */
	public TurnRequirementStrategy(Controller controller) {
		super(controller, "TurnRequirementNamePageDescription", "TurnRequirementScriptingPageDescription");
		setScriptPrompt("TurnRequirement_Example_");
	}

	@Override
	public Requirement finish() {
		return new Requirement(getScriptEngine(), getName(), getDescriptionBoxText(), getImgPath());
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("TurnRequirementTitle");
	}

}
