package controller;

import backend.cell.Cell;
import backend.cell.Terrain;
import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.ReadonlyGameplayState;
import backend.util.io.XMLSerializer;
import frontend.util.Updatable;
import javafx.application.Platform;
import util.net.Modifier;
import util.net.ObservableClient;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author Created by ncp14
 *         This class is the communication controller which communicates between the frontend and backend.
 *         The primary purpose of my controller is to hide implementation of backend structure, specifically how
 *         our networking works and how the GameState is structured.
 */
public class CommunicationController implements Controller {
	private AuthoringGameState mGameState;
	private ObservableClient<ReadonlyGameplayState> mClient;
	private Collection<Updatable> thingsToUpdate;
	private final String playerName;
	private final CountDownLatch waitForReady;

	public CommunicationController(String username, ReadonlyGameplayState gameState, Collection<Updatable> thingsToUpdate) {
		this.thingsToUpdate = new CopyOnWriteArrayList<>();
		this.waitForReady = new CountDownLatch(1);
		if (thingsToUpdate != null) {
			this.thingsToUpdate.addAll(thingsToUpdate);
		}
		this.playerName = username;
		try {
			mClient = new ObservableClient<>("127.0.0.1", 10023, new XMLSerializer<>(), new XMLSerializer<>(), Duration.ofSeconds(60));
			mClient.addListener(this::updateGameState);
			setGameState(gameState);
			Executors.newSingleThreadExecutor().submit(mClient);
			//login with username
			sendModifier((AuthoringGameState state) -> {
				state.addPlayer(new Player(username, "Test player", ""));
				return state;
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public GameBoard getGrid() {
		return mGameState.getGrid();
	}

	@Override
	public Cell getCell(CoordinateTuple tuple) {
		return getGrid().get(tuple);
	}

	private synchronized <U extends ReadonlyGameplayState> void updateGameState(U newGameState) {
		mGameState = (AuthoringGameState) newGameState;
		updateAll();
		waitForReady.countDown();
	}

	public void setClient(ObservableClient<ReadonlyGameplayState> client) {
		this.mClient = client;
	}

	public ObservableClient<ReadonlyGameplayState> getClient() {
		return mClient;
	}

	@Override
	public <U extends ReadonlyGameplayState> void setGameState(U gameState) {
//		this.mGameState = gameState;
		getClient().addToOutbox(gameState);
	}

	@Override
	public ReadonlyGameplayState getReadOnlyGameState() {
		return mGameState;
	}

	@Override
	public AuthoringGameState getAuthoringGameState() {
		return mGameState;
	}

	@Override
	public GameplayState getGameState() {
		return mGameState;
	}

	@Override
	public ImmutablePlayer getPlayer(String name) {
		return mGameState.getPlayerByName(name);
	}

	@Override
	public GameBoard getModifiableCells() {
		return mGameState.getGrid();
	}

	@Override
	public <U extends ReadonlyGameplayState> void sendModifier(Modifier<U> modifier) {
		try {
			waitForReady.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mClient.addToOutbox((Modifier<ReadonlyGameplayState>) modifier);
		//lol this is so unsafe
//		mGameState = modifier.modify((U) mGameState);
	}

	@Override
	public Collection<? extends Unit> getUnits() {
		return mGameState.getGrid().getUnits();
	}

	@Override
	public Collection<? extends Unit> getUnitTemplates() {
		return (Collection<? extends Unit>) mGameState.getTemplateByCategory("unit").getAll();
	}

	public void addUnitTemplates(Unit... unitTemplates) {
		sendModifier((AuthoringGameState state) -> {
			state.getTemplateByCategory("unit").addAll(unitTemplates);
			return state;
		});
	}

	@Override
	public void removeUnitTemplates(Unit... unitTemplates) {
		sendModifier((AuthoringGameState state) -> {
			state.getTemplateByCategory("unit").removeAll(unitTemplates);
			return state;
		});
	}

	@Override
	public Collection<? extends Terrain> getTerrainTemplates() {
		return (Collection<? extends Terrain>) mGameState.getTemplateByCategory("terrain").getAll();
	}

	public void addTerrainTemplates(Terrain... terrainTemplates) {
		sendModifier((AuthoringGameState state) -> {
			state.getTemplateByCategory("terrain").addAll(terrainTemplates);
			return state;
		});
	}

	@Override
	public void removeTerrainTemplates(Terrain... terrainTemplates) {
		sendModifier((AuthoringGameState state) -> {
			state.getTemplateByCategory("terrain").removeAll(terrainTemplates);
			return state;
		});
	}

	@Override
	public void addToUpdated(Updatable updatable) {
		if (!thingsToUpdate.contains(updatable)) {
			thingsToUpdate.add(updatable);
		}
	}

	@Override
	public void removeFromUpdated(Updatable updatable) {
		if (thingsToUpdate.contains(updatable)) {
			thingsToUpdate.remove(updatable);
		}
	}

	@Override
	public String getPlayerName() {
		return playerName;
	}

	private void updateAll() {
		thingsToUpdate.forEach(e -> Platform.runLater(e::update));
	}
}