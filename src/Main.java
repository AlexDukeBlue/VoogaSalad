import backend.grid.GridPattern;
import frontend.startup.StartupScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
		primaryStage.setOnCloseRequest(event -> System.exit(0));
		primaryStage.show();
	}
}
