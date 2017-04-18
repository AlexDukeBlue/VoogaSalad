package frontend.wizards;

import backend.unit.Unit;
import backend.util.AuthoringGameState;
import frontend.wizards.strategies.UnitStrategy;
import javafx.stage.Stage;

/**
 * UnitWizard creates objects of the type Unit. It extends Wizard and is used
 * to get information from the user about the unit to be instantiated.
 * 
 * @author Dylan Peters
 *
 */
public class UnitWizard extends Wizard<Unit> {

	/**
	 * Creates a new instance of UnitWizard using the AuthoringGameState
	 * provided.
	 * 
	 * @param gameState
	 *            The AuthoringGameState from which to get information that will
	 *            be used to prompt the user.
	 */
	public UnitWizard(AuthoringGameState gameState) {
		this(new Stage(), gameState);
	}

	/**
	 * Creates a new instance of UnitWizard using the AuthoringGameState
	 * provided.
	 * 
	 * @param stage
	 *            the stage on which to display the UnitWizard
	 * @param gameState
	 *            The AuthoringGameState from which to get information that will
	 *            be used to prompt the user.
	 */
	private UnitWizard(Stage stage, AuthoringGameState gameState) {
		super(stage, new UnitStrategy(gameState));
	}

}
