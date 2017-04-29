package frontend.factory.wizard.wizards.strategies;

import backend.util.Actionable;
import javafx.beans.binding.StringBinding;

public class TurnActionStrategy extends NameScriptBaseStrategy<Actionable>{

	public TurnActionStrategy() {
		super("TurnActionNamePageDescription","TurnActionScriptingPageDescription");
		setScriptPrompt("TurnAction_Example_");
	}
	
	@Override
	public Actionable finish() {
		return new Actionable(getScriptEngine(), getName(), getDescriptionBoxText(), getImgPath());
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("TurnActionWizardTitle");
	}

}
