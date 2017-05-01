package backend.game_engine;

import backend.game_engine.ResultQuadPredicate.Result;
import backend.player.Team;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.io.XMLSerializer;
import util.AlertFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alexander Zapata
 */

public class DieselEngine implements GameEngine {

	private GameplayState currentState;

	public DieselEngine() {
		currentState = null;
	}

	/**
	 * This method compiles all of the necessary checking methods to run at the
	 * change of a GameState. This method is what gets passed to the Server to
	 * execute with lambdas.
	 *
	 * @param state
	 */
	public void checkGame(GameplayState state) {
		currentState = state;
		if (!state.isAuthoringMode()) {
			checkTurnRules(state);
			checkTurnEvents(state);
			checkObjectives(state);
		}
	}

	@Override
	public void save(GameplayState gameState) {
		try {
			new XMLSerializer<>().doSerialize(gameState);
		} catch (Exception e) {
			saveFailAlert();
		}
	}

	@Override
	public AuthoringGameState load(File gameStateFile) {
		AuthoringGameState newGameState;
		try {
			newGameState = (AuthoringGameState) new XMLSerializer<>()
					.unserialize(new String(Files.readAllBytes(Paths.get(gameStateFile.getPath()))));
		} catch (Exception e) {
			loadFailAlert();
			newGameState = null;
		}
		return newGameState;
	}

	/**
	 * This method will take the TurnRequirements within GameState (which are
	 * wrapped by Requirement) and in the case they are all satisfied call
	 * state.endTurn().
	 *
	 * @param state
	 */
	private void checkTurnRules(GameplayState state) {
		if (!state.getActiveTurnRequirements().parallelStream().allMatch(e -> e.test(state.getActiveTeam(), state))
				&& state.turnRequirementsSatisfied())
			state.endTurn();
	}

	/**
	 * This method will go through all of the TurnEvents (wrapped by
	 * Actionables) and execute their BIPredicates when added as a listener in
	 * the Server.
	 *
	 * @param state
	 */
	private void checkTurnEvents(GameplayState state) {
		state.getActiveTurnActions()
				.forEach(action -> state.getTeams().forEach(team -> action.accept(team, state)));
	}

	/**
	 * This method goes through all of the objectives of the GameState so that
	 * every-time a state changes the Server can check for a winner.
	 *
	 * @param state
	 */
	private void checkObjectives(GameplayState state) {
		state.getActiveObjectives().parallelStream().forEach(e -> {
			Result result = e.getResultQuad().determine(state.getActiveTeam(), state);
			result.accept(state.getActiveTeam(), this);
		});
	}

	@Override
	public void handleWin(Team team) {
		currentState.getOrderedPlayerNames().stream().map(playerName -> currentState.getPlayerByName(playerName))
				.forEach(aPlayer -> aPlayer.setResult(aPlayer.getTeam().equals(team) ? Result.WIN : Result.LOSE));
	}

	@Override
	public void handleLoss(Team team) {
		team.getAll().forEach(player -> player.setResult(Result.LOSE));
		checkForOneRemainingTeam();
	}

	@Override
	public void handleTie() {
		currentState.getOrderedPlayerNames().forEach(name -> currentState.getPlayerByName(name).setResult(Result.TIE));
	}

	private void saveFailAlert() {
		AlertFactory.errorAlert("Could not save file", "An error occured. Try saving again?", "").showAndWait();
	}

	private void loadFailAlert() {
		AlertFactory.errorAlert("Could not load file", "An error occured. Try loading again?", "").showAndWait();
	}

	private void checkForOneRemainingTeam() {
		Set<Team> remainingTeams = currentState.getOrderedPlayerNames().stream()
				.filter(playerName -> currentState.getPlayerByName(playerName).getResult().equals(Result.NONE))
				.map(playerName -> currentState.getPlayerByName(playerName).getTeam().orElse(null))
				.collect(Collectors.toSet());
		if (remainingTeams.size() == 1) {
			remainingTeams.forEach(lastTeam -> lastTeam.getAll().forEach(player -> player.setResult(Result.WIN)));
		}
	}

}