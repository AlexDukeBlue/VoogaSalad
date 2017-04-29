package frontend.factory.wizard.wizards.strategies;

import backend.util.AuthoringGameState;
import backend.util.ModifiableTriggeredEffect;
import backend.util.TriggeredEffect;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ActivationTriggersPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.NumTurnsPage;
import javafx.beans.binding.StringBinding;

public class TriggeredEffectStrategy extends NameScriptBaseStrategy<TriggeredEffect> {

	private NumTurnsPage numTurnsPage;
	private ActivationTriggersPage activationTriggersPage;

	public TriggeredEffectStrategy(AuthoringGameState gameState) {
		super("TriggeredEffectNamePageDescription","TriggeredEffectScriptingPageDescription");
		initialize(gameState);
	}

	@Override
	public TriggeredEffect finish() {
		return new ModifiableTriggeredEffect(getName(), getScriptEngine(), numTurnsPage.getNumTurns(), getDescriptionBoxText(),
				getImgPath(), activationTriggersPage.getSelectedActivationTriggers());
	}

	private void initialize(AuthoringGameState gameState) {
		numTurnsPage = new NumTurnsPage("TriggeredEffectNumTurnsPageDescription");
		getPages().add(numTurnsPage);
		activationTriggersPage = new ActivationTriggersPage(gameState,"TriggeredEffectActivationTriggersPageDescription");
		getPages().add(activationTriggersPage);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("TriggeredEffectWizardTitle");
	}

}
