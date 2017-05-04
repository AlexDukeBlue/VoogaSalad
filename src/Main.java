import backend.grid.GridPattern;
import frontend.startup.StartupScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		@SuppressWarnings("unused")
		GridPattern pattern = GridPattern.HEXAGONAL_ADJACENT;
		primaryStage.setScene(new Scene(new StartupScreen(primaryStage).getNode()));
		primaryStage.setResizable(true);
		primaryStage.setOnCloseRequest(event -> {
			Arrays.stream(new File("data/scripts").listFiles()).forEach(e -> {
				try {
					Files.deleteIfExists(Paths.get(e.getPath()));
				} catch (IOException e1) {
				}
			});
			System.exit(0);
		});
		primaryStage.show();
	}
}
