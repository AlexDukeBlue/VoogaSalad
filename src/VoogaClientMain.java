import backend.cell.Terrain;
import backend.grid.GameBoard;
import backend.grid.ModifiableGameBoard;
import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.ReadonlyGameplayState;
import backend.util.io.XMLSerializer;
import controller.Controller;
import frontend.util.ChatLogView;
import frontend.util.Updatable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import util.net.Modifier;
import util.net.ObservableClient;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

//import frontend.startup.StartupScreen;

/**
 * @author Created by th174 on 4/4/2017.
 */
public class VoogaClientMain extends Application {
	public static final int PORT = 10023;
	public static final String HOST = ObservableClient.LOCALHOST;
	public static final int TIMEOUT = 20;
	public static final String CHATBOX = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n------------TEST GAME STATE CHAT LOG------------\n\n%s\n\n >>  ";
	private ObservableClient<GameplayState> client;
	private GameplayState gameplayState;
	private ChatLogView chatLogView;

	public static void main(String[] args) throws IOException, InterruptedException {
		launch(args);
//		Scanner stdin = new Scanner(System.in);
//		String pName = name;
//		while (client.isActive()) {
//			String input = stdin.nextLine();
//			client.addToOutbox(state -> state.messageAll(input, state.getPlayerByName(pName)));
//		}
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle(ResourceBundle.getBundle("resources/Selections", Locale.getDefault()).getString("Title"));
		String name = System.getProperty("user.name") + new Random().nextInt(10);
		XMLSerializer<GameplayState> serializer = new XMLSerializer<>();
		client = new ObservableClient<>(HOST, PORT, serializer, serializer, Duration.ofSeconds(TIMEOUT));
		client.addListener(state -> {
			try {
				System.out.printf(CHATBOX, state.getPlayerByName(name).getChatLog().parallelStream().map(Object::toString).collect(Collectors.joining("\n")));
				gameplayState = state;
				chatLogView.update();
			} catch (NullPointerException e) {
			}
		});
		Executors.newSingleThreadExecutor().submit(client);
		client.addToOutbox(state -> {
			state.addPlayer(new Player(name, "It's me!", ""));
			return state;
		});
		chatLogView = new ChatLogView(name, new Controller<GameplayState>() {
			@Override
			public GameBoard getGrid() {
				return null;
			}

			@Override
			public ReadonlyGameplayState getReadOnlyGameState() {
				return null;
			}

			@Override
			public AuthoringGameState getAuthoringGameState() {
				return null;
			}

			@Override
			public GameplayState getGameState() {
				return gameplayState;
			}

			@Override
			public ImmutablePlayer getPlayer(String name) {
				return null;
			}

			@Override
			public void setGameState(GameplayState newGameState) {

			}

			@Override
			public ModifiableGameBoard getModifiableCells() {
				return null;
			}

			@Override
			public void sendModifier(Modifier<GameplayState> modifier) {
				client.addToOutbox(modifier);
				System.out.println(name);
			}

			@Override
			public Collection<? extends Unit> getUnits() {
				return null;
			}

			@Override
			public Collection<? extends Terrain> getTerrains() {
				return null;
			}

			@Override
			public Collection<? extends Unit> getUnitTemplates() {
				return null;
			}

			@Override
			public Collection<? extends Terrain> getTerrainTemplates() {
				return null;
			}

			@Override
			public void addToUpdated(Updatable objectToUpdate) {

			}

			@Override
			public void removeFromUpdated(Updatable objectToUpdate) {

			}
		});
		Scene scene = new Scene(chatLogView.getObject(), 500, 500, new ImagePattern(new Image("resources/images/splash.png")));
		scene.getStylesheets().add("resources/styles/notheme.css");
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.show();
	}
}
