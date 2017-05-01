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
import java.io.Serializable;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;

/**
 * @author Created by ncp14 on 4/3/2017.
 */
public interface Controller {

	void save(Serializable obj, Path path) throws IOException;

	<T extends Serializable> T load(Path path) throws IOException;

	void saveState(Path path) throws IOException;

	GameBoard getGrid();

	void setGrid(GameBoard grid);

	default String getActiveTeamName(){
		return getAuthoringGameState().getActiveTeam().getName();
	}

	String getMyPlayerName();

	default boolean isMyTeam() {
		return !getMyPlayer().getTeam().isPresent() || getMyPlayer().getTeam().get().equals(getActiveTeam());
	}

	default ImmutablePlayer getMyPlayer() {
		return getPlayer(getMyPlayerName());
	}

	default Team getActiveTeam(){
		return getAuthoringGameState().getActiveTeam();
	}

	default Team getMyTeam(){
		return getMyPlayer().getTeam().orElse(null);
	}

	void startClient(String host, int port, Duration timeout);

	void startServer(ReadonlyGameplayState gameState, int port, Duration timeout);

	Cell getCell(CoordinateTuple tuple);

	ReadonlyGameplayState getReadOnlyGameState();

	AuthoringGameState getAuthoringGameState();

	GameplayState getGameplayState();

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

	/**
	 * Informs the Model that the program has entered Authoring mode.
	 */
	void enterAuthoringMode();

	/**
	 * Informs the Model that the program has entered Gameplay mode.
	 */
	void enterGamePlayMode();

	/**
	 * Returns a boolean indicating whether or not the Model is currently in
	 * Authoring mode.
	 * 
	 * @return true if the Model is in Authoring mode, false if not
	 */
	boolean isAuthoringMode();

	/**
	 * Returns a boolean indicating whether or not the Controller's active
	 * Player has won.
	 * 
	 * @return true if the Player has won, false if not
	 */
	boolean activeTeamWon();

	/**
	 * Returns a boolean indicating whether or not the Controller's active
	 * Player has lost.
	 * 
	 * @return true if the Player has lost, false if not
	 */
	boolean activeTeamLost();

	/**
	 * Returns a boolean indicating whether or not the Controller's active
	 * Player has tied.
	 * 
	 * @return true if the Player has tied, false if not
	 */
	boolean activeTeamTied();

	/**
	 * Adds a turn requirement to the Model's list of turn requirements.
	 * 
	 * @param name
	 *            String holding the name of the turn requirement. Used for
	 *            identification.
	 * @param description
	 *            String describing the turn requirement.
	 * @param imgPath
	 *            String used to access image which represents the turn
	 *            requirement.
	 * @param biPredicate
	 *            SerializableBiPredicate which is used to determine if the turn
	 *            requirement has been satisfied.
	 */
	void addTurnRequirement(String name, String description, String imgPath, SerializableBiPredicate biPredicate);

	/**
	 * Remove a turn requirement from the Model's list of turn requirements.
	 * 
	 * @param name
	 *            String holding the name of the turn requirement. Used to
	 *            identify and remove the correct turn requirement.
	 */
	void removeTurnRequirement(String name);

	/**
	 * Activates a turn requirement held in the Model's list of turn
	 * requirements. Does nothing if the turn requirement does not exist.
	 * 
	 * @param name
	 *            String holding the name of the turn requirement. Used to
	 *            identify and activate the correct turn requirement.
	 */
	void activateTurnRequirement(String name);

	/**
	 * Deactivates a turn requirement held in the Model's list of turn
	 * requirements. Does nothing if the turn requirement does not exist.
	 * 
	 * @param name
	 *            String holding the name of the turn requirement. Used to
	 *            identify and deactivate the correct turn requirement.
	 */
	void deactivateTurnRequirement(String name);

	/**
	 * Adds a turn action to the Model's list of turn actions.
	 * 
	 * @param name
	 *            String holding the name of the turn action. Used for
	 *            identification.
	 * @param description
	 *            String describing the turn action.
	 * @param imgPath
	 *            String used to access image which represents the turn action.
	 */
	void addTurnAction(String name, String description, String imgPath, SerializableBiConsumer biConsumer);

	/**
	 * Remove a turn action from the Model's list of turn actions.
	 * 
	 * @param name
	 *            String holding the name of the turn action. Used to identify
	 *            and remove the correct turn action.
	 */
	void removeTurnAction(String name);

	/**
	 * Activates a turn action held in the Model's list of turn actions. Does
	 * nothing if the turn action does not exist.
	 * 
	 * @param name
	 *            String holding the name of the turn action. Used to identify
	 *            and activate the correct turn action.
	 */
	void activateTurnAction(String name);

	/**
	 * Deactivates a turn action held in the Model's list of turn actions. Does
	 * nothing if the turn action does not exist.
	 * 
	 * @param name
	 *            String holding the name of the turn action. Used to identify
	 *            and deactivate the correct turn action.
	 */
	void deactivateTurnAction(String name);

	/**
	 * Adds an end condition to the Model's list of end conditions.
	 * 
	 * @param name
	 *            String holding the name of the end condition. Used for
	 *            identification.
	 * @param description
	 *            String describing the end condition.
	 * @param imgPath
	 *            String used to access image which represents the end
	 *            condition.
	 */
	void addEndCondition(String name, String description, String imgPath, ResultQuadPredicate resultQuadPredicate);

	/**
	 * Remove an end condition from the Model's list of end condition.
	 * 
	 * @param name
	 *            String holding the name of the end condition. Used to identify
	 *            and remove the correct end condition.
	 */
	void removeEndCondition(String name);

	/**
	 * Activates an end condition held in the Model's list of end conditions.
	 * Does nothing if the end condition does not exist.
	 * 
	 * @param name
	 *            String holding the name of the end condition. Used to identify
	 *            and activate the correct end condition.
	 */
	void activateEndCondition(String name);

	/**
	 * Deactivates an end condition held in the Model's list of end conditions.
	 * Does nothing if the end condition does not exist.
	 * 
	 * @param name
	 *            String holding the name of the end condition. Used to identify
	 *            and deactivate the correct end condition.
	 */
	void deactivateEndCondition(String name);

	default Collection<? extends Team> getTeamTemplates() {
		return (Collection<? extends Team>) getTemplatesByCategory("team");
	}

	default void addTeams(Team... teamTemplates) {
		addTemplatesByCategory("team", teamTemplates);
	}

	default void removeTeams(String... teamTemplates) {
		removeTemplatesByCategory("team", teamTemplates);
	}

	void setPlayer(String name, String description, String imgPath);

	void undo();

	Shape getShape();

	void endTurn();

	void moveUnit(String unitName, CoordinateTuple unitLocation, CoordinateTuple targetLocation);

	void sendMessage(String messageContent, ChatMessage.AccessLevel accessLevel, String receiverName);

	void setBoundsHandler(String boundsHandlerName);

	void copyTemplateToGrid(VoogaEntity template, HasLocation destination);

	void removeUnitFromGrid(String unitName, CoordinateTuple unitLocation);

	void useActiveAbility(String abilityName, String userName, CoordinateTuple userLocation, String targetName,
	                      CoordinateTuple targetLocation);

	void updateAll();

	void joinTeam(String teamName);

	void sendFile(String path) throws IOException;
}