package frontend.factory.wizard.wizards.strategies;

import backend.util.Requirement;
import javafx.beans.binding.StringBinding;

public class TurnRequirementStrategy extends NameScriptBaseStrategy<Requirement> {

	public TurnRequirementStrategy() {
		super("TurnRequirementNamePageDescription","TurnRequirementScriptingPageDescription");
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
