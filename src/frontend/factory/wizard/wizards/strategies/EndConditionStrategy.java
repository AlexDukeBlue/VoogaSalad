package frontend.factory.wizard.wizards.strategies;

import backend.game_engine.Resultant;

public class EndConditionStrategy extends NameScriptBaseStrategy<Resultant>{

	public EndConditionStrategy() {
		setTitle(getPolyglot().get("EndConditionStrategyTitle"));
		setDescription(getPolyglot().get("EndConditionStrategyDescription"));
		setScriptPrompt("EndCondition_Example_");
	}
	
	@Override
	public Resultant finish() {
		return new Resultant(getScriptEngine(), getName(), getDescription(), getImgPath());
	}

}
