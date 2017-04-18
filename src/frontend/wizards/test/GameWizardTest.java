package frontend.wizards.test;

import backend.grid.CoordinateTuple;
import backend.grid.GridPattern;
import frontend.wizards.GameWizard;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameWizardTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		new CoordinateTuple(1, 2, 3);
		@SuppressWarnings("unused")
		GridPattern pattern = GridPattern.HEXAGONAL_ADJACENT;
		GameWizard newGameWizard = new GameWizard();
		newGameWizard.addObserver((observable, object) -> {
			System.out.println("Observable: " + observable.toString());
			System.out.println("Object: " + object.toString());
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
