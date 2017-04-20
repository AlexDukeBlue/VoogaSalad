package backend.game_engine;

import backend.player.ImmutablePlayer;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;

import java.io.File;

/**
 * @author Alexander Zapata
 */
public interface GameEngine {
	
	/**
	 * Save is the method that will be called by the controller or the server to save the passed-in GameState to XML.
	 * Ideally, the save method should use the existing name of the GameState to name the file that it will write to
	 * which will be loaded with a soon-to-be explained method.
	 * 
	 * @param gameState
	 */
	void save(GameplayState gameState);

	/**
	 * This takes in a file of a previously saved game-state and loads it into an AuthoringGameState.
	 * The usage of this would reside primarily server-side, so in the implementation of the server
	 * would be the changing of this AuthoringGameState into a GameplayState.
	 * 
	 * @param gameStateFile
	 * @return AuthoringGameState
	 */
	AuthoringGameState load(File gameStateFile);

	/**	 
	 * The handleWin method is what gets called by a Result.WIN.accept() call. This method 
	 * is effectively the decider for the actions that occur when a player wins. In future
	 * implementation, this method will probably get passed to the server for it to control
	 * what happens from the enclosed BiConsumer.
	 * 
	 * @param player
	 * @return Object
	 */
	Object handleWin(ImmutablePlayer player);

	/**
	 * The handleLoss method is what gets called by a Result.LOSE.accept() call. This method 
	 * is effectively the decider for the actions that occur when a player loses. In future
	 * implementation, this method will probably get passed to the server for it to control
	 * what happens from the enclosed BiConsumer.
	 * 
	 * @param player
	 * @return
	 */
	Object handleLoss(ImmutablePlayer player);

	/**
	 * The handleTie method is what gets called by a Result.Tie.accept() call. This method 
	 * is effectively the decider for the actions that occur when a player Ties. In future
	 * implementation, this method will probably get passed to the server for it to control
	 * what happens from the enclosed BiConsumer. In a lot of cases, the same thing probably
	 * occurs as when Result.NONE.accept() is called, but that is uniquely up to the game.
	 * 
	 * @return
	 */
	Object handleTie();
	
}