package frontend.menubar;

import frontend.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

class VoogaMenuBarTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane bp = new BorderPane();
		View view = new View(null);
		AuthorMenuBar voogaMenuBar = new AuthorMenuBar(view, view.getController());
		bp.setTop(voogaMenuBar.getObject());
		primaryStage.setScene(new Scene(bp));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
