package backend.util;

import backend.cell.CellTemplate;
import backend.cell.ModifiableTerrain;
import backend.game_engine.ResultQuadPredicate;
import backend.grid.ModifiableGameBoard;
import backend.player.Player;
import backend.player.Team;
import backend.unit.UnitTemplate;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * @author Created by th174 on 3/30/2017. Worked on by Alex
 */

//TODO: Implement getTurnNumber(), toXML() (Kinda Tavo's job), messagePlayer(Player from, Player to, String message);
public class GameState implements MutableGameState {
	private List<Player> playerList;
	private Player currentPlayer;
	private Collection<Team> teams;
	private ModifiableGameBoard gameGrid;
	private Collection<UnitTemplate> unitTemplates;
	private Collection<UnitTemplate> activeAbilities;
	private Collection<ModifiableTerrain> terrains;
	private Collection<CellTemplate> cellTemplates;

	private Collection<ResultQuadPredicate> currentObjectives;
	private Map<Event, List<BiConsumer<Player, ImmutableGameState>>> turnActions;
	private Collection<BiPredicate<Player, ImmutableGameState>> turnRequirements;

	public GameState() {
		this(null);
	}

	public GameState(ModifiableGameBoard grid) {
		gameGrid = grid;

		playerList = new ArrayList<>();
		teams = new ArrayList<>();

		unitTemplates = new ArrayList<>();
		activeAbilities = new ArrayList<>();
		terrains = new ArrayList<>();
		cellTemplates = new ArrayList<>();

		currentObjectives = new ArrayList<>();
		turnActions = new HashMap<>();
		turnRequirements = new ArrayList<>();
	}

	public void endTurn(Player player) {
		if (playerList.indexOf(player) == playerList.size()) {
			setCurrentPlayer(playerList.get(0));
			return;
		}
		setCurrentPlayer(playerList.get(playerList.indexOf(player) + 1));
	}

	public Collection<ModifiableTerrain> getTerrains() {
		return terrains;
	}

	public Collection<UnitTemplate> getUnitTemplates() {
		return unitTemplates;
	}

	public Collection<UnitTemplate> getActiveAbilities() {
		return activeAbilities;
	}

	public Collection<CellTemplate> getCellTemplates() {
		return cellTemplates;
	}

	@Override
	public ModifiableGameBoard getGrid() {
		return gameGrid;
	}

	@Override
	public void endTurn() {
		if (playerList.indexOf(getCurrentPlayer()) == playerList.size() || playerList.size() == 0) {
			setCurrentPlayer(playerList.get(0));
			return;
		}
		setCurrentPlayer(playerList.get(playerList.indexOf(getCurrentPlayer()) + 1));
	}

	@Override
	public Collection<Team> getTeams() {
		return teams;
	}

	@Override
	public List<Player> getPlayers() {
		return playerList;
	}

	@Override
	public void messagePlayer(Player from, Player to, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getTurnNumber() {
		return 0;
	}

	@Override
	public void addEventHandler(BiConsumer<Player, ImmutableGameState> eventListener, Event event) {
		turnActions.merge(event, new ArrayList<>(Collections.singletonList(eventListener)), (list, t) -> {
			list.addAll(t);
			return list;
		});
	}

	@Override
	public Collection<ResultQuadPredicate> getObjectives() {
		return currentObjectives;
	}

	@Override
	public void addObjective(ResultQuadPredicate winCondition) {
		currentObjectives.add(winCondition);
	}

	@Override
	public Map<Event, List<BiConsumer<Player, ImmutableGameState>>> getTurnEvents() {
		return turnActions;
	}

	@Override
	public void addTurnRequirement(BiPredicate<Player, ImmutableGameState> requirement) {
		turnRequirements.add(requirement);
	}

	@Override
	public Collection<BiPredicate<Player, ImmutableGameState>> getTurnRequirements() {
		return turnRequirements;
	}

	@Override
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	private void setCurrentPlayer(Player player) {
		currentPlayer = player;
	}

	public void setGrid(ModifiableGameBoard grid) {
		gameGrid = grid;
	}

}
