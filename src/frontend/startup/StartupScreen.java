package frontend.startup;

import frontend.util.BaseUIManager;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * 
 * StartupScreen creates a page that allows the user to select from a variety of
 * startup options. It uses the StartupSelectionScreen to determine those
 * options.
 * 
 * @author Sam, Dylan Peters
 *
 */
public class StartupScreen extends BaseUIManager<Parent> {
	private LoadingScreen ls;
	private BorderPane primaryPane;

	/**
	 * Creates a new StartupScreen on a new Stage. Sets all values to default.
	 */
	public StartupScreen() {
		this(new Stage());
	}

	/**
	 * Creates a new StartupScreen using the stage passed to it. Sets width and
	 * height to defaults.
	 * 
	 * @param stage
	 *            the stage on which to display the startup screen and the game.
	 */
	public StartupScreen(Stage stage) {
		this(stage, -1, -1);
	}

	/**
	 * Creates a new StartupScreen using the stage passed to it. Sets width and
	 * height to the specified parameters
	 * 
	 * @param stage
	 *            the stage on which to display the startup screen and the game.
	 * @param width
	 *            the width to set the stage to
	 * @param height
	 *            the height to set the stage to
	 */
	public StartupScreen(Stage stage, double width, double height) {
		super(null);
		initPrimaryPane(stage, width, height);
	}

	/**
	 * Allows client code to set the prefWidth of the startupScreen
	 * 
	 * @param width
	 *            the width for the StartupScreen to attempt to be.
	 */
	public void setPrefWidth(double width) {
		primaryPane.setPrefWidth(width);
	}

	/**
	 * Returns the preferred width of the StartupScreen.
	 * 
	 * @return the preferred width of the StartupScreen.
	 */
	public double getPrefWidth() {
		return primaryPane.getPrefWidth();
	}

	/**
	 * Returns the DoubleProperty that specifies the preferred width of the
	 * StartupScreen. This allows other classes to listen for changes or to bind
	 * its width.
	 * 
	 * @return the DoubleProperty that specifies the preferred width of the
	 *         StartupScreen
	 */
	public DoubleProperty prefWidthProperty() {
		return primaryPane.prefWidthProperty();
	}

	/**
	 * Allows client code to set the prefHeight of the startupScreen
	 * 
	 * @param height
	 *            the height for the StartupScreen to attempt to be.
	 */
	public void setPrefHeight(double height) {
		primaryPane.setPrefHeight(height);
	}

	/**
	 * Returns the preferred height of the StartupScreen.
	 * 
	 * @return the preferred height of the StartupScreen.
	 */
	public double getPrefHeight() {
		return primaryPane.getPrefHeight();
	}

	/**
	 * Returns the DoubleProperty that specifies the preferred height of the
	 * StartupScreen. This allows other classes to listen for changes or to bind
	 * its height.
	 * 
	 * @return the DoubleProperty that specifies the preferred height of the
	 *         StartupScreen
	 */
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
				getImg(getResourceBundle().getString("StartupBackgroundImage")), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, null, new BackgroundSize(width, height, false, false, true, true)));
		primaryPane = new BorderPane();
		primaryPane.setBottom(new StartupSelectionScreen(stage, this).getNode());
		primaryPane.setPrefWidth(width);
		primaryPane.setPrefHeight(height);
		primaryPane.setBackground(imgv);
	}
	
	public void initLoadingScreen(){
		double width = primaryPane.getBackground().getImages().get(0).getImage().getWidth();
		double height = primaryPane.getBackground().getImages().get(0).getImage().getHeight();
//		ls = new LoadingScreen(width, height);
		Image img = getImg(getResourceBundle().getString("StartupBackgroundImage"));
//		ls = new LoadingScreen(img.getWidth(), img.getHeight());
//		primaryPane.getChildren().add(ls);
	}
	
	public void removeLoadingScreen(){
		primaryPane.getChildren().remove(ls);
	}

}
