package controller;

import backend.cell.Cell;
import backend.cell.Terrain;
import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.ModifiableGameBoard;
import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.unit.ModifiableUnit;
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
import java.util.concurrent.Executors;

/**
 * @author Created by ncp14
 *         This class is the communication controller which communicates between the frontend and backend.
 *         The primary purpose of my controller is to hide implementation of backend structure, specifically how
 *         our networking works and how the GameState is structured.
 */
public class CommunicationController implements Controller {
	private ReadonlyGameplayState mGameState;
	private ObservableClient<ReadonlyGameplayState> mClient;
	private Collection<Updatable> thingsToUpdate;
	private String playerName;

	public CommunicationController(String name, ReadonlyGameplayState gameState, Collection<Updatable> thingsToUpdate) {
		this.mGameState = gameState;
		this.thingsToUpdate = new CopyOnWriteArrayList<>();
		if (thingsToUpdate != null) {
			this.thingsToUpdate.addAll(thingsToUpdate);
		}
		this.playerName = name;
		try {
			mClient = new ObservableClient<>("127.0.0.1", 10023, new XMLSerializer<>(), new XMLSerializer<>(), Duration.ofSeconds(60));
			mClient.addListener(this::updateGameState);
			setGameState(gameState);
			sendModifier((AuthoringGameState state) -> {
				state.addPlayer(new Player(name, "Test player", ""));
				return state;
			});
			Executors.newSingleThreadExecutor().submit(mClient);
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

	private <U extends ReadonlyGameplayState> void updateGameState(U newGameState) {
		mGameState = newGameState;
		updateAll();
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
		return (AuthoringGameState) mGameState;
	}

	@Override
	public GameplayState getGameState() {
		return (GameplayState) mGameState;
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
	public <U extends ReadonlyGameplayState> void sendModifier(Modifier<U> modifier) {
		mClient.addToOutbox((Modifier<ReadonlyGameplayState>) modifier);
		//lol this is so unsafe
//		mGameState = modifier.modify((U) mGameState);
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
	
	public void addUnitTemplates(ModifiableUnit newUnit) {
		Collection<ModifiableUnit> currentUnits =  ModifiableUnit.getPredefinedUnits();
		currentUnits.add(newUnit);
		
	}

	@Override
	public Collection<? extends Terrain> getTerrainTemplates() {
		return Terrain.getPredefinedTerrain();
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