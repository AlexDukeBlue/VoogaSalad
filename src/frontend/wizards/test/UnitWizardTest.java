package frontend.wizards.test;

import backend.grid.CoordinateTuple;
import backend.grid.GridPattern;
import backend.util.AuthoringGameState;
import frontend.wizards.UnitWizard;
import javafx.application.Application;
import javafx.stage.Stage;

public class UnitWizardTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		new CoordinateTuple(1, 2, 3);
		@SuppressWarnings("unused")
		GridPattern pattern = GridPattern.HEXAGONAL_ADJACENT;
		UnitWizard newUnitWizard = new UnitWizard(new AuthoringGameState("Test Game State"));
		newUnitWizard.addObserver((observable, object) -> {
			System.out.println("Observable: " + observable.toString());
			System.out.println("Object: " + object.toString());
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
