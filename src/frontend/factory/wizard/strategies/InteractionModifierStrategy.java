package frontend.factory.wizard.strategies;

import backend.unit.properties.InteractionModifier;
import controller.Controller;
import javafx.beans.binding.StringBinding;

/**
 * InteractionModifierStrategy is a WizardStrategy that allows the user to
 * create new InteractionModifiers.
 * 
 * @author Dylan Peters
 *
 */
class InteractionModifierStrategy extends NameScriptBaseStrategy<InteractionModifier<?>> {

	/**
	 * Creates a new InteractionModifierStrategy
	 */
	public InteractionModifierStrategy(Controller controller) {
		super(controller, "InteractionModifierNamePageDescription", "InteractionModifierScriptingPageDescription");
	}

	/**
	 * Returns a fully instantiated InteractionModifier.
	 */
	@Override
	public InteractionModifier<?> finish() {
		return new InteractionModifier<>(getName(), (InteractionModifier.Modifier<?>) getScriptEngine(),
				getDescriptionBoxText(), getImgPath(), "");
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("InteractionModifierTitle");
	}

}
