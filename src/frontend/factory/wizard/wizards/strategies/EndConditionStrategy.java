package frontend.factory.wizard.wizards.strategies;

import backend.game_engine.Resultant;

public class EndConditionStrategy extends NameScriptBaseStrategy<Resultant>{

	@Override
	public Resultant finish() {
		return new Resultant(getScriptEngine(), getName(), getDescription(), getImgPath());
	}

}
