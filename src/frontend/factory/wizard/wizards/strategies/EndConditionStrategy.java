package frontend.factory.wizard.wizards.strategies;

import backend.game_engine.Resultant;
import javafx.beans.binding.StringBinding;

public class EndConditionStrategy extends NameScriptBaseStrategy<Resultant>{

	public EndConditionStrategy() {
		super("EndConditionNamePageDescription","EndConditionScriptingPageDescription");
		setScriptPrompt("EndCondition_Example_");
	}
	
	@Override
	public Resultant finish() {
		return new Resultant(getScriptEngine(), getName(), getDescriptionBoxText(), getImgPath());
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("EndConditionTitle");
	}

}
