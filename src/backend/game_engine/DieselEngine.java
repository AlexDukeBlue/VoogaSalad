package backend.game_engine;

import backend.game_engine.ResultQuadPredicate.Result;
import backend.player.ImmutablePlayer;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.io.XMLSerializer;
import util.net.ObservableServer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author Zapata
 */

//TODO: Implement some way to checkTurnState() to determine if it is the beginning or end of a turn. Implement restart(), save() and load() (Tavo's job). Also implement a messagePlayer(Player from, Player to, String message).
public class DieselEngine implements GameEngine {

	private ObservableServer<GameplayState> server;
	private Consumer<GameplayState> stateUpdateListener = this::checkGame;

	public DieselEngine(ObservableServer<GameplayState> s) {
		server = s;
		server.addListener(stateUpdateListener);
		Executors.newSingleThreadExecutor().submit(server);
	}

	private void checkGame(GameplayState state) {
		checkTurnRules(state);
		checkTurnEvents(state);
		checkObjectives(state);
	}

	@Override
	public void save(GameplayState gameState) {
		try{
			new XMLSerializer<GameplayState>().doSerialize(gameState);
		}catch(Exception e){
			//Something here.
		}
	}

	@Override
	public AuthoringGameState load(File gameStateFile) {
		AuthoringGameState newGameState;
		try{
			newGameState = new XMLSerializer<AuthoringGameState>().unserialize(new String(Files.readAllBytes(Paths.get(gameStateFile.getPath()))));
		}catch(Exception e){
			//Something here.
			newGameState = null;
		}
		return newGameState;
	}

	private void checkTurnRules(GameplayState state) {
		if (!state.getTurnRequirements().parallelStream()
				.allMatch(e -> e.getBiPredicate().test(state.getCurrentPlayer(), state)) && state.turnRequirementsSatisfied()) state.endTurn();
	}

	private void checkTurnEvents(GameplayState state) {
		state.getTurnActions().forEach((key, value) -> value.forEach(t -> t.getBiComsumer().accept(state.getCurrentPlayer(), state)));
	}

	private void checkObjectives(GameplayState state) {
		state.getObjectives().parallelStream().forEach(e -> {
			Result result = e.getResultQuad().determine(state.getCurrentPlayer(), state);
			result.accept(state.getCurrentPlayer(), this);
		});
	}

	@Override
	public Object handleWin(ImmutablePlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object handleLoss(ImmutablePlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object handleTie() {
		// TODO Auto-generated method stub
		return null;
	}

}