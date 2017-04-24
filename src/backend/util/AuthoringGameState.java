package backend.util;

import backend.cell.Terrain;
import backend.game_engine.ResultQuadPredicate;
import backend.game_engine.Resultant;
import backend.grid.BoundsHandler;
import backend.grid.GameBoard;
import backend.grid.GridPattern;
import backend.grid.ModifiableGameBoard;
import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.player.Team;
import backend.unit.ModifiableUnit;
import backend.unit.properties.ActiveAbility;
import backend.unit.properties.InteractionModifier;
import backend.unit.properties.ModifiableUnitStat;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class AuthoringGameState extends GameplayState implements VoogaEntity, ReadonlyGameplayState {
	public transient static final String BOUNDS_HANDLER = "boundshandler", TERRAIN = "terrain", OFFENSIVE_MODIFIER = "offensivemodifier", DEFENSIVE_MODIFIER = "defensivemodifier", CELL_TRIGGERED_EFFECT = "celltriggeredeffect", UNIT_TRIGGERED_EFFECT = "unittriggeredeffect", ACTIVE_ABILITY = "activeability", UNIT = "unit", UNIT_STAT = "unitstat", GRID_PATTERN = "gridpattern", GAMEBOARD = "gameboard";


	private Map<String, ModifiableVoogaCollection<VoogaEntity, ModifiableVoogaCollection>> templates;

	public AuthoringGameState(String name) {
		super(name, null, "", "");
		this.setAuthoringMode(true);
		templates = new HashMap<>();
		templates.put(GAMEBOARD, new ModifiableVoogaCollection<>("GameBoards", "", "", ModifiableGameBoard.getPredefinedGameBoards()));
		templates.put(TERRAIN, new ModifiableVoogaCollection<>("Terrain", "", "", Terrain.getPredefinedTerrain()));
		templates.put(UNIT, new ModifiableVoogaCollection<>("Units", "", "", ModifiableUnit.getPredefinedUnits()));
		templates.put(UNIT_TRIGGERED_EFFECT, new ModifiableVoogaCollection<>("Unit Passive/Triggered Abilities", "", "", ModifiableTriggeredEffect.getPredefinedTriggeredUnitAbilities()));
		templates.put(CELL_TRIGGERED_EFFECT, new ModifiableVoogaCollection<>("Cell Passive/Triggered Abilities", "", "", ModifiableTriggeredEffect.getPredefinedTriggeredCellAbilities()));
		templates.put(UNIT_STAT, new ModifiableVoogaCollection<>("Unit Stats", "", "", ModifiableUnitStat.getPredefinedUnitStats()));
		templates.put(GRID_PATTERN, new ModifiableVoogaCollection<>("Grid Patterns", "", "", GridPattern.getPredefinedGridPatterns()));
		templates.put(BOUNDS_HANDLER, new ModifiableVoogaCollection<>("Bounds Handlers", "", "", BoundsHandler.getPredefinedBoundsHandlers()));
		templates.put(ACTIVE_ABILITY, new ModifiableVoogaCollection<>("Active Abilities", "", "", ActiveAbility.getPredefinedActiveAbilities()));
		templates.put(OFFENSIVE_MODIFIER, new ModifiableVoogaCollection<>("Offensive Modifiers", "", "", InteractionModifier.getPredefinedOffensiveModifiers()));
		templates.put(DEFENSIVE_MODIFIER, new ModifiableVoogaCollection<>("Defensive Modifiers", "", "", InteractionModifier.getPredefinedDefensiveModifiers()));
	}

	@Override
	public Player getPlayerByName(String name) {
		return (Player) super.getPlayerByName(name);
	}

	@Override
	public Player getCurrentPlayer() {
		return (Player) super.getCurrentPlayer();
	}

	@Override
	public List<Player> getAllPlayers() {
		return (List<Player>) super.getAllPlayers();
	}

	@Override
	public AuthoringGameState addPlayer(ImmutablePlayer newPlayer) {
		return (AuthoringGameState) super.addPlayer(newPlayer);
	}

	@Override
	public AuthoringGameState addPlayer(ImmutablePlayer newPlayer, Team team) {
		return (AuthoringGameState) super.addPlayer(newPlayer, team);
	}

	@Override
	public AuthoringGameState setDescription(String description) {
		return (AuthoringGameState) super.setDescription(description);
	}

	@Override
	public AuthoringGameState setImgPath(String imgPath) {
		return (AuthoringGameState) super.setImgPath(imgPath);
	}

	@Override
	public AuthoringGameState setName(String name) {
		return (AuthoringGameState) super.setName(name);
	}

	@Override
	public ModifiableGameBoard getGrid() {
		return (ModifiableGameBoard) super.getGrid();
	}

	@Override
	public AuthoringGameState setGrid(GameBoard grid) {
		return (AuthoringGameState) super.setGrid(grid);
	}

	@Override
	public AuthoringGameState addObjectives(Resultant... objectives) {
		return (AuthoringGameState) super.addObjectives(objectives);
	}

	@Override
	public AuthoringGameState addObjectives(Collection<Resultant> objectives) {
		return (AuthoringGameState) super.addObjectives(objectives);
	}

	@Override
	public AuthoringGameState addTeam(Team team) {
		return (AuthoringGameState) super.addTeam(team);
	}

	public void setTeams(Collection<Team> teams) {
		getTeams().stream().forEach(team -> removeTeamByName(team.getName()));
		teams.forEach(team -> addTeam(team));
	}

	@Override
	public AuthoringGameState addTurnActions(Event event, Collection<Actionable> actions) {
		return (AuthoringGameState) super.addTurnActions(event, actions);
	}

	@Override
	public AuthoringGameState addTurnActions(Event event, Actionable... actions) {
		return (AuthoringGameState) super.addTurnActions(event, actions);
	}

	@Override
	public AuthoringGameState addTurnRequirements(Collection<Requirement> turnRequirements) {
		return (AuthoringGameState) super.addTurnRequirements(turnRequirements);
	}

	@Override
	public AuthoringGameState addTurnRequirements(Requirement... turnRequirements) {
		return (AuthoringGameState) super.addTurnRequirements(turnRequirements);
	}

	@Override
	public AuthoringGameState removeObjectives(ResultQuadPredicate... objectives) {
		return (AuthoringGameState) super.removeObjectives(objectives);
	}

	@Override
	public AuthoringGameState removeObjectives(Collection<ResultQuadPredicate> objectives) {
		return (AuthoringGameState) super.removeObjectives(objectives);
	}

	@Override
	public AuthoringGameState removeTeamByName(String name) {
		return (AuthoringGameState) super.removeTeamByName(name);
	}

	@Override
	public AuthoringGameState removeTurnActions(Event event, Collection<BiConsumer<ImmutablePlayer, GameplayState>> actions) {
		return (AuthoringGameState) super.removeTurnActions(event, actions);
	}

	@Override
	public AuthoringGameState removeTurnActions(Event event, BiConsumer<ImmutablePlayer, GameplayState>[] actions) {
		return (AuthoringGameState) super.removeTurnActions(event, actions);
	}

	@Override
	public AuthoringGameState removeTurnRequirements(Collection<BiPredicate<ImmutablePlayer, GameplayState>> turnRequirements) {
		return (AuthoringGameState) super.removeTurnRequirements(turnRequirements);
	}

	@Override
	public AuthoringGameState removeTurnRequirements(BiPredicate<ImmutablePlayer, GameplayState>... turnRequirements) {
		return (AuthoringGameState) super.removeTurnRequirements(turnRequirements);
	}

	//Name can be Terrain, OffensiveModifier, DefensiveModifier, Cell, CellTriggeredEffect, UnitTriggeredEffect, ActiveAbility, Unit, UnitStat, GridPattern, GameBoard
	//Case and space character insensitive
	//Plural singular insensitive
	public ModifiableVoogaCollection<VoogaEntity, ModifiableVoogaCollection> getTemplateByCategory(String categoryName) {
		return templates.get(categoryName.replaceAll(" ", "").toLowerCase().replaceAll("ies$", "y").replaceAll("s$", ""));
	}

	public VoogaEntity getTemplateByName(String name) {
		return templates.values().stream().flatMap(ImmutableVoogaCollection::stream).filter(e -> e.getName().equals(name)).findAny().orElseThrow(() -> new RuntimeException("Template not found"));
	}

}
