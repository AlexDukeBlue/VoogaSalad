package frontend.wizards;

import backend.util.AuthoringGameState;
import frontend.wizards.selection_strategies.GameStrategy;
import javafx.stage.Stage;

/**
 * GameWizard creates objects of the type Game. It extends Wizard and is used
 * to get information from the user about the Game to be instantiated.
 * 
 * @author Dylan Peters
 *
 */
public class GameWizard extends Wizard<AuthoringGameState> {

	/**
	 * Creates a new instance of GameWizard. Sets all values to default.
	 */
	public GameWizard() {
		this(new Stage());
	}

	/**
	 * Creates a new instance of GameWizard. Sets all values to default.
	 * 
	 * @param stage the stage on which to display the GameWizard
	 */
	GameWizard(Stage stage) {
		super(stage, new GameStrategy());
	}

}
