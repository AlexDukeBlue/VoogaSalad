package frontend.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

import backend.util.AuthoringGameState;
import controller.CommunicationController;
import controller.Controller;
import frontend.View;
import frontend.wizards.GameWizard;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StartupSelectionScreen extends VBox {

	private ResourceBundle SelectionProperties = ResourceBundle.getBundle("frontend/properties/SelectionProperties");
	private StartupScreen ui;
	//ObservableClient<ImmutableGameState> myClient;
	private Stage stage;

	public StartupSelectionScreen(Stage stage, StartupScreen ui) { //should have some sort of parameter that is passing the UI
		this.stage = stage;
		this.setUpPane();
		this.ui = ui;
	}

	public void setUpPane() {

//		System.out.println("setUpPane");
//		Button play = new Button(SelectionProperties.getString("Play")){{
//			this.setOnAction(e -> play());
//		}};

		Button create = new Button(SelectionProperties.getString("Create")) {{
			this.setOnAction(e -> create());
		}};

//		Button edit = new Button(SelectionProperties.getString("EditGame")){{
//			this.setOnAction(e -> edit());
//		}};

		this.setPadding(new Insets(30, 10, 10, 10));
		this.setSpacing(10);
		this.setMinWidth(450);
		this.setMinHeight(400);
		//this.getChildren().addAll(play, create, edit);
		this.getChildren().add(create);
	}

	private void play() {
		read("play");
	}

	private void edit() {
		read("load");
	}

	private void create() {
		GameWizard wiz = new GameWizard();
		wiz.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				createGame((AuthoringGameState) arg, true);
				stage.close();
			}
		});

	}

	private void createGame(AuthoringGameState state, boolean editable) {
		Controller control = new CommunicationController(state, null);
		View view = new View(control);
		control.addToUpdated(view);
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