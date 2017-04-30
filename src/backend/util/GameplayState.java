package backend.util;

import backend.cell.Terrain;
import backend.game_engine.Resultant;
import backend.grid.BoundsHandler;
import backend.grid.GameBoard;
import backend.grid.GridPattern;
import backend.grid.ModifiableGameBoard;
import backend.player.ChatMessage;
import backend.player.ImmutablePlayer;
import backend.player.Team;
import backend.unit.ModifiableUnit;
import backend.unit.properties.ActiveAbility;
import backend.unit.properties.InteractionModifier;
import backend.unit.properties.ModifiableUnitStat;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 4/10/2017.
 */
public class GameplayState extends ImmutableVoogaObject implements ReadonlyGameplayState {
	private static final long serialVersionUID = 1L;

	public transient static final String
			CELL_TRIGGERED_EFFECT = "celltriggeredeffect",
			UNIT_TRIGGERED_EFFECT = "unittriggeredeffect",
			ACTIVE_ABILITY = "activeability",
			UNIT = "unit",
			UNIT_STAT = "unitstat",
			GRID_PATTERN = "gridpattern",
			GAMEBOARD = "gameboard",
			TEAM = "team",
			BOUNDS_HANDLER = "boundshandler",
			TERRAIN = "terrain",
			OFFENSIVE_MODIFIER = "offensivemodifier",
			DEFENSIVE_MODIFIER = "defensivemodifier",
			TURN_REQUIREMENT = "turnrequirement",
			TURN_EVENT = "turnaction",
			END_CONDITION = "endcondition";
	private final Random random;
	private final Collection<Resultant> objectives;
	private final TreeSet<ImmutablePlayer> players;
	private final Collection<Actionable> turnActions;
	private final Collection<Requirement> turnRequirements;
	private final Map<String, ModifiableVoogaCollection<VoogaEntity, ? extends ModifiableVoogaCollection>> templates;
	private boolean isAuthoringMode;
	private volatile int currentTeamNumber;
	private volatile int turnNumber;
	private volatile GameBoard grid;

	public GameplayState(String name, GameBoard grid, String description, String imgPath) {
		this(name, grid, 0, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), description, imgPath, new Random(7));
	}

	private GameplayState(String name, GameBoard grid, int turnNumber, Collection<Team> teams,
	                      Collection<Resultant> objectives,
	                      Collection<Actionable> turnActions,
	                      Collection<Requirement> turnRequirements,
	                      String description, String imgPath, Random random) {
		super(name, description, imgPath);
		this.grid = grid;
		this.random = random;
		this.turnNumber = turnNumber;
		this.turnActions = new HashSet<Actionable>(turnActions);
		this.objectives = new HashSet<>(objectives);
		this.turnRequirements = new HashSet<>(turnRequirements);
		this.players = new TreeSet<>(new Comparator<ImmutablePlayer>() {
			public int compare (ImmutablePlayer p1, ImmutablePlayer p2) {
				if (p1.getTeam().isPresent() && p2.getTeam().isPresent()) {
					return p1.getTeam().get().getName().compareTo(p2.getTeam().get().getName());
				} else if (p1.getTeam().isPresent()) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		this.currentTeamNumber = 0;
		this.isAuthoringMode = false;
		this.templates = new HashMap<>();
		getTemplates().put(GAMEBOARD, new ModifiableVoogaCollection<>("GameBoards", "", "", ModifiableGameBoard.getPredefinedGameBoards()));
		getTemplates().put(TERRAIN, new ModifiableVoogaCollection<>("Terrain", "", "", Terrain.getPredefinedTerrain()));
		getTemplates().put(UNIT, new ModifiableVoogaCollection<>("Units", "", "", ModifiableUnit.getPredefinedUnits()));
		getTemplates().put(UNIT_TRIGGERED_EFFECT, new ModifiableVoogaCollection<>("Unit Passive/Triggered Abilities", "", "", ModifiableTriggeredEffect.getPredefinedTriggeredUnitAbilities()));
		getTemplates().put(CELL_TRIGGERED_EFFECT, new ModifiableVoogaCollection<>("Cell Passive/Triggered Abilities", "", "", ModifiableTriggeredEffect.getPredefinedTriggeredCellAbilities()));
		getTemplates().put(UNIT_STAT, new ModifiableVoogaCollection<>("Unit Stats", "", "", ModifiableUnitStat.getPredefinedUnitStats()));
		getTemplates().put(GRID_PATTERN, new ModifiableVoogaCollection<>("Grid Patterns", "", "", GridPattern.getPredefinedGridPatterns()));
		getTemplates().put(BOUNDS_HANDLER, new ModifiableVoogaCollection<>("Bounds Handlers", "", "", BoundsHandler.getPredefinedBoundsHandlers()));
		getTemplates().put(ACTIVE_ABILITY, new ModifiableVoogaCollection<>("Active Abilities", "", "", ActiveAbility.getPredefinedActiveAbilities()));
		getTemplates().put(OFFENSIVE_MODIFIER, new ModifiableVoogaCollection<>("Offensive Modifiers", "", "", InteractionModifier.getPredefinedOffensiveModifiers()));
		getTemplates().put(DEFENSIVE_MODIFIER, new ModifiableVoogaCollection<>("Defensive Modifiers", "", "", InteractionModifier.getPredefinedDefensiveModifiers()));
		getTemplates().put(TEAM, new ModifiableVoogaCollection<>("Teams", "", "", teams));
		getTemplates().put(TURN_REQUIREMENT, new ModifiableVoogaCollection<>("Turn Requirements", "", "", turnRequirements));
		getTemplates().put(TURN_EVENT, new ModifiableVoogaCollection<>("Turn Actions", "", "", turnActions));
		getTemplates().put(END_CONDITION, new ModifiableVoogaCollection<>("Objectives", "", "", objectives));
	}

	@Override
	public Team getActiveTeam() {
		return getTeams().isEmpty() ? null : getTeams().get(currentTeamNumber);
	}

	@Override
	public ImmutablePlayer getPlayerByName(String name) {
		return players.stream().filter(e -> e.getName().equals(name)).findAny().orElse(null);
	}

	@Override
	public List<String> getOrderedPlayerNames() {
		return players.stream().map(ImmutablePlayer::getName).collect(Collectors.toList());
	}

	@Override
	public boolean isAuthoringMode() {
		return isAuthoringMode;
	}

	@Override
	public void setAuthoringMode(boolean isAuthoringMode) {
		this.isAuthoringMode = isAuthoringMode;
	}

	@Override
	public Team getTeamByName(String teamName) {
		return (Team) templates.get(TEAM).getByName(teamName);
	}

	@Override
	public List<Team> getTeams() {
		return new ArrayList<>((Collection<Team>) getTemplates().get(TEAM).getAll());
	}

	public GameplayState endTurn() {
		getGrid().endTurn(this);
		if (++currentTeamNumber >= getTeams().size()) {
			++turnNumber;
			currentTeamNumber = 0;
		}
		getGrid().startTurn(this);
		return this;
	}

	@Override
	public int getTurnNumber() {
		return turnNumber;
	}

	public GameplayState addPlayer(ImmutablePlayer newPlayer) {
		players.add(newPlayer);
		return this;
	}

	public GameplayState joinTeam(String playerName, String teamName) {
		getPlayerByName(playerName).setTeam(getTeamByName(teamName));
		return this;
	}

	public GameplayState removePlayer(String playerName) {
		return removePlayer(getPlayerByName(playerName));
	}

	public GameplayState removePlayer(ImmutablePlayer player) {
		player.getTeam().ifPresent(team -> team.removeAll(player));
		players.remove(player);
		return removePlayer(player.getName());
	}

	@Override
	public GameBoard getGrid() {
		return grid;
	}

	protected Map<String, ModifiableVoogaCollection<VoogaEntity, ? extends ModifiableVoogaCollection>> getTemplates() {
		return templates;
	}

	GameplayState setGrid(GameBoard grid) {
		this.grid = grid;
		return this;
	}

	@Override
	public double random() {
		return random.nextDouble();
	}

	@Override
	public Collection<Resultant> getObjectives() {
		return Collections.unmodifiableCollection(objectives);
	}

	@Override
	public Collection<Actionable> getTurnActions() {
		return Collections.unmodifiableCollection(turnActions);
	}

	@Override
	public Collection<Requirement> getTurnRequirements() {
		return Collections.unmodifiableCollection(turnRequirements);
	}

	@Override
	public boolean turnRequirementsSatisfied() {
		return turnRequirements.stream().allMatch(e -> e.test(getActiveTeam(), this));
	}

	public GameplayState messageAll(String message, ImmutablePlayer sender) {
		ChatMessage chatMessage = new ChatMessage(ChatMessage.AccessLevel.ALL, sender, message);
		players.forEach(player -> player.receiveMessage(chatMessage));
		return this;
	}

	public GameplayState messagePlayer(String message, ImmutablePlayer sender, ImmutablePlayer recipient) {
		ChatMessage chatMessage = new ChatMessage(ChatMessage.AccessLevel.WHISPER, sender, message);
		sender.receiveMessage(chatMessage);
		recipient.receiveMessage(chatMessage);
		return this;
	}

	public GameplayState messageTeam(String message, ImmutablePlayer sender) {
		ChatMessage chatMessage = new ChatMessage(ChatMessage.AccessLevel.TEAM, sender, message);
		sender.getTeam().ifPresent(e -> e.forEach(player -> player.receiveMessage(chatMessage)));
		return this;
	}

	@Override
	public GameplayState copy() {
		return new GameplayState(getName(), getGrid(), turnNumber, getTeams().stream().map(Team::copy).collect(Collectors.toList()), objectives, turnActions.stream().collect(Collectors.toList()), turnRequirements, getDescription(), getImgPath(), random);
	}

	GameplayState addObjectives(Resultant... objectives) {
		return addObjectives(Arrays.asList(objectives));
	}

	GameplayState addObjectives(Collection<Resultant> objectives) {
		this.objectives.addAll(objectives);
		return this;
	}

	GameplayState removeObjectives(Resultant... objectives) {
		return removeObjectives(Arrays.asList(objectives));
	}

	GameplayState removeObjectives(Collection<Resultant> objectives) {
		this.objectives.removeAll(objectives);
		return this;
	}

	GameplayState addTurnActions(Collection<Actionable> actions) {
		turnActions.addAll(actions);
		return this;
	}

	GameplayState addTurnActions(Actionable... actions) {
		return addTurnActions(Arrays.asList(actions));
	}

	GameplayState removeTurnActions(Event event, Collection<Actionable> actions) {
		turnActions.removeIf(actions::contains);
		return this;
	}

	GameplayState removeTurnActions(Event event, Actionable... actions) {
		return removeTurnActions(event, Arrays.asList(actions));
	}

	GameplayState addTurnRequirements(Collection<Requirement> turnRequirements) {
		this.turnRequirements.addAll(turnRequirements);
		return this;
	}

	GameplayState addTurnRequirements(Requirement... turnRequirements) {
		return addTurnRequirements(Arrays.asList(turnRequirements));
	}

	GameplayState removeTurnRequirements(Collection<Requirement> turnRequirements) {
		this.turnRequirements.removeAll(turnRequirements);
		return this;
	}

	GameplayState removeTurnRequirements(Requirement... turnRequirements) {
		return removeTurnRequirements(Arrays.asList(turnRequirements));
	}

	public AuthoringGameState removeTurnActions(Actionable[] actions) {
		return null;
	}
}
