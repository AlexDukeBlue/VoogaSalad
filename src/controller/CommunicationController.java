package controller;

import backend.cell.Cell;
import backend.game_engine.ResultQuadPredicate;
import backend.game_engine.Resultant;
import backend.grid.BoundsHandler;
import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.Shape;
import backend.player.ChatMessage;
import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.unit.Unit;
import backend.util.*;
import backend.util.Actionable.SerializableBiConsumer;
import backend.util.Requirement.SerializableBiPredicate;
import backend.util.io.XMLSerializer;
import frontend.util.UIComponentListener;
import javafx.application.Platform;
import util.io.Serializer;
import util.net.Modifier;
import util.net.ObservableClient;
import util.net.ObservableServer;

import java.io.IOException;
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
	private static final XMLSerializer<ReadonlyGameplayState> XML = new XMLSerializer<>();
	private static final String AUTOSAVE_DIRECTORY = System.getProperty("user.dir") + "/data/saved_game_data/autosaves/";

	private final Executor executor;
	private ObservableClient<ReadonlyGameplayState> mClient;
	private final Collection<UIComponentListener> thingsToUpdate;
	private String playerName;
	private final CountDownLatch waitForReady;
	private Deque<Path> saveHistory;

	public CommunicationController(String username) {
		this(username, Collections.emptyList());
	}

	public CommunicationController(String username, Collection<UIComponentListener> thingsToUpdate) {
		this.thingsToUpdate = new CopyOnWriteArrayList<>(thingsToUpdate);
		this.waitForReady = new CountDownLatch(1);
		this.playerName = username;
		this.executor = Executors.newCachedThreadPool();
		this.saveHistory = new ArrayDeque<>();
	}

	public void startClient(String host, int port, Duration timeout) {
		try {
			mClient = new ObservableClient<>(host, port, XML, XML, timeout);
			mClient.addListener(newGameState -> updateGameState());
			executor.execute(mClient);
			addPlayer(this.playerName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void startServer(ReadonlyGameplayState gameState, int port, Duration timeout) {
		try {
			ObservableServer<ReadonlyGameplayState> server = new ObservableServer<>(gameState, port, XML, XML, timeout);
			executor.execute(server);
			System.out.println("Server started successfully on port: " + port);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ReadonlyGameplayState loadFile(Path path) throws IOException {
		return XML.unserialize(new String(Files.readAllBytes(path)));
	}

	@Override
	public void saveFile(Path path) throws IOException {
		Files.createDirectories(path.getParent());
		Files.write(path, ((String) XML.serialize(getAuthoringGameState())).getBytes());
	}

	@Override
	public GameBoard getGrid() {
		return getGameState().getGrid();
	}

	@Override
	public void setGrid(GameBoard gameBoard) {
		sendModifier((AuthoringGameState state) -> {
			state.setGrid(gameBoard);
			return state;
		});
	}

	@Override
	public Cell getCell(CoordinateTuple tuple) {
		return getGrid().get(tuple);
	}

	private synchronized void updateGameState() {
		updateAll();
		waitForReady.countDown();
	}

	public void setClient(ObservableClient<ReadonlyGameplayState> client) {
		this.mClient = client;
	}

	public ObservableClient<ReadonlyGameplayState> getClient() {
		return mClient;
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
	public GameplayState getGameState() {
		return (GameplayState) getClient().getState();
	}

	@Override
	public ImmutablePlayer getPlayer(String name) {
		return getGameState().getPlayerByName(name);
	}

	@Override
	public <U extends ReadonlyGameplayState> void sendModifier(Modifier<U> modifier) {
		try {
			waitForReady.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mClient.addToOutbox((Modifier<ReadonlyGameplayState>) modifier);
	}

	@Override
	public Collection<? extends Unit> getUnits() {
		return getGameState().getGrid().getUnits();
	}

	@Override
	public Collection<? extends VoogaEntity> getTemplatesByCategory(String category) {
		return getAuthoringGameState().getTemplateByCategory(category).getAll();
	}

	@Override
	public void addTemplatesByCategory(String category, VoogaEntity... templates) {
		sendModifier((AuthoringGameState state) -> {
			state.getTemplateByCategory(category).addAll(templates);
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
		return getGameState().isAuthoringMode();
	}

	@Override
	public String getPlayerName() {
		return playerName;
	}

	@Override
	public void addPlayer(String playerName) {
		this.playerName = playerName;
		sendModifier((AuthoringGameState state) -> {
			state.addPlayer(new Player(playerName, "Test player", ""));
			return state;
		});
	}

	public void addTurnRequirement(String name, String description, String imgPath, SerializableBiPredicate biPredicate) {
		sendModifier((AuthoringGameState state) -> {
			state.addAvailableTurnRequirements(new Requirement(biPredicate, name, description, imgPath));
			return state;
		});
	}

	public void removeTurnRequirement(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnRequirements().stream().filter(req -> req.getName().equals(name)).forEach(req -> state.removeAvailableTurnRequirements(req));
			return state;
		});
	}

	public void activateTurnRequirement(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnRequirements().stream().filter(req -> req.getName().equals(name)).forEach(req -> state.addTurnRequirements(req));
			return state;
		});
	}

	public void deactivateTurnRequirement(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnRequirements().stream().filter(req -> req.getName().equals(name)).forEach(req -> state.removeTurnRequirements(req));
			return state;
		});
	}

	public void addTurnAction(Event event, String name, String description, String imgPath, SerializableBiConsumer biConsumer) {
		sendModifier((AuthoringGameState state) -> {
			state.addAvailableTurnActions(event, new Actionable(biConsumer, name, description, imgPath));
			return state;
		});
	}

	public void removeTurnAction(Event event, String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnActions().get(event).stream().filter(act -> act.getName().equals(name)).forEach(act -> state.removeAvailableTurnActions(event, act));
			return state;
		});
	}

	public void activateTurnAction(Event event, String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnActions().get(event).stream().filter(act -> act.getName().equals(name)).forEach(act -> state.addTurnActions(event, act));
			return state;
		});
	}

	public void deactivateTurnAction(Event event, String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableTurnActions().get(event).stream().filter(act -> act.getName().equals(name)).forEach(act -> state.removeTurnActions(event, act));
			return state;
		});
	}

	public void addEndCondition(String name, String description, String imgPath, ResultQuadPredicate resultQuadPredicate) {
		sendModifier((AuthoringGameState state) -> {
			state.addObjectives(new Resultant(resultQuadPredicate, name, description, imgPath));
			return state;
		});
	}

	public void removeEndCondition(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableObjectives().removeIf(obj -> obj.getName().equals(name));
			return state;
		});
	}

	public void activateEndCondition(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableObjectives().removeIf(obj -> obj.getName().equals(name));
			return state;
		});
	}

	public void deactivateEndCondition(String name) {
		sendModifier((AuthoringGameState state) -> {
			state.getAvailableObjectives().removeIf(obj -> obj.getName().equals(name));
			return state;
		});
	}

	private void updateAll() {
		executor.execute(() -> {
			try {
				Path autoSavePath = Paths.get(String.format("%s/%s/autosave_turn-%d_%s.xml", AUTOSAVE_DIRECTORY, getAuthoringGameState().getName().length() < 1 ? "Untitled" : getAuthoringGameState().getName(), getAuthoringGameState().getTurnNumber(), Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_SS"))));
				saveFile(autoSavePath);
				saveHistory.push(autoSavePath);
			} catch (Serializer.SerializationException e) {
				System.err.println("You're going TOO FAST!!!!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		thingsToUpdate.forEach(e -> Platform.runLater(() -> {
			try {
				e.update();
			} catch (Exception e1) {
				removeListener(e);
			}
		}));
	}

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
	public void copyTemplateToGrid(String templateName, CoordinateTuple gridLocation, String targetUnitName) {
		sendModifier((AuthoringGameState gameState) -> {
			try {
				Unit targetUnit = gameState.getGrid().get(gridLocation).getOccupantByName(targetUnitName);
				if (Objects.nonNull(targetUnit)) {
					targetUnit.add(gameState.getTemplateByName(templateName).copy());
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				Cell targetCell = gameState.getGrid().get(gridLocation);
				if (Objects.nonNull(targetCell)) {
					targetCell.add(gameState.getTemplateByName(templateName).copy());
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
			setGameState(loadFile(saveHistory.pop()));
		} catch (IOException ignored) {
		}
	}

	@Override
	public Shape getShape() {
		return getGrid().getShape();
	}
}