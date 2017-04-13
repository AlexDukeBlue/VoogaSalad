package controller;

import java.util.Collection;

import backend.cell.Terrain;
import backend.grid.GameBoard;
import backend.grid.ModifiableGameBoard;
import backend.player.ImmutablePlayer;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import frontend.util.Updatable;
import util.net.Modifier;

/**
 * @author Created by ncp14 on 4/3/2017.
 */
public interface Controller {

	GameBoard getGrid();
	
	AuthoringGameState getGameState();
	
	AuthoringGameState getAuthoringGameState();

	GameplayState getGameplayState();

	ImmutablePlayer getPlayer(String name);

	void setGameState(AuthoringGameState newGameState);
	
	ModifiableGameBoard getModifiableCells();
	
	void sendModifier(Modifier<AuthoringGameState> modifier);

	Collection<? extends Unit> getUnits();

	Collection<? extends Terrain> getTerrains();
	
	Collection<? extends Unit> getUnitTemplates();

	Collection<? extends Terrain> getTerrainTemplates();
	
	void addToUpdated(Updatable objectToUpdate);
	
	void removeFromUpdated(Updatable objectToUpdate);
	

	
	
	
	
	
	
	
}
