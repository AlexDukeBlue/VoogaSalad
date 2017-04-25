package frontend.util;

import controller.CommunicationController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScriptingDialogTest extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(new ScriptingDialog(new CommunicationController("asdf")).getObject());
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
