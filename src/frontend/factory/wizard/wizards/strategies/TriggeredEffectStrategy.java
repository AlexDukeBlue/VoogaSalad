package frontend.factory.wizard.wizards.strategies;

import backend.util.AuthoringGameState;
import backend.util.ModifiableTriggeredEffect;
import backend.util.TriggeredEffect;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ActivationTriggersPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.NumTurnsPage;

public class TriggeredEffectStrategy extends NameScriptBaseStrategy<TriggeredEffect> {

	private NumTurnsPage numTurnsPage;
	private ActivationTriggersPage activationTriggersPage;

	public TriggeredEffectStrategy(AuthoringGameState gameState) {
		initialize(gameState);
	}

	@Override
	public TriggeredEffect finish() {
		return new ModifiableTriggeredEffect(getName(), getScriptEngine(), numTurnsPage.getNumTurns(), getDescription(),
				getImgPath(), activationTriggersPage.getSelectedActivationTriggers());
	}

	private void initialize(AuthoringGameState gameState) {
		numTurnsPage = new NumTurnsPage();
		getPages().add(numTurnsPage);
		activationTriggersPage = new ActivationTriggersPage(gameState);
		getPages().add(activationTriggersPage);
	}

}
