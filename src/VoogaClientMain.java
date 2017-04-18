import backend.cell.Terrain;
import backend.grid.GameBoard;
import backend.grid.GridPattern;
import backend.grid.ModifiableGameBoard;
import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.ReadonlyGameplayState;
import backend.util.io.XMLSerializer;
import controller.Controller;
import frontend.View;
import frontend.util.ChatLogView;
import frontend.util.Updatable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import util.net.Modifier;
import util.net.ObservableClient;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

//import frontend.startup.StartupScreen;

/**
 * @author Created by th174 on 4/4/2017.
 */
public class VoogaClientMain extends Application {

	public static final int PORT = 10023;
	//	public static final String HOST = ObservableClient.LOCALHOST;
	public static final String HOST = "25.4.129.184";
	public static final int TIMEOUT = 20;
	private ObservableClient<GameplayState> client;
	private GameplayState gameplayState;
	private ChatLogView chatLogView;

	public static void main(String[] args) throws IOException, InterruptedException {
		@SuppressWarnings("unused")
		GridPattern pattern = GridPattern.HEXAGONAL_ADJACENT;
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle(ResourceBundle.getBundle("resources/Selections", Locale.getDefault()).getString("Title"));
		String name = System.getProperty("user.name");
		XMLSerializer<GameplayState> serializer = new XMLSerializer<>();
		client = new ObservableClient<>(HOST, PORT, serializer, serializer, Duration.ofSeconds(TIMEOUT));
		client.addListener(state -> {
			try {
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
		chatLogView = new ChatLogView(name, new Controller() {
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
			public void setGameState(ReadonlyGameplayState newGameState) {

			}

			@Override
			public ModifiableGameBoard getModifiableCells() {
				return null;
			}

			@Override
			public <U extends ReadonlyGameplayState> void sendModifier(Modifier<U> modifier) {
				client.addToOutbox((Modifier<GameplayState>) modifier);
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
		Scene scene = new Scene(chatLogView.getObject(), 700, 700, new ImagePattern(View.getImg("resources/images/splash.png")));
		scene.getStylesheets().add("resources/styles/notheme.css");
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.show();
	}
}
