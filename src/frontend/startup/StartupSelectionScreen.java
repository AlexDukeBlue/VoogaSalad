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
 * The intro screen containing a "create new game" button.
 *
 * @authors Sam, ncp14, Stone Mathers, Dylan Peters
 */
public class StartupSelectionScreen extends BaseUIManager<Node> {
	private static final Collection<String> buttonNames = new ArrayList<>(Arrays.asList("create", "join", "play"));

	private VBox vbox;

	public StartupSelectionScreen(Stage stage) {
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
		vbox.setPadding(new Insets(30, 10, 10, 10));
		vbox.setSpacing(10);
		vbox.setMinWidth(450);
		vbox.setMinHeight(400);
	}

}