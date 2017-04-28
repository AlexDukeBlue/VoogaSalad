package frontend.startup;

import frontend.View;
import frontend.util.BaseUIManager;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * 
 * @author Sam, Dylan Peters
 *
 */
public class StartupScreen extends BaseUIManager<Parent> {

	private BorderPane primaryPane;

	public StartupScreen() {
		this(new Stage());
	}

	public StartupScreen(Stage stage) {
		this(stage, -1, -1);
	}

	public StartupScreen(Stage stage, double width, double height) {
		initPrimaryPane(stage, width, height);
	}

	public void setPrefWidth(double width) {
		primaryPane.setPrefWidth(width);
	}

	public double getPrefWidth() {
		return primaryPane.getPrefWidth();
	}

	public DoubleProperty prefWidthProperty() {
		return primaryPane.prefWidthProperty();
	}

	public void setPrefHeight(double height) {
		primaryPane.setPrefHeight(height);
	}

	public double getPrefHeight() {
		return primaryPane.getPrefHeight();
	}

	public DoubleProperty prefHeightProperty() {
		return primaryPane.prefHeightProperty();
	}

	@Override
	public Parent getNode() {
		return primaryPane;
	}

	private void initPrimaryPane(Stage stage, double width, double height) {
		if (width <= 0) {
			width = Double.parseDouble(getResourceBundle().getString("DefaultStartupWidth"));
		}
		if (height <= 0) {
			height = Double.parseDouble(getResourceBundle().getString("DefaultStartupHeight"));
		}
		Background imgv = new Background(new BackgroundImage(
				View.getImg(getResourceBundle().getString("StartupBackgroundImage")), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, null, new BackgroundSize(width, height, false, false, true, true)));
		primaryPane = new BorderPane();
		primaryPane.setBottom(new StartupSelectionScreen(stage).getNode());
		primaryPane.setPrefWidth(width);
		primaryPane.setPrefHeight(height);
		primaryPane.setBackground(imgv);
	}
}
