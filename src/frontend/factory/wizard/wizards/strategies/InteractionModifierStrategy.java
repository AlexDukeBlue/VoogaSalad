package frontend.factory.wizard.wizards.strategies;

import backend.unit.properties.InteractionModifier;
import javafx.beans.binding.StringBinding;

public class InteractionModifierStrategy extends NameScriptBaseStrategy<InteractionModifier<?>>{

	public InteractionModifierStrategy() {
		super("InteractionModifierNamePageDescription", "InteractionModifierScriptingPageDescription");
	}

	@Override
	public InteractionModifier<?> finish() {
		return new InteractionModifier<>(getName(), (InteractionModifier.Modifier<?>)getScriptEngine(), getDescriptionBoxText(), getImgPath(), "");
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("InteractionModifierTitle");
	}

}
