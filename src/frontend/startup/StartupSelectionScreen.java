package frontend.startup;

import backend.util.AuthoringGameState;
import controller.CommunicationController;
import controller.Controller;
import frontend.View;
import frontend.wizards.GameWizard;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.concurrent.Callable;

import com.sun.javafx.geom.Shape;

/**
 * The intro screen containing a "create new game" button.
 *
 * @authors Sam, ncp14
 */
public class StartupSelectionScreen extends VBox {

	private ResourceBundle SelectionProperties = ResourceBundle.getBundle("frontend/properties/SelectionProperties");
	private StartupScreen ui;
	//ObservableClient<ImmutableGameState> myClient;
	private Stage stage;

	Color startColor;
	Color endColor;
	ObjectProperty<Color> color;

	public StartupSelectionScreen(Stage stage, StartupScreen ui) { //should have some sort of parameter that is passing the UI
		this.stage = stage;
		this.setUpPane();
		this.ui = ui;
	}

	public void setButtonAnimationColors() {
		startColor = Color.web("#e08090");
		endColor = Color.web("#80e090");
		color = new SimpleObjectProperty<Color>(startColor);
	}

	// This method returns a string that represents the color above as a JavaFX CSS function:
	// -fx-body-color: rgb(r, g, b);
	// with r, g, b integers between 0 and 255
	public StringBinding generateStringBinding() {
		StringBinding cssColorSpec = Bindings.createStringBinding(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return String.format("-fx-body-color: rgb(%d, %d, %d);",
						(int) (256 * color.get().getRed()),
						(int) (256 * color.get().getGreen()),
						(int) (256 * color.get().getBlue()));
			}
		}, color);
		return cssColorSpec;
	}

	public Pane generateShape(Rectangle rotatingRect) {
		final Pane rectHolder = new Pane();
		rectHolder.setMinSize(20, 16);
		rectHolder.setPrefSize(20, 16);
		rectHolder.setMaxSize(20, 16);
		rectHolder.getChildren().add(rotatingRect);
		return rectHolder;
	}

	public RotateTransition generateRotation(Rectangle rotatingRect) {
		final RotateTransition rotate = new RotateTransition(Duration.seconds(1), rotatingRect);
		rotate.setByAngle(360);
		rotate.setCycleCount(Animation.INDEFINITE);
		rotate.setInterpolator(Interpolator.LINEAR);
		return rotate;
	}


	public void setUpPane() {
		
		Button create = new Button(SelectionProperties.getString("Create")) {{
			this.setOnAction(e -> create());
		}};
		
		Button join = new Button(SelectionProperties.getString("Join")) {{
			this.setOnAction(e -> create());
		}};

		/////////********** basic animation idea from https://gist.github.com/james-d/8474941, but heavily refactored and changed by ncp14
		setButtonAnimationColors();
		create.styleProperty().bind(generateStringBinding());
		join.styleProperty().bind(generateStringBinding());

		final Timeline timeline = new Timeline(
				new KeyFrame(Duration.ZERO, new KeyValue(color, startColor)),
				new KeyFrame(Duration.seconds(1), new KeyValue(color, endColor)));

		create.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				timeline.play();
				create();
			}
		});
		
		join.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				timeline.play();
				create();
			}
		});

		// Create a rotating rectangle and set it as the graphic for the button
		final Rectangle rotatingRect = new Rectangle(5, 5, 10, 6);
		rotatingRect.setFill(Color.CORNFLOWERBLUE);
		Pane rectHolder = generateShape(rotatingRect);
		RotateTransition rotate = generateRotation(rotatingRect);
		
		// Create a rotating rectangle and set it as the graphic for the button
	    final Rectangle rotatingRect2 = new Rectangle(5, 5, 10, 6);
	    rotatingRect2.setFill(Color.CORNFLOWERBLUE);
		Pane rectHolder2 = generateShape(rotatingRect2);
		RotateTransition rotate2 = generateRotation(rotatingRect2);

		create.setGraphic(rectHolder);
		join.setGraphic(rectHolder2);

		// make the rectangle rotate when the mouse hovers over the button
		create.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				rotate.play();
			}
		});

		create.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				rotate.stop();
				rotatingRect.setRotate(0);
			}
		});
		
		join.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				rotate2.play();
			}
		});

		join.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				rotate2.stop();
				rotatingRect2.setRotate(0);
			}
		});


//		Button edit = new Button(SelectionProperties.getString("EditGame")){{
//			this.setOnAction(e -> edit());
//		}};

		this.setPadding(new Insets(30, 10, 10, 10));
		this.setSpacing(10);
		this.setMinWidth(450);
		this.setMinHeight(400);
		//this.getChildren().addAll(play, create, edit);
		this.getChildren().add(create);
		this.getChildren().add(join);
	}

	private void play() {
		read("play");
	}

	private void edit() {
		read("load");
	}

	private void create() {
		GameWizard wiz = new GameWizard();
		wiz.show();
		wiz.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				createGame((AuthoringGameState) arg, true);
				stage.close();
			}
		});

	}

	private void createGame(AuthoringGameState state, boolean editable) {
		Controller control = new CommunicationController(System.getProperty("user.name") + "-" + System.currentTimeMillis() % 100, state, null);
		View view = new View(control);
		//myClient.setGameState(state);
		//control.setClient(myClient);
		view.setEditable(editable);
		Stage stage = new Stage();
		Scene scene = new Scene(view.getObject());
		stage.setScene(scene);
		stage.show();

	}

	private void read(String saveOrLoad) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".xml Files", "*.xml"));
		fileChooser.setTitle("Open Resource File");
		Window stage = null;
		File file = fileChooser.showOpenDialog(stage);

		try {

			FileInputStream fileIn = new FileInputStream(file);

			ObjectInputStream in = new ObjectInputStream(fileIn);

			//need to do something with the file

			in.close();

			fileIn.close();

			//this part probs doesn't work
			Region pane = ui.getPrimaryPane();
			((BorderPane) pane).setCenter(new View(null).getObject());

		} catch (IOException i) {

			i.printStackTrace();

			return;

		} catch (NullPointerException e) {
			e.printStackTrace();

			Alert alert = new Alert(AlertType.CONFIRMATION);

			alert.setTitle("No file selected");

//			alert.setGraphic(graphic); //insert DuvallSalad

			if (Objects.equals(saveOrLoad, "save")) {
				alert.setHeaderText("Current game will not save");
			}
			if (Objects.equals(saveOrLoad, "load") || Objects.equals(saveOrLoad, "play")) {
				alert.setHeaderText("Failed to load game");
			}

			alert.setContentText("Would you like to try again?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == new ButtonType("okay")) {
				read(saveOrLoad);
			} else {
				return;

			}
		}
		//		}catch(ClassNotFoundException c) {
		//
		//			System.out.println("Employee class not found");
		//
		//			c.printStackTrace();
		//
		//			return;
		//
		//		}
	}
}