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
public class GameplayState extends ImmutableVoogaObject {
	private final Random random;
	private int turnNumber;
	private int currentPlayerNumber;
	private List<String> playerNames;
	private Map<String, Player> playerList;
	private Map<String, Team> teams;
	private GameBoard grid;

	private Collection<ResultQuadPredicate> objectives;
	private Map<Event, Collection<BiConsumer<Player, GameplayState>>> turnActions;
	private Collection<BiPredicate<Player, GameplayState>> turnRequirements;

	public GameplayState(String name, GameBoard grid, String description, String imgPath) {
		super(name, description, imgPath);
		teams = new HashMap<>();
		this.grid = grid;
		this.random = new Random(7);
		this.turnNumber = 0;
	}

	private GameplayState(String name, GameBoard grid, int turnNumber, Collection<Team> teams,
	                      Collection<ResultQuadPredicate> objectives,
	                      Map<Event, Collection<BiConsumer<Player, GameplayState>>> turnActions,
	                      Collection<BiPredicate<Player, GameplayState>> turnRequirements,
	                      String description, String imgPath, Random random) {
		super(name, description, imgPath);
		this.grid = grid.copy();
		this.teams = teams.stream().map(Team::copy).collect(Collectors.toMap(Team::getName, e -> e));
		this.random = random;
		this.turnNumber = turnNumber;
		this.turnActions = turnActions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue())));
		this.objectives = new HashSet<>(objectives);
		this.turnRequirements = new HashSet<>(turnRequirements);
	}

	public Player getCurrentPlayer() {
		return playerList.get(playerNames.get(currentPlayerNumber));
	}

	public Player getPlayerByName(String name) {
		return playerList.get(name);
	}

	GameplayState addTeam(Team team) {
		teams.put(team.getName(), team);
		return this;
	}

	public Team getTeamByName(String teamName) {
		return teams.get(teamName);
	}

	GameplayState removeTeamByName(String name) {
		teams.remove(name);
		return this;
	}

	public Collection<Team> getTeams() {
		return Collections.unmodifiableCollection(teams.values());
	}

	public GameplayState endTurn() {
		currentPlayerNumber = (currentPlayerNumber + 1) % playerNames.size();
		turnNumber++;
		return this;
	}

	public int getTurnNumber() {
		return turnNumber;
	}

	public GameplayState addPlayer(Player newPlayer) {
		return addPlayer(newPlayer, new Team(newPlayer.getName() + "'s Team", "", ""));
	}

	public GameplayState addPlayer(Player newPlayer, Team team) {
		playerNames.add(newPlayer.getName());
		playerList.putIfAbsent(newPlayer.getName(), newPlayer);
		team.addAll(newPlayer);
		return this;
	}

	public GameBoard getGrid() {
		return grid;
	}

	GameplayState setGrid(GameBoard grid) {
		this.grid = grid;
		return this;
	}

	public double random() {
		return random.nextDouble();
	}

	public Collection<ResultQuadPredicate> getObjectives() {
		return Collections.unmodifiableCollection(objectives);
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

	public Map<Event, Collection<BiConsumer<Player, GameplayState>>> getTurnActions() {
		return Collections.unmodifiableMap(turnActions);
	}

	GameplayState addTurnActions(Event event, Collection<BiConsumer<Player, GameplayState>> actions) {
		turnActions.merge(event, new ArrayList<>(actions), (oldActions, newActions) -> Stream.of(oldActions, newActions).flatMap(Collection::stream).collect(Collectors.toList()));
		return this;
	}

	GameplayState addTurnActions(Event event, BiConsumer<Player, GameplayState>... actions) {
		return addTurnActions(event, Arrays.asList(actions));
	}

	//TODO: Doesn't workbecause there's no way to getByName a collection of BiConsumers you want to remove
	GameplayState removeTurnActions(Event event, Collection<BiConsumer<Player, GameplayState>> actions) {
		turnActions.get(event).removeIf(actions::contains);
		return this;
	}

	//TODO: Doesn't work
	GameplayState removeTurnActions(Event event, BiConsumer<Player, GameplayState>... actions) {
		return removeTurnActions(event, Arrays.asList(actions));
	}

	public Collection<BiPredicate<Player, GameplayState>> getTurnRequirements() {
		return Collections.unmodifiableCollection(turnRequirements);
	}

	GameplayState addTurnRequirements(Collection<BiPredicate<Player, GameplayState>> turnRequirements) {
		this.turnRequirements.addAll(turnRequirements);
		return this;
	}

	GameplayState addTurnRequirements(BiPredicate<Player, GameplayState>... turnRequirements) {
		return addTurnRequirements(Arrays.asList(turnRequirements));
	}

	GameplayState removeTurnRequirements(Collection<BiPredicate<Player, GameplayState>> turnRequirements) {
		this.turnRequirements.removeAll(turnRequirements);
		return this;
	}

	GameplayState removeTurnRequirements(BiPredicate<Player, GameplayState>... turnRequirements) {
		return removeTurnRequirements(Arrays.asList(turnRequirements));
	}

	public boolean turnRequirementsSatisfied() {
		return turnRequirements.stream().allMatch(e -> e.test(getCurrentPlayer(), this));
	}

	public GameplayState messageAll(String message, ImmutablePlayer sender) {
		ChatMessage chatMessage = new ChatMessage(ChatMessage.AccessLevel.ALL, sender, message);
		getTeams().forEach(team -> team.forEach(player -> player.receiveMessage(chatMessage)));
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
		System.out.println(sender.getTeam().size());
		return this;
	}

	@Override
	public GameplayState copy() {
		return new GameplayState(getName(), getGrid(), turnNumber, getTeams(), objectives, turnActions, turnRequirements, getDescription(), getImgPath(), random);
	}
}
