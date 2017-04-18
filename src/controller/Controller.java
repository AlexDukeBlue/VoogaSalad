package controller;

import backend.cell.Terrain;
import backend.grid.GameBoard;
import backend.grid.ModifiableGameBoard;
import backend.player.ImmutablePlayer;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.ReadonlyGameplayState;
import frontend.util.Updatable;
import util.net.Modifier;

import java.util.Collection;

/**
 * @author Created by ncp14 on 4/3/2017.
 */
public interface Controller {

	GameBoard getGrid();

	ReadonlyGameplayState getReadOnlyGameState();

	AuthoringGameState getAuthoringGameState();

	GameplayState getGameState();

	ImmutablePlayer getPlayer(String name);

	<U extends ReadonlyGameplayState> void setGameState(U newGameState);

	ModifiableGameBoard getModifiableCells();

	<U extends ReadonlyGameplayState> void sendModifier(Modifier<U> modifier);

	Collection<? extends Unit> getUnits();

	Collection<? extends Terrain> getTerrains();

	Collection<? extends Unit> getUnitTemplates();

	Collection<? extends Terrain> getTerrainTemplates();

	void addToUpdated(Updatable objectToUpdate);

	void removeFromUpdated(Updatable objectToUpdate);

}
