package controller;

import backend.cell.Cell;
import backend.cell.Terrain;
import backend.game_engine.ResultQuadPredicate;
import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.Shape;
import backend.player.ChatMessage;
import backend.player.ImmutablePlayer;
import backend.player.Team;
import backend.unit.Unit;
import backend.util.Actionable.SerializableBiConsumer;
import backend.util.*;
import backend.util.Requirement.SerializableBiPredicate;
import frontend.util.UIComponentListener;
import util.net.Modifier;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;

/**
 * @author Created by ncp14 on 4/3/2017.
 */
public interface Controller {

	ReadonlyGameplayState loadFile(Path path) throws IOException;

	void saveFile(Path path) throws IOException;

	GameBoard getGrid();

	void setGrid(GameBoard grid);

	void startClient(String host, int port, Duration timeout);

	void startServer(ReadonlyGameplayState gameState, int port, Duration timeout);

	Cell getCell(CoordinateTuple tuple);

	ReadonlyGameplayState getReadOnlyGameState();

	AuthoringGameState getAuthoringGameState();

	GameplayState getGameState();

	ImmutablePlayer getPlayer(String name);

	<U extends ReadonlyGameplayState> void setGameState(U newGameState);

	<U extends ReadonlyGameplayState> void sendModifier(Modifier<U> modifier);

	Collection<? extends Unit> getUnits();

	Collection<? extends VoogaEntity> getTemplatesByCategory(String category);

	default Collection<? extends Unit> getUnitTemplates() {
		return (Collection<? extends Unit>) getTemplatesByCategory("unit");
	}

	void addTemplatesByCategory(String category, VoogaEntity... templates);

	void removeTemplatesByCategory(String category, String... templateNames);

	default void removeUnitTemplates(String... unitTemplates) {
		removeTemplatesByCategory("unit", unitTemplates);
	}

	default Collection<? extends Terrain> getTerrainTemplates() {
		return (Collection<? extends Terrain>) getTemplatesByCategory("terrain");
	}

	default void addTerrainTemplates(Terrain... terrainTemplates) {
		addTemplatesByCategory("terrain", terrainTemplates);
	}

	default void removeTerrainTemplates(String... terrainTemplates) {
		removeTemplatesByCategory("terrain", terrainTemplates);
	}

	void addListener(UIComponentListener objectToUpdate);

	void removeListener(UIComponentListener objectToUpdate);

	void enterAuthoringMode();

	void enterGamePlayMode();

	boolean isAuthoringMode();

	String getPlayerName();
	
	void addTurnRequirement(String name, String description, String imgPath, SerializableBiPredicate biPredicate);
	
	void removeTurnRequirement(String name);
	
	void activateTurnRequirement(String name);
	
	void deactivateTurnRequirement(String name);
	
	void addTurnAction(Event event, String name, String description, String imgPath, SerializableBiConsumer biConsumer);
	
	void removeTurnAction(Event event, String name);
	
	void activateTurnAction(Event event, String name);
	
	void deactivateTurnAction(Event event, String name);
	
	void addEndCondition(String name, String description, String imgPath, ResultQuadPredicate resultQuadPredicate);
	
	void removeEndCondition(String name);
	
	void activateEndCondition(String name);
	
	void deactivateEndCondition(String name);

	default Collection<? extends Team> getTeamTemplates() {
		return (Collection<? extends Team>) getTemplatesByCategory("team");
	}

	default void addTeamTemplates(Team... teamTemplates) {
		addTemplatesByCategory("team", teamTemplates);
	}

	default void removeTeamTemplates(String... teamTemplates) {
		removeTemplatesByCategory("team", teamTemplates);
	}

	void addPlayer(String playerName);

	void undo();

	Shape getShape();

	void endTurn();

	void moveUnit(String unitName, CoordinateTuple unitLocation, CoordinateTuple targetLocation);

	void sendMessage(String messageContent, ChatMessage.AccessLevel accessLevel, String receiverName);

	void setBoundsHandler(String boundsHandlerName);

	void copyTemplateToGrid(String templateName, CoordinateTuple gridLocation, String targetUnitName);

	void removeUnitFromGrid(String unitName, CoordinateTuple unitLocation);

	void useUnitActiveAbility(String abilityName, String userName, CoordinateTuple userLocation, String targetName, CoordinateTuple targetLocation);
}
