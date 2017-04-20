package backend.util;

import backend.game_engine.ResultQuadPredicate;
import backend.grid.GameBoard;
import backend.player.ChatMessage;
import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.player.Team;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Created by th174 on 4/10/2017.
 */
public class GameplayState extends ImmutableVoogaObject implements ReadonlyGameplayState {
	private final Random random;
	private final List<String> playerNames;
	private final Map<String, ImmutablePlayer> playerList;
	private final Map<String, Team> teams;
	private final Collection<ResultQuadPredicate> objectives;
	private final Map<Event, Collection<BiConsumer<ImmutablePlayer, GameplayState>>> turnActions;
	private final Collection<BiPredicate<ImmutablePlayer, GameplayState>> turnRequirements;
	private int turnNumber;
	private int currentPlayerNumber;
	private volatile GameBoard grid;
	private boolean isAuthoringMode;
	
	//private Collection<ModifiableUnit> allUnits;

	public GameplayState(String name, GameBoard grid, String description, String imgPath) {
		this(name, grid, 0, Collections.emptyMap(), Collections.emptyList(), Collections.emptyMap(), Collections.emptyList(), description, imgPath, new Random(7));
	}

	private GameplayState(String name, GameBoard grid, int turnNumber, Map<String, Team> teams,
	                      Collection<ResultQuadPredicate> objectives,
	                      Map<Event, Collection<BiConsumer<ImmutablePlayer, GameplayState>>> turnActions,
	                      Collection<BiPredicate<ImmutablePlayer, GameplayState>> turnRequirements,
	                      String description, String imgPath, Random random) {
		super(name, description, imgPath);
		this.grid = grid;
		this.teams = new HashMap<>(teams);
		this.random = random;
		this.turnNumber = turnNumber;
		this.turnActions = new HashMap<>(turnActions);
		this.objectives = new HashSet<>(objectives);
		this.turnRequirements = new HashSet<>(turnRequirements);
		this.playerList = new HashMap<>();
		this.playerNames = new ArrayList<>();
		this.isAuthoringMode = false;
	}

	@Override
	public ImmutablePlayer getCurrentPlayer() {
		return playerList.get(playerNames.get(currentPlayerNumber));
	}

	@Override
	public ImmutablePlayer getPlayerByName(String name) {
		return playerList.get(name);
	}

	@Override
	public List<? extends ImmutablePlayer> getAllPlayers() {
		return Collections.unmodifiableList(playerNames.stream().map(playerList::get).collect(Collectors.toList()));
	}

	@Override
	public boolean isAuthoringMode() {
		return isAuthoringMode;
	}

	@Override
	public Team getTeamByName(String teamName) {
		return teams.get(teamName);
	}

	@Override
	public Collection<Team> getTeams() {
		return Collections.unmodifiableCollection(teams.values());
	}

	public GameplayState endTurn() {
		currentPlayerNumber = (currentPlayerNumber + 1) % playerNames.size();
		turnNumber++;
		return this;
	}

	@Override
	public int getTurnNumber() {
		return turnNumber;
	}

	public GameplayState addPlayer(ImmutablePlayer newPlayer) {
		return addPlayer(newPlayer, newPlayer.getTeam());
	}

	public GameplayState addPlayer(ImmutablePlayer newPlayer, Team team) {
		if (playerNames.contains(newPlayer.getName())) {
			((Player) newPlayer).setName(newPlayer.getName() + " (2)");
		}
		playerNames.add(newPlayer.getName());
		playerList.put(newPlayer.getName(), newPlayer);
		team.addAll(newPlayer);
		teams.put(team.getName(), team);
		return this;
	}

	public GameplayState removePlayer(String playerName) {
		playerList.remove(playerName);
		playerNames.remove(playerName);
		return this;
	}

	public GameplayState removePlayer(ImmutablePlayer player) {
		return removePlayer(player.getName());
	}

	@Override
	public GameBoard getGrid() {
		return grid;
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
	public Collection<ResultQuadPredicate> getObjectives() {
		return Collections.unmodifiableCollection(objectives);
	}

	@Override
	public Map<Event, Collection<BiConsumer<ImmutablePlayer, GameplayState>>> getTurnActions() {
		return Collections.unmodifiableMap(turnActions);
	}

	@Override
	public Collection<BiPredicate<ImmutablePlayer, GameplayState>> getTurnRequirements() {
		return Collections.unmodifiableCollection(turnRequirements);
	}

	@Override
	public boolean turnRequirementsSatisfied() {
		return turnRequirements.stream().allMatch(e -> e.test(getCurrentPlayer(), this));
	}

	public GameplayState messageAll(String message, ImmutablePlayer sender) {
		ChatMessage chatMessage = new ChatMessage(ChatMessage.AccessLevel.ALL, sender, message);
		getAllPlayers().forEach(player -> player.receiveMessage(chatMessage));
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
		sender.getTeam().forEach(player -> player.receiveMessage(chatMessage));
		return this;
	}

	@Override
	public GameplayState copy() {
		return new GameplayState(getName(), getGrid(), turnNumber, getTeams().stream().map(Team::copy).collect(Collectors.toMap(Team::getName, e -> e)), objectives, turnActions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue()))), turnRequirements, getDescription(), getImgPath(), random);
	}

	GameplayState addTeam(Team team) {
		teams.put(team.getName(), team);
		team.forEach(p -> addPlayer(p, team));
		return this;
	}

	GameplayState removeTeamByName(String name) {
		teams.get(name).stream().map(ImmutablePlayer::getName).forEach(this::removePlayer);
		teams.remove(name);
		return this;
	}

	GameplayState addObjectives(ResultQuadPredicate... objectives) {
		return addObjectives(Arrays.asList(objectives));
	}

	GameplayState addObjectives(Collection<ResultQuadPredicate> objectives) {
		this.objectives.addAll(objectives);
		return this;
	}

	GameplayState removeObjectives(ResultQuadPredicate... objectives) {
		return removeObjectives(Arrays.asList(objectives));
	}

	GameplayState removeObjectives(Collection<ResultQuadPredicate> objectives) {
		this.objectives.removeAll(objectives);
		return this;
	}

	GameplayState addTurnActions(Event event, Collection<BiConsumer<ImmutablePlayer, GameplayState>> actions) {
		turnActions.merge(event, new ArrayList<>(actions), (oldActions, newActions) -> Stream.of(oldActions, newActions).flatMap(Collection::stream).collect(Collectors.toList()));
		return this;
	}

	GameplayState addTurnActions(Event event, BiConsumer<ImmutablePlayer, GameplayState>... actions) {
		return addTurnActions(event, Arrays.asList(actions));
	}

	//TODO: Doesn't work because there's no way to getByName a collection of BiConsumers you want to remove, same goes for all of these
	GameplayState removeTurnActions(Event event, Collection<BiConsumer<ImmutablePlayer, GameplayState>> actions) {
		turnActions.get(event).removeIf(actions::contains);
		return this;
	}

	GameplayState removeTurnActions(Event event, BiConsumer<ImmutablePlayer, GameplayState>... actions) {
		return removeTurnActions(event, Arrays.asList(actions));
	}

	GameplayState addTurnRequirements(Collection<BiPredicate<ImmutablePlayer, GameplayState>> turnRequirements) {
		this.turnRequirements.addAll(turnRequirements);
		return this;
	}

	GameplayState addTurnRequirements(BiPredicate<ImmutablePlayer, GameplayState>... turnRequirements) {
		return addTurnRequirements(Arrays.asList(turnRequirements));
	}

	GameplayState removeTurnRequirements(Collection<BiPredicate<ImmutablePlayer, GameplayState>> turnRequirements) {
		this.turnRequirements.removeAll(turnRequirements);
		return this;
	}

	GameplayState removeTurnRequirements(BiPredicate<ImmutablePlayer, GameplayState>... turnRequirements) {
		return removeTurnRequirements(Arrays.asList(turnRequirements));
	}
}
