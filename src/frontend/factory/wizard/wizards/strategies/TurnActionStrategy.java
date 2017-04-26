package frontend.factory.wizard.wizards.strategies;

import backend.util.Actionable;

public class TurnActionStrategy extends NameScriptBaseStrategy<Actionable>{

	public TurnActionStrategy() {
		setTitle(getPolyglot().get("TurnActionStrategyTitle"));
		setDescription(getPolyglot().get("TurnActionStrategyDescription"));
	}
	
	@Override
	public Actionable finish() {
		return new Actionable(getScriptEngine(), getName(), getDescription(), getImgPath());
	}

}
