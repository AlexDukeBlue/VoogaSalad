package controller;

import backend.cell.Cell;
import backend.cell.Terrain;
import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.player.Team;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.ReadonlyGameplayState;
import backend.util.io.XMLSerializer;
import frontend.util.Updatable;
import javafx.application.Platform;
import util.net.Modifier;
import util.net.ObservableClient;
import util.net.ObservableServer;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Created by ncp14
 *         This class is the communication controller which communicates between the frontend and backend.
 *         The primary purpose of my controller is to hide implementation of backend structure, specifically how
 *         our networking works and how the GameState is structured.
 */
public class CommunicationController implements Controller {
	private static final XMLSerializer<ReadonlyGameplayState> XML = new XMLSerializer<>();
	private AuthoringGameState mGameState;
	private Executor executor;
	private ObservableClient<ReadonlyGameplayState> mClient;
	private Collection<Updatable> thingsToUpdate;
	private final String playerName;
	private final CountDownLatch waitForReady;

	public CommunicationController(String username) {
		this(username, Collections.emptyList());
	}

	public CommunicationController(String username, Collection<Updatable> thingsToUpdate) {
		this.thingsToUpdate = new CopyOnWriteArrayList<>(thingsToUpdate);
		this.waitForReady = new CountDownLatch(1);
		this.playerName = username;
		this.executor = Executors.newCachedThreadPool();
	}

	public void startClient(String host, int port, Duration timeout) {
		try {
			mClient = new ObservableClient<>(host, port, XML, XML, timeout);
			mClient.addListener(this::updateGameState);
			executor.execute(mClient);
			String playerName = this.playerName;
			sendModifier((AuthoringGameState state) -> {
				state.addPlayer(new Player(playerName, "Test player", ""));
				return state;
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void startServer(ReadonlyGameplayState gameState, int port, Duration timeout) {
		try {
			ObservableServer<ReadonlyGameplayState> server = new ObservableServer<>(gameState, port, XML, XML, timeout);
			executor.execute(server);
			System.out.println("Server started successfully...");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public GameBoard getGrid() {
		return getGameState().getGrid();
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
		return getGameState();
	}

	@Override
	public AuthoringGameState getAuthoringGameState() {
		try {
			waitForReady.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return mGameState;
	}

	@Override
	public GameplayState getGameState() {
		return getAuthoringGameState();
	}

	@Override
	public ImmutablePlayer getPlayer(String name) {
		return getGameState().getPlayerByName(name);
	}

	public Map<CoordinateTuple, Cell> getModifiableCells() {
		return getGameState().getGrid().getCells();
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
		return getGameState().getGrid().getUnits();
	}

	@Override
	public Collection<? extends Unit> getUnitTemplates() {
		return (Collection<? extends Unit>) getAuthoringGameState().getTemplateByCategory("unit").getAll();
	}

	@Override
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
		return (Collection<? extends Terrain>) getAuthoringGameState().getTemplateByCategory("terrain").getAll();
	}

	@Override
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
	public Collection<? extends Team> getTeamTemplates() {
		return (Collection<? extends Team>) getAuthoringGameState().getTemplateByCategory("team").getAll();
	}

	@Override
	public void addTeamTemplates(Team... teamTemplates) {
		sendModifier((AuthoringGameState state) -> {
			state.getTemplateByCategory("team").addAll(teamTemplates);
			return state;
		});
	}

	@Override
	public void removeTeamTemplates(Team... teamTemplates) {
		sendModifier((AuthoringGameState state) -> {
			state.getTemplateByCategory("team").removeAll(teamTemplates);
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