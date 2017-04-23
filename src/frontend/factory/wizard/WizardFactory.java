package frontend.factory.wizard;

import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.StrategyFactory;

public class WizardFactory {

	public static Wizard<? extends Object> newWizard(Class<? extends Object> clazz, AuthoringGameState gameState){
		return new Wizard<>(StrategyFactory.newStrategy(clazz,gameState));
	}
	
}
