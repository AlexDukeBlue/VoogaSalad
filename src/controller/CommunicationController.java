package controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

import backend.cell.ModifiableTerrain;
import backend.cell.Terrain;
import backend.grid.GameBoard;
import backend.grid.ModifiableGameBoard;
import backend.player.ImmutablePlayer;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.io.XMLSerializer;
import frontend.View;
import frontend.util.Updatable;
import util.net.Modifier;
import util.net.ObservableClient;

/**
 * @author Created by ncp14
 *         This class is the communication controller which communicates between the frontend and backend.
 *         The primary purpose of my controller is to hide implementation of backend structure, specifically how
 *         our networking works and how the GameState is structured.
 */
public class CommunicationController implements Controller {
	private MyBuffer<AuthoringGameState> gameStateHistory;
	private AuthoringGameState mGameState;
	private ObservableClient<AuthoringGameState> mClient;
	private Collection<Updatable> thingsToUpdate;

	public CommunicationController(AuthoringGameState gameState, Collection<Updatable> thingsToUpdate) {
		this.thingsToUpdate = new ArrayList<Updatable>(thingsToUpdate);
		this.mGameState = gameState;
		try{
			mClient = new ObservableClient<AuthoringGameState>("127.0.0.1", 10023, new XMLSerializer<AuthoringGameState>(), new XMLSerializer<AuthoringGameState>(), Duration.ofSeconds(60));
			mClient.addListener(e -> updateGameState(e));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public GameBoard getGrid() {
		return mGameState.getGrid();
	}
	
	public void updateGameState(AuthoringGameState newGameState)
	{
		mGameState = newGameState;
		updateAll();
	}

	public void setClient(ObservableClient<AuthoringGameState> client) {
		this.mClient = client;
		updateAll();
	}

	public ObservableClient<AuthoringGameState> getClient() {
		return mClient;
	}

	public void setGameState(AuthoringGameState gameState) {
		gameStateHistory.addToBuffer(gameState);
		this.mGameState = gameState;
		updateAll();
	}

	public AuthoringGameState getGameState() {
		return mGameState;
	}

	public AuthoringGameState getMostRecentGameState() {
		return gameStateHistory.getBufferHead();
	}

	@Override
	public AuthoringGameState getAuthoringGameState() {
		return mGameState;
	}

	@Override
	public GameplayState getGameplayState() {
		return mGameState;
	}

	@Override
	public ImmutablePlayer getPlayer(String name) {
		return mGameState.getPlayerByName(name);
	}

	@Override
	public ModifiableGameBoard getModifiableCells() {
		return mGameState.getGrid();
	}

	@Override
	public void sendModifier(Modifier<AuthoringGameState> modifier) {
		mClient.addToOutbox(modifier);	
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
		return ModifiableTerrain.getPredefinedTerrain();
	}
	
	public void addToUpdated(Updatable updatable){
		if (!thingsToUpdate.contains(updatable)){
			thingsToUpdate.add(updatable);
		}
	}
	
	public void removeFromUpdated(Updatable updatable){
		if (thingsToUpdate.contains(updatable)){
			thingsToUpdate.remove(updatable);
		}
	}
	
	private void updateAll(){
		thingsToUpdate.stream().forEach(updatable -> updatable.update());
	}

}