package frontend.wizards;

import backend.util.AuthoringGameState;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UnitWizardTestMain extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		UnitWizard sup = new UnitWizard(new AuthoringGameState("Name"));
		Scene disScene = new Scene(sup.getObject().getParent(), 600, 600);
		primaryStage.setScene(disScene);
	}

}
