package frontend.startup;

import frontend.View;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StartupScreen {
	public static final double DEFAULT_WIDTH = 700;
	public static final double DEFAULT_HEIGHT = 700;

	private BorderPane primaryPane;
	private double width, height;
	private StartupSelectionScreen selectionScreen;
	//private ObservableClient<ImmutableGameState> myClient;
	private Stage stage;

	public StartupScreen() {
		this(new Stage(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
	}

	public StartupScreen(Stage stage, double width, double height) {
		this.stage = stage;
		this.width = width;
		this.height = height;
		this.primaryPane = initPrimaryPane();
	}

	private BorderPane initPrimaryPane() {
		this.selectionScreen = new StartupSelectionScreen(stage, this);
		BackgroundImage bi = new BackgroundImage(View.getImg("frontend/properties/Screen Shot 2017-04-07 at 3.22.00 PM.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, new BackgroundSize(width, height, false, false, true, true));
		Background imgv = new Background(bi);
		this.primaryPane = new BorderPane();
		primaryPane.setMinSize(width, height);
		primaryPane.setBottom(selectionScreen);
		selectionScreen.setAlignment(Pos.CENTER);
		primaryPane.setBackground(imgv);
		return primaryPane;
	}

	public BorderPane getPrimaryPane() {
		return primaryPane;
	}

}
