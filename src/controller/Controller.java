package controller;

import backend.cell.Cell;
import backend.cell.Terrain;
import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.player.ImmutablePlayer;
import backend.player.Team;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.ReadonlyGameplayState;
import frontend.util.Updatable;
import util.io.Serializer;
import util.io.Unserializer;
import util.net.Modifier;

import java.time.Duration;
import java.util.Collection;

/**
 * @author Created by ncp14 on 4/3/2017.
 */
public interface Controller {

	GameBoard getGrid();

	void startClient(String host, int port, Serializer<ReadonlyGameplayState> serializer, Unserializer<ReadonlyGameplayState> unserializer, Duration timeout);

	void startServer(ReadonlyGameplayState gameState, int port, Serializer<ReadonlyGameplayState> serializer, Unserializer<ReadonlyGameplayState> unserializer, Duration timeout);

	Cell getCell(CoordinateTuple tuple);

	ReadonlyGameplayState getReadOnlyGameState();

	AuthoringGameState getAuthoringGameState();

	GameplayState getGameState();

	ImmutablePlayer getPlayer(String name);

	<U extends ReadonlyGameplayState> void setGameState(U newGameState);

	GameBoard getModifiableCells();

	<U extends ReadonlyGameplayState> void sendModifier(Modifier<U> modifier);

	Collection<? extends Unit> getUnits();

	Collection<? extends Unit> getUnitTemplates();

	void addUnitTemplates(Unit... unitTemplates);

	void removeUnitTemplates(Unit... unitTemplates);

	Collection<? extends Terrain> getTerrainTemplates();

	void addTerrainTemplates(Terrain... terrainTemplates);

	void removeTerrainTemplates(Terrain... terrainTemplates);

	void addToUpdated(Updatable objectToUpdate);

	void removeFromUpdated(Updatable objectToUpdate);

	String getPlayerName();

	Collection<? extends Team> getTeamTemplates();

	void addTeamTemplates(Team[] teamTemplates);

	void removeTeamTemplates(Team[] teamTemplates);
}
