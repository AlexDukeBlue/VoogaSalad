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
		StartupScreen starter = new StartupScreen(primaryStage, 700.0, 700.0);
		primaryStage.setScene(new Scene(starter.getPrimaryPane()));
		primaryStage.setResizable(true);
		primaryStage.show();
	}
}
