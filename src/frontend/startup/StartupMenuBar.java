package frontend.startup;

import frontend.View;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class StartupMenuBar extends VBox {
	private ResourceBundle SelectionProperties = ResourceBundle.getBundle("frontend/properties/SelectionProperties");

	Pane selectionPane = new Pane();

	MenuBar menuBar;
	private MenuItem playItem, saveItem, loadItem, newGameItem, editItem;
	Menu fileMenu, helpMenu;
	StartupScreen ui;

	public StartupMenuBar(StartupScreen ui) {
		this.ui = ui;
		this.initMenu();
	}

	private void initMenu() {
		this.playItem = new MenuItem(SelectionProperties.getString("Play")) {{
			setOnAction(e -> play());
		}};

		this.saveItem = new MenuItem(SelectionProperties.getString("Save")) {{
			setOnAction(e -> save());
		}};

		this.editItem = new MenuItem(SelectionProperties.getString("Edit")) {{
			setOnAction(e -> edit());
		}};

		this.loadItem = new MenuItem(SelectionProperties.getString("EditGame")) {{
			setOnAction(e -> edit());
		}};

		this.newGameItem = new MenuItem(SelectionProperties.getString("Create")) {{
			setOnAction(e -> newGame());
		}};

		this.helpMenu = new Menu(SelectionProperties.getString("Help")) {{
		}};

		this.fileMenu = new Menu(SelectionProperties.getString("File")) {{
			getItems().addAll(playItem, saveItem, editItem, loadItem, newGameItem);
		}};

		this.menuBar = new MenuBar() {{
			getMenus().addAll(fileMenu, helpMenu);
		}};

		this.getChildren().addAll(menuBar);

	}

	private void play() {
		System.out.println("you can load and save files, but it won't do anything");
		read("play");

	}

	private void save() {
		System.out.println("you can load and save files, but it won't do anything");
		try {
			System.out.println("here");
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".xml Files", "*.xml"));
			Window ownerWindow = null;
			File file = chooser.showSaveDialog(ownerWindow);
			FileOutputStream fileOut =
					new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(new Object());  //need to pass in an object
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + file);

		} catch (IOException i) {
			i.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("No file selected");
			//			alert.setGraphic(graphic); //insert DuvallSalad
			alert.setHeaderText("Current game will not save");
			alert.setContentText("Would you like to try again?");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == new ButtonType("okay")) {
				save();
			} else {
				return;
			}
		}
	}

	private void edit() {

	}

	private void load() {
		read("load");
	}

	private void newGame() {
		System.out.println("you clicked New Game, which means you are ahead of me");
	}

	private void read(String saveOrLoad) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".xml Files", "*.xml"));
		fileChooser.setTitle("Open Resource File");
		Window stage = null;
		File file = fileChooser.showOpenDialog(stage);
		System.out.println(saveOrLoad);
		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);

			//need to do something with the file

			in.close();
			fileIn.close();

			//this part probs doesn't work
			Region pane = ui.getPrimaryPane();
			((BorderPane) pane).setCenter(new View(null, null).getObject());

		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (NullPointerException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("No file selected");
			
			/*
			 * failed attempt to set DuvallSalad as graphic
			 */
//			ImageView graphic = new ImageView(new Image("frontend/properties/DuvallSalad.png"));
//			graphic.setScaleX(.25);
//			graphic.setScaleY(.25);
//			
//			alert.setGraphic(graphic); //insert DuvallSalad


				alert.setHeaderText("Failed to load game");
			

			alert.setContentText("Would you like to try again?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == new ButtonType("okay")) {
				read(saveOrLoad);
			} else {
				return;

			}
			
			//changing something for an example
		}
	}
}

