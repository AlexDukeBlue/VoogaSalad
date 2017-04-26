package frontend.factory.wizard.wizards.strategies;

import backend.util.ModifiableTriggeredEffect;
import backend.util.TriggeredEffect;

public class TriggeredEffectStrategy extends NameScriptBaseStrategy<TriggeredEffect> {

	@Override
	public TriggeredEffect finish() {
		return new ModifiableTriggeredEffect(getName(), getScriptEngine(), numTurns, getDescription(), getImgPath(), activationTriggers);
	}

}
