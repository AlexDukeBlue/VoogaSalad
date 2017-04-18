package frontend.menubar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

class VoogaMenuBarTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane bp = new BorderPane();
		VoogaMenuBar voogaMenuBar = new VoogaMenuBar();
		bp.setTop(voogaMenuBar.getObject());
		primaryStage.setScene(new Scene(bp));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
