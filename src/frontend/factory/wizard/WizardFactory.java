package frontend.factory.wizard;

import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.StrategyFactory;

public class WizardFactory {
	public static Wizard<?> newWizard(String categoryName, AuthoringGameState gameState) {
		return new Wizard<>(StrategyFactory.newStrategy(categoryName, gameState));
	}
}
