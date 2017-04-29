package frontend.factory.wizard.strategies;

import backend.util.AuthoringGameState;
import backend.util.ModifiableTriggeredEffect;
import backend.util.TriggeredEffect;
import frontend.factory.wizard.strategies.wizard_pages.ActivationTriggersPage;
import frontend.factory.wizard.strategies.wizard_pages.NumTurnsPage;
import javafx.beans.binding.StringBinding;

/**
 * TriggeredEffectStrategy creates a WizardStrategy that gathers information
 * from the user required to instantiate a new TriggeredEffect.
 * 
 * @author Dylan Peters
 *
 */
class TriggeredEffectStrategy extends NameScriptBaseStrategy<TriggeredEffect> {

	private NumTurnsPage numTurnsPage;
	private ActivationTriggersPage activationTriggersPage;

	/**
	 * Instantiates a new TriggeredEffectStrategy.
	 * 
	 * @param gameState
	 *            the AuthoringGameState that will be used to instatiate parts
	 *            of the wizard
	 */
	public TriggeredEffectStrategy(AuthoringGameState gameState) {
		super("TriggeredEffectNamePageDescription", "TriggeredEffectScriptingPageDescription");
		initialize(gameState);
	}

	/**
	 * Returns a fully instnatiated TriggeredEffect object
	 */
	@Override
	public TriggeredEffect finish() {
		return new ModifiableTriggeredEffect(getName(), getScriptEngine(), numTurnsPage.getNumTurns(),
				getDescriptionBoxText(), getImgPath(), activationTriggersPage.getSelectedActivationTriggers());
	}

	private void initialize(AuthoringGameState gameState) {
		numTurnsPage = new NumTurnsPage("TriggeredEffectNumTurnsPageDescription");
		getPages().add(numTurnsPage);
		activationTriggersPage = new ActivationTriggersPage(gameState,
				"TriggeredEffectActivationTriggersPageDescription");
		getPages().add(activationTriggersPage);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("TriggeredEffectWizardTitle");
	}

}
