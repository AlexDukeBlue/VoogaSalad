package frontend.factory.wizard.wizards.strategies;

import backend.unit.properties.InteractionModifier;

public class InteractionModifierStrategy extends NameScriptBaseStrategy<InteractionModifier<?>>{

	@Override
	public InteractionModifier<?> finish() {
		return new InteractionModifier<>(getName(), (InteractionModifier.Modifier<?>)getScriptEngine(), getDescription(), getImgPath(), "");
	}

}
