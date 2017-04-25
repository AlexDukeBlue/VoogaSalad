package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import backend.cell.Cell;
import backend.cell.ModifiableTerrain;
import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.unit.properties.ActiveAbility;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.ReadonlyGameplayState;
import backend.util.VoogaEntity;
import backend.util.io.XMLSerializer;
import frontend.util.UIComponentListener;
import javafx.application.Platform;
import util.net.Modifier;
import util.net.ObservableClient;
import util.net.ObservableServer;

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
	private static final Map<Class<?>,String> templateMap;
	static {
		Map<Class<?>,String> map = new HashMap<>();
		map.put(ModifiableUnit.class, "unit");
		map.put(ModifiableTerrain.class, "terrain");
		map.put(ActiveAbility.class, "activeability");
		templateMap = map;
	}
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
	public Cell getCell(CoordinateTuple tuple) {
		return getGrid().get(tuple);
	}

	private synchronized <U extends ReadonlyGameplayState> void updateGameState() {
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

	private void updateAll() {
		executor.execute(() -> {
			try {
				Path autoSavePath = Paths.get(String.format("%s/%s/autosave_turn-%d_%s.xml", AUTOSAVE_DIRECTORY, getAuthoringGameState().getName(), getAuthoringGameState().getTurnNumber(), Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_SS"))));
				saveHistory.push(autoSavePath);
				saveFile(autoSavePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		thingsToUpdate.forEach(e -> Platform.runLater(e::update));
	}

	public void endTurn() {
		sendModifier(GameplayState::endTurn);
	}

	@Override
	public void undo() {
		try {
			saveHistory.pop();
			setGameState(loadFile(saveHistory.pop()));
		} catch (IOException ignored) {
			ignored.printStackTrace();
		}
	}

	@Override
	public void addTemplates(VoogaEntity... templates) {
		System.out.println(templates[0].getClass());
		templateMap.get(templates[0].getClass());
		addTemplatesByCategory(templateMap.get(templates[0].getClass()), templates);
	}
}