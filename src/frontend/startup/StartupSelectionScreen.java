package frontend.startup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import frontend.util.BaseUIManager;
import frontend.util.ButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The StartupSelectionScreen sets up a Node that can be displayed to allow the
 * user to select whether to start a new game, load a previously saved game, or
 * join a game that is currently being hosted.
 *
 * @authors Sam, ncp14, Stone Mathers, Dylan Peters
 */
class StartupSelectionScreen extends BaseUIManager<Node> {
	private static final Collection<String> buttonNames = new ArrayList<>(Arrays.asList("create", "join", "play"));

	private VBox vbox;

	/**
	 * Creates a new instance of the StartupSelectionScreen with a reference to
	 * the Stage object being passed to it.
	 * 
	 * @param stage
	 *            the stage on which the game will be placed.
	 */
	StartupSelectionScreen(Stage stage) {
		initializePane(stage);
	}

	@Override
	public Node getNode() {
		return vbox;
	}

	private void initializePane(Stage stage) {
		vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		StartupDelegate delegate = new ConcreteStartupDelegate();
		buttonNames.stream().forEachOrdered(name -> {
			Button button = ButtonFactory.newSpinningButton();
			button.textProperty().bind(getPolyglot().get(name));
			vbox.getChildren().add(button);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				try {
					delegate.getClass().getDeclaredMethod(name, Stage.class).invoke(delegate, stage);
				} catch (Exception e) {
					vbox.getChildren().remove(button);
				}
			});
		});
		vbox.setPadding(new Insets(Double.parseDouble(getResourceBundle().getString("INSETS_TOP")),
				Double.parseDouble(getResourceBundle().getString("INSETS_RIGHT")),
				Double.parseDouble(getResourceBundle().getString("INSETS_BOTTOM")),
				Double.parseDouble(getResourceBundle().getString("INSETS_LEFT"))));
		vbox.setSpacing(Double.parseDouble(getResourceBundle().getString("SPACING")));
		vbox.setMinWidth(Double.parseDouble(getResourceBundle().getString("MIN_WIDTH")));
		vbox.setMinHeight(Double.parseDouble(getResourceBundle().getString("MIN_HEIGHT")));
	}

}