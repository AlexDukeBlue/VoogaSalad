package frontend.factory.wizard.wizards.strategies;

import backend.util.Requirement;

public class TurnRequirementStrategy extends NameScriptBaseStrategy<Requirement> {

	@Override
	public Requirement finish() {
		return new Requirement(getScriptEngine(), getName(), getDescription(), getImgPath());
	}

}
