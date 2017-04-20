import backend.grid.GridPattern;
import backend.player.Player;
import backend.util.GameplayState;
import backend.util.io.XMLSerializer;
import frontend.View;
import frontend.util.ChatLogView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import util.net.ObservableClient;

import java.io.IOException;
import java.time.Duration;
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
		Scene scene = new Scene(chatLogView.getObject(), 700, 700, new ImagePattern(View.getImg("resources/images/splash.png")));
		scene.getStylesheets().add("resources/styles/notheme.css");
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.show();
	}
}
