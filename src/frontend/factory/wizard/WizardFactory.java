package frontend.factory.wizard;

import backend.util.AuthoringGameState;
import controller.Controller;
import frontend.factory.wizard.strategies.StrategyFactory;
import util.polyglot.PolyglotException;

/**
 * The WizardFactory uses the Factory design pattern to make it easy to generate
 * wizards for different types of objects. It allows the user to simply pass the
 * type of object he/she would like to create as a parameter, and the
 * WizardFactory produces the correct wizard.
 * 
 * @author Dylan Peters
 *
 */
public class WizardFactory {

	/**
	 * Creates a new wizard that produces an object of type specified by the
	 * categoryName, given the information stored in the GameState.
	 * 
	 * @param categoryName
	 *            a String describing the type of wizard to create. If the
	 *            string is not recognized as a valid category, the method
	 *            returns a Wizard that does nothing but notify the user that
	 *            the wizard is not supported.
	 * @param gameState
	 *            an AuthoringGameState that stores information to be used by
	 *            the wizard.
	 * @return a wizard that produces the correct type of object.
	 */
	public static Wizard<?> newWizard(String categoryName, Controller controller) {
		return new Wizard<>(controller,StrategyFactory.newStrategy(categoryName, controller));
	}

	/**
	 * Creates a new wizard that produces an object of type specified by the
	 * categoryName, given the information stored in the GameState.
	 * 
	 * @param categoryName
	 *            a String describing the type of wizard to create. If the
	 *            string is not recognized as a valid category, the method
	 *            returns a Wizard that does nothing but notify the user that
	 *            the wizard is not supported.
	 * @param gameState
	 *            an AuthoringGameState that stores information to be used by
	 *            the wizard.
	 * @param language
	 *            the language to display the wizard in.
	 * @param stylesheet
	 *            the stylesheet to style the wizard with
	 * @return
	 */
	public static Wizard<?> newWizard(String categoryName, Controller controller, String language,
			String stylesheet) {
		Wizard<?> wizard = new Wizard<>(controller,StrategyFactory.newStrategy(categoryName, controller));
		wizard.getPolyglot().setLanguage(language);
		wizard.getNode().getStylesheets().clear();
		wizard.getNode().getStylesheets().add(stylesheet);
		return wizard;
	}
}
