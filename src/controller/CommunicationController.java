package controller;

import backend.cell.Terrain;
import backend.grid.GameBoard;
import backend.grid.ModifiableGameBoard;
import backend.player.ImmutablePlayer;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.ReadonlyGameplayState;
import backend.util.io.XMLSerializer;
import frontend.util.Updatable;
import util.net.Modifier;
import util.net.ObservableClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executors;

/**
 * @author Created by ncp14
 *         This class is the communication controller which communicates between the frontend and backend.
 *         The primary purpose of my controller is to hide implementation of backend structure, specifically how
 *         our networking works and how the GameState is structured.
 */
public class CommunicationController<T extends ReadonlyGameplayState> implements Controller<T> {
	private MyBuffer<AuthoringGameState> gameStateHistory;
	private T mGameState;
	private ObservableClient<T> mClient;
	private Collection<Updatable> thingsToUpdate;

	public CommunicationController(T gameState, Collection<Updatable> thingsToUpdate) {
		this.mGameState = gameState;
//		try {
//			mClient = new ObservableClient<T>("127.0.0.1", 10023, new XMLSerializer<T>(), new XMLSerializer<T>(), Duration.ofSeconds(60));
//			mClient.addListener(e -> updateGameState(e));
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//		Executors.newSingleThreadExecutor().submit(mClient);
//		mClient.addToOutbox(gameState);
		this.thingsToUpdate = new ArrayList<Updatable>();
		if (thingsToUpdate != null) {
			this.thingsToUpdate.addAll(thingsToUpdate);
		}

	}

	@Override
	public GameBoard getGrid() {
		return mGameState.getGrid();
	}

	public void updateGameState(T newGameState) {
		mGameState = newGameState;
		updateAll();
	}

	public void setClient(ObservableClient<T> client) {
		this.mClient = client;
		updateAll();
	}

	public ObservableClient<? extends ReadonlyGameplayState> getClient() {
		return mClient;
	}

	public void setGameState(T gameState) {
		this.mGameState = gameState;
		updateAll();
	}

	@Override
	public ReadonlyGameplayState getReadOnlyGameState() {
		return mGameState;
	}

	public ReadonlyGameplayState getMostRecentGameState() {
		return gameStateHistory.getBufferHead();
	}

	@Override
	public AuthoringGameState getAuthoringGameState() {
		return (AuthoringGameState) mGameState;
	}

	@Override
	public T getGameState() {
		return mGameState;
	}

	@Override
	public ImmutablePlayer getPlayer(String name) {
		return mGameState.getPlayerByName(name);
	}

	@Override
	public ModifiableGameBoard getModifiableCells() {
		return (ModifiableGameBoard) mGameState.getGrid();
	}

	@Override
	public void sendModifier(Modifier<T> modifier) {
//		mClient.addToOutbox(modifier);
		mGameState = modifier.modify(mGameState);
		updateAll();
	}

	@Override
	public Collection<? extends Unit> getUnits() {
		return mGameState.getGrid().getUnits();
	}

	@Override
	public Collection<? extends Terrain> getTerrains() {
		//Todo
		return null;
	}

	@Override
	public Collection<? extends Unit> getUnitTemplates() {
		return ModifiableUnit.getPredefinedUnits();
	}

	@Override
	public Collection<? extends Terrain> getTerrainTemplates() {
		return Terrain.getPredefinedTerrain();
	}

	public void addToUpdated(Updatable updatable) {
		if (!thingsToUpdate.contains(updatable)) {
			thingsToUpdate.add(updatable);
		}
	}

	public void removeFromUpdated(Updatable updatable) {
		if (thingsToUpdate.contains(updatable)) {
			thingsToUpdate.remove(updatable);
		}
	}

	private void updateAll() {
		thingsToUpdate.stream().forEach(updatable -> updatable.update());
	}

}