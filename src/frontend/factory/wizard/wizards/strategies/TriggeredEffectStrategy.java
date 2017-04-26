package frontend.factory.wizard.wizards.strategies;

import backend.util.ModifiableTriggeredEffect;
import backend.util.TriggeredEffect;

import java.util.Collections;

public class TriggeredEffectStrategy extends NameScriptBaseStrategy<TriggeredEffect> {

	@Override
	public TriggeredEffect finish() {
		return new ModifiableTriggeredEffect(getName(), getScriptEngine(), 0, getDescription(), getImgPath(), Collections.emptyList());
	}

}
