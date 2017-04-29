package frontend.startup;

import javafx.stage.Stage;

/**
 * This interface specifies the actions that the StartupScreen must be able to
 * do: create a new game, play an existing game, or join a game currently being
 * hosted. The behavior has been abstracted to an interface so that the
 * implementation of it can be changed easily as the program continues to
 * evolve; someone can simply write a different concrete version of the
 * StartupDelegate to load or play games in a different way.
 * 
 * @author Dylan Peters
 *
 */
interface StartupDelegate {

	/**
	 * Creates a new game in edit mode. Can either create a whole new game or
	 * load a game from a file.
	 * 
	 * @param stage
	 *            the stage on which to show the game.
	 */
	void create(Stage stage);

	/**
	 * Attempts to join a game that is currently being hosted on someone else's
	 * computer.
	 * 
	 * @param stage
	 *            the stage on which to show the game.
	 */
	void join(Stage stage);

	/**
	 * Enters play-only mode for the game that the user chooses to load. This
	 * prevents the user from being able to edit the game.
	 * 
	 * @param stage
	 *            the stage on which to show the game.
	 * 
	 */
	void play(Stage stage);

}
