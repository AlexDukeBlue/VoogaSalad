package controller;

import backend.cell.Cell;
import backend.game_engine.DieselEngine;
import backend.game_engine.ResultQuadPredicate;
import backend.game_engine.ResultQuadPredicate.Result;
import backend.game_engine.Resultant;
import backend.grid.BoundsHandler;
import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.Shape;
import backend.player.ChatMessage;
import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.player.Team;
import backend.unit.Unit;
import backend.util.*;
import backend.util.Actionable.SerializableBiConsumer;
import backend.util.Requirement.SerializableBiPredicate;
import backend.util.io.XMLSerializer;
import frontend.util.BaseUIManager;
import frontend.util.UIComponentListener;
import javafx.application.Platform;
import util.io.Serializer;
import util.net.Modifier;
import util.net.ObservableClient;
import util.net.ObservableServer;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Created by ncp14, th174
 *         This class is the communication controller which communicates between the frontend and backend.
 *         The primary purpose of my controller is to hide implementation of backend structure, specifically how
 *         our networking works and how the GameState is structured.
 */
public class CommunicationController implements Controller {
	//TODO RESOURCE BUNDLE PLS
	private static final XMLSerializer XML = new XMLSerializer<>();
	private static final String AUTOSAVE_DIRECTORY = System.getProperty("user.dir") + "/data/saved_game_data/autosaves/";
	private final Executor executor;
	private final Collection<UIComponentListener> thingsToUpdate;
	private final CountDownLatch waitForReady;
	private int playerCountCache;
	private ObservableClient<ReadonlyGameplayState> client;
	private ObservableServer<ReadonlyGameplayState> server;
	private String playerName;
	private Deque<Path> saveHistory;
	private DieselEngine engine;

	public CommunicationController(String username) {
		this(username, Collections.emptyList());
	}

	public CommunicationController(String username, Collection<UIComponentListener> thingsToUpdate) {
		this.thingsToUpdate = new CopyOnWriteArrayList<>(thingsToUpdate);
		this.waitForReady = new CountDownLatch(1);
		this.playerName = username;
		this.executor = Executors.newCachedThreadPool();
		this.playerCountCache = 1;
		this.saveHistory = new ArrayDeque<>();
		this.engine = new DieselEngine();
	}

	public void startClient(String host, int port, Duration timeout) {
		try {
			client = new ObservableClient<>(host, port, XML, XML, timeout);
			client.addListener(newGameState -> updateGameState());
			executor.execute(client);
			setPlayer(this.playerName, "", "");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void startServer(ReadonlyGameplayState gameState, int port, Duration timeout) {
		try {
			server = new ObservableServer<>(gameState, port, XML, XML, timeout);
			executor.execute(server);
			System.out.println("Server started successfully on port: " + port);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void saveState(Path path) throws IOException {
		save(getAuthoringGameState(), path);
	}

	@Override
	public void save(Serializable obj, Path path) throws IOException {
		Files.createDirectories(path.getParent());
		Files.write(path, ((String) XML.serialize(obj)).getBytes());
	}

	@Override
	public <T extends Serializable> T load(Path path) throws IOException {
		return (T) XML.unserialize(new String(Files.readAllBytes(path)));
	}

	@Override
	public GameBoard getGrid() {
		return getGameplayState().getGrid();
	}

	@Override
	public void setGrid(GameBoard gameBoard) {
		sendModifier((AuthoringGameState state) -> {
			state.setGrid(gameBoard);
			return state;
		});
	}

	@Override
	public String getActivePlayerName() {
		return getAuthoringGameState().getActiveTeam().getName();
	}

	@Override
	public String getMyPlayerName() {
		return playerName;
	}

	@Override
	public Cell getCell(CoordinateTuple tuple) {
		return getGrid().get(tuple);
	}

	public ObservableClient<ReadonlyGameplayState> getClient() {
		return client;
	}

	public void setClient(ObservableClient<ReadonlyGameplayState> client) {
		this.client = client;
	}

	@Override
	public <U extends ReadonlyGameplayState> void setGameState(U gameState) {
		getClient().addToOutbox(gameState);
	}

	@Override
	public ReadonlyGameplayState getReadOnlyGameState() {
		return getClient().getState();
	}

	@Override
	public AuthoringGameState getAuthoringGameState() {
		try {
			waitForReady.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return (AuthoringGameState) getClient().getState();
	}

	@Override
	public GameplayState getGameplayState() {
		return (GameplayState) getClient().getState();
	}

	@Override
	public ImmutablePlayer getPlayer(String name) {
		return getGameplayState().getPlayerByName(name);
	}

	@Override
	public <U extends ReadonlyGameplayState> void sendModifier(Modifier<U> modifier) {
		try {
			waitForReady.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.addToOutbox((Modifier<ReadonlyGameplayState>) modifier);
	}

	@Override
	public Collection<? extends Unit> getUnits() {
		return getGameplayState().getGrid().getUnits();
	}

	@Override
	public Collection<? extends VoogaEntity> getTemplatesByCategory(String category) {
		return getAuthoringGameState().getTemplateByCategory(category).getAll();
	}

	@Override
	public void addTemplatesByCategory(String category, VoogaEntity... templates) {
		System.out.println(category);
		System.out.println(templates);
		Arrays.stream(templates).map(VoogaEntity::getImgPath).forEach(this::sendFile);
		sendModifier((AuthoringGameState state) -> {
			state.getTemplateByCategory(category)
				 .addAll(templates);
			return state;
		});
	}

	@Override
	public void removeTemplatesByCategory(String category, String... templateNames) {
		sendModifier((AuthoringGameState state) -> {
			state.getTemplateByCategory(category).removeAll(templateNames);
			return state;
		});
	}

	@Override
	public void addListener(UIComponentListener listener) {
		thingsToUpdate.add(listener);
	}

	@Override
	public void removeListener(UIComponentListener listener) {
		thingsToUpdate.remove(listener);
	}

	@Override
	public void enterAuthoringMode() {
		sendModifier((AuthoringGameState state) -> {
			state.setAuthoringMode(true);
			return state;
		});
	}

	@Override
	public void enterGamePlayMode() {
		sendModifier((AuthoringGameState state) -> {
			state.setAuthoringMode(false);
			return state;
		});
	}

	@Override
	public boolean isAuthoringMode() {
		return getGameplayState().isAuthoringMode();
	}
	
	@Override
	public boolean activePlayerWon(){
		return getActivePlayer().getResult().equals(Result.WIN);
	}
	
	@Override
	public boolean activePlayerLost(){
		return getActivePlayer().getResult().equals(Result.LOSE);
	}
	
	@Override
	public boolean activePlayerTied(){
		return getActivePlayer().getResult().equals(Result.TIE);
	}

	@Override
	public void setPlayer(String name, String description, String imgPath) {
		this.playerName = name;
		Team temp = new Team(name + "'s Team", "Temporary team for " + name, Team.COLORS.get((int) (Math.random() * Team.COLORS.size())), imgPath);
		Player me = new Player(name, description, imgPath);
		sendModifier((AuthoringGameState state) -> {
			state.addPlayer(me);
			return state;
		});
	}

	@Override
	public void addTurnRequirement(String name, String description, String imgPath, SerializableBiPredicate biPredicate) {
		sendModifier((AuthoringGameState state) -> {
			state.addAvailableTurnRequirements(new Requirement(biPredicate, name, description, imgPath));
			return state;
		});
	}

	@Override
	public void removeTurnRequirement(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnRequirements().stream().filter(req -> req.getName().equals(name)).forEach(req -> state.removeAvailableTurnRequirements(req));
			return state;
		});
	}

	@Override
	public void activateTurnRequirement(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnRequirements().stream().filter(req -> req.getName().equals(name)).forEach(state::addTurnRequirements);
			return state;
		});
	}

	@Override
	public void deactivateTurnRequirement(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnRequirements().stream().filter(req -> req.getName().equals(name)).forEach(state::removeTurnRequirements);
			return state;
		});
	}

	@Override
	public void addTurnAction(String name, String description, String imgPath, SerializableBiConsumer biConsumer) {
		sendModifier((AuthoringGameState state) -> {
			state.addAvailableTurnActions(new Actionable(biConsumer, name, description, imgPath));
			return state;
		});
	}

	@Override
	public void removeTurnAction(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnActions().stream().filter(act -> act.getName().equals(name)).forEach(act -> state.removeAvailableTurnActions(act));
			return state;
		});
	}

	@Override
	public void activateTurnAction(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnActions().stream().filter(act -> act.getName().equals(name)).forEach(act -> state.addTurnActions(act));
			return state;
		});
	}

	@Override
	public void deactivateTurnAction(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnActions().stream().filter(act -> act.getName().equals(name)).forEach(act -> state.removeTurnActions(act));
			return state;
		});
	}

	@Override
	public void addEndCondition(String name, String description, String imgPath, ResultQuadPredicate resultQuadPredicate) {
		sendModifier((AuthoringGameState state) -> {
			state.addObjectives(new Resultant(resultQuadPredicate, name, description, imgPath));
			return state;
		});
	}

	@Override
	public void removeEndCondition(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableObjectives().removeIf(obj -> obj.getName().equals(name));
			return state;
		});
	}

	@Override
	public void activateEndCondition(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableObjectives().removeIf(obj -> obj.getName().equals(name));
			return state;
		});
	}

	@Override
	public void deactivateEndCondition(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableObjectives().removeIf(obj -> obj.getName().equals(name));
			return state;
		});
	}

	@Override
	public void updateAll() {
		executor.execute(() -> {
			try {
				Path autoSavePath = Paths.get(String.format("%s/%s/autosave_turn-%d_%s.xml", AUTOSAVE_DIRECTORY, getAuthoringGameState().getName().length() < 1 ? "Untitled" : getAuthoringGameState().getName(), getAuthoringGameState().getTurnNumber(), Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_SS"))));
				saveState(autoSavePath);
				saveHistory.push(autoSavePath);
			} catch (Serializer.SerializationException e) {
				System.err.println("You're going TOO FAST!!!!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		engine.checkGame(this.getGameplayState());
		thingsToUpdate.forEach(e -> Platform.runLater(() -> {
			try {
				e.update();
			} catch (Exception e1) {
				removeListener(e);
			}
		}));
	}

	@Override
	public void joinTeam(String teamName) {
		String playerName = this.playerName;
		sendModifier((GameplayState state) -> state.joinTeam(playerName, teamName));
	}

	@Override
	public void sendFile(String path) {
		if (Objects.nonNull(path) && path.length() > 0) {
			try {
				byte[] buffer = Files.readAllBytes(Paths.get(path));
				sendModifier(state -> {
					Path newFilePath = Paths.get(path);
					if (Files.notExists(newFilePath)) {
						if (Objects.nonNull(newFilePath.getParent())) {
							Files.createDirectories(newFilePath.getParent());
						}
						Files.write(newFilePath, buffer);
					}
					return state;
				});
			} catch (IOException e) {
				throw new Error(e);
			}
		}
	}

	@Override
	public void endTurn() {
		sendModifier(GameplayState::endTurn);
	}

	@Override
	public void moveUnit(String unitName, CoordinateTuple unitLocation, CoordinateTuple targetLocation) {
		sendModifier((GameplayState state) -> {
			Unit unitToMove = state.getGrid().get(unitLocation).getOccupantByName(unitName);
			unitToMove.moveTo(state.getGrid().get(targetLocation), state);
			return state;
		});
	}

	@Override
	public void sendMessage(String messageContent, ChatMessage.AccessLevel accessLevel, String receiverName) {
		sendModifier(accessLevel.getSendMessageModifier(messageContent, playerName, receiverName));
	}

	@Override
	public void setBoundsHandler(String boundsHandlerName) {
		sendModifier((AuthoringGameState state) -> {
			state.getGrid().setBoundsHandler((BoundsHandler) state.getTemplateByCategory("boundshandler").getByName(boundsHandlerName));
			return state;
		});
	}

	@Override
	public void copyTemplateToGrid(VoogaEntity template, HasLocation destination) {
		String templateName = template.getName();
		String targetUnitName = destination.getName();
		CoordinateTuple gridLocation = destination.getLocation();
		String playerName = getMyPlayerName();
		sendModifier((AuthoringGameState gameState) -> {
			VoogaEntity templateCopy = gameState.getTemplateByName(templateName).copy();
			try {
				Unit targetUnit = gameState.getGrid().get(gridLocation).getOccupantByName(targetUnitName);
				if (Objects.nonNull(targetUnit)) {
					targetUnit.add(templateCopy);
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				Cell targetCell = gameState.getGrid().get(gridLocation);
				if (Objects.nonNull(targetCell)) {
					//TODO: MAKE BETTER WAY OF SETTING OWNER OTHER THAN WHOEVER HAPPENS TO PUT IT DOWN
					if (templateCopy instanceof Unit) {
						((Unit) templateCopy).setTeam(gameState.getPlayerByName(playerName).getTeam().orElse(null));
					}
					targetCell.add(templateCopy);
				} else {
					e.printStackTrace();
				}
			}
			return gameState;
		});
	}

	@Override
	public void removeUnitFromGrid(String unitName, CoordinateTuple unitLocation) {
		sendModifier((AuthoringGameState gameState) -> {
			gameState.getGrid().get(unitLocation).removeOccupants(unitName);
			return gameState;
		});

	}

	@Override
	public void useUnitActiveAbility(String abilityName, String userName, CoordinateTuple userLocation, String targetName, CoordinateTuple targetLocation) {
		sendModifier((GameplayState gameState) -> {
			VoogaEntity abilityTarget = gameState.getGrid().get(targetLocation).getOccupantByName(targetName);
			gameState.getGrid().get(userLocation).getOccupantByName(userName).useActiveAbility(abilityName, abilityTarget, gameState);
			return gameState;
		});
	}

	@Override
	public void undo() {
		try {
			saveHistory.pop();
			setGameState(this.load(saveHistory.pop()));
		} catch (IOException ignored) {
		}
	}

	@Override
	public Shape getShape() {
		return getGrid().getShape();
	}

	private synchronized void updateGameState() {
		updateAll();
		if (Objects.nonNull(server) && getGameplayState().getOrderedPlayerNames().size() > playerCountCache) {
			BaseUIManager.getResourcePaths().forEach(this::sendFile);
		}
		playerCountCache = getGameplayState().getOrderedPlayerNames().size();
		waitForReady.countDown();
	}
}