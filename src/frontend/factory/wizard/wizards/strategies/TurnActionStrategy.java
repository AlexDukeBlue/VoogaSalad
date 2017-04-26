package frontend.factory.wizard.wizards.strategies;

import backend.util.Actionable;

public class TurnActionStrategy extends NameScriptBaseStrategy<Actionable>{

	public TurnActionStrategy() {
		setTitle(getPolyglot().get("TurnActionStrategyTitle"));
		setDescription(getPolyglot().get("TurnActionStrategyDescription"));
		setScriptPrompt("TurnAction_Example_");
	}
	
	@Override
	public Actionable finish() {
		return new Actionable(getScriptEngine(), getName(), getDescription(), getImgPath());
	}

}
