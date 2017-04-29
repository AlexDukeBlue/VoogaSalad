package frontend.factory.wizard;

import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.StrategyFactory;
import util.polyglot.PolyglotException;

public class WizardFactory {
	public static Wizard<?> newWizard(String categoryName, AuthoringGameState gameState) {
		return new Wizard<>(StrategyFactory.newStrategy(categoryName, gameState));
	}

	public static Wizard<?> newWizard(String categoryName, AuthoringGameState gameState, String language,
			String stylesheet) {
		Wizard<?> wizard = new Wizard<>(StrategyFactory.newStrategy(categoryName, gameState));
		try {
			wizard.getPolyglot().setLanguage(language);
		} catch (PolyglotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wizard.getNode().getStylesheets().clear();
		wizard.getNode().getStylesheets().add(stylesheet);
		return wizard;
	}
}
