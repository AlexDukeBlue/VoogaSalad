package frontend.menubar;

import backend.util.AuthoringGameState;
import backend.util.ReadonlyGameplayState;
import backend.util.io.XMLSerializer;
import controller.CommunicationController;
import controller.Controller;
import frontend.View;
import frontend.util.BaseUIManager;
import frontend.util.ComponentFactory;
import frontend.wizards.GameWizard;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Stone Mathers, Sam Schwaller
 *
 */
public class AuthorMenuBar extends VoogaMenuBar {

	private Menu file, edit, language, theme, view, help, setLanguageItem, setThemeItem;
	private MenuItem newGameItem, loadItem, saveItem, mainMenuItem, quitItem, newUnitItem, newTerrainItem,
		newActiveAbilityItem, newTriggeredAbilityItem, newInteractionModifierItem, rulesPaneItem, templatePaneItem, detailsPaneItem,
		playModeItem, helpItem, aboutItem;
	private ComponentFactory factory;

	public AuthorMenuBar(View view, Controller controller) {
		super(view, controller);
		factory = new ComponentFactory();
		populateMenuBar();
		getLanguage().addListener(new ChangeListener<ResourceBundle>() {
			@Override
			public void changed(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldLanguage,
					ResourceBundle newLanguage) {
				getObject().getMenus().clear();
				populateMenuBar();
			}
		});
	}
	
	public void setEditable(boolean editable){
		saveItem.setDisable(!editable);
		loadItem.setDisable(!editable);
		newGameItem.setDisable(!editable);
	}

	protected void populateMenuBar() {
		initMenuItems();
		initMenus();
	}
	
	private void initMenuItems(){
		newGameItem = factory.getMenuItem(getLanguage().getValue().getString("Create"), e -> create());
		saveItem = factory.getMenuItem(getLanguage().getValue().getString("Save"), e -> save());
		loadItem = factory.getMenuItem(getLanguage().getValue().getString("Load"), e -> load());
		quitItem = factory.getMenuItem(getLanguage().getValue().getString("Quit"), e -> {}); //TODO implement
		
		setLanguageItem = factory.getMenu(getLanguage().getValue().getString("SetLanguage"));
		getPossibleResourceBundleNamesAndResourceBundles().forEach((name, bundle) -> {
			MenuItem menuItem = new MenuItem(name){{
				setOnAction(e -> getLanguage().setValue(bundle));
			}};
			
			setLanguageItem.getItems().add(menuItem);
		});
		
		setThemeItem = factory.getMenu(getLanguage().getValue().getString("SetTheme"));
		getPossibleStyleSheetNamesAndFileNames().forEach((name, fileName) -> {
			MenuItem menuItem = new MenuItem(name);
			menuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					getStyleSheet().setValue(fileName);
				}
			});
			setThemeItem.getItems().add(menuItem);
		});
		
		helpItem = factory.getMenuItem(getLanguage().getValue().getString("Help"), e -> {});  //TODO implement
		
		playModeItem = factory.getMenuItem("Enter Play Mode", e -> getView().setEditable(false)); //TODO get from resource files, operate through controller
	}
	
	private void initMenus(){
		file = factory.getMenu(getLanguage().getValue().getString("File"));
		file.getItems().add(newGameItem);
		file.getItems().add(loadItem);
		file.getItems().add(saveItem);
		file.getItems().add(quitItem);
		
		language = factory.getMenu(getLanguage().getValue().getString("Language"));
		language.getItems().add(setLanguageItem);
		
		theme = factory.getMenu(getLanguage().getValue().getString("Theme"));
		theme.getItems().add(setThemeItem);
		
		view = factory.getMenu("View"); //TODO get from resource files
		view.getItems().add(playModeItem);
		
		help = factory.getMenu(getLanguage().getValue().getString("Help"));
		help.getItems().add(helpItem);
		
		getObject().getMenus().add(file);
		getObject().getMenus().add(language);
		getObject().getMenus().add(theme);
		getObject().getMenus().add(view);
		getObject().getMenus().add(help);
	}

	private void save() {
		try {
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".xml Files", "*.xml"));
			Window ownerWindow = null;
			File file = chooser.showSaveDialog(ownerWindow);
			Files.write(Paths.get(file.getPath()), ((String) new XMLSerializer<>()
					.serialize(getController().getGameState())).getBytes());

		} catch (IOException i) {
			i.printStackTrace();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("No file selected");
			alert.setHeaderText("Current game will not save");
			alert.setContentText("Would you like to try again?");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				save();
			} 
		} catch (NullPointerException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("No file selected");
			alert.setHeaderText("Current game will not save");
			alert.setContentText("Would you like to try again?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				save();
			} 
		}
	}

	private void load() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".xml Files", "*.xml"));
		fileChooser.setTitle("Open Resource File");
		Window stage = null;
		File file = fileChooser.showOpenDialog(stage);

		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);

			// have controller reset GameState using file

			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("No file selected");
			alert.setHeaderText("Failed to load game");
			alert.setContentText("Would you like to try again?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				load();
			}
		}
	}

	private void create() {
		GameWizard wiz = new GameWizard();
		wiz.show();
		wiz.addObserver((o, arg) -> createGame((AuthoringGameState) arg, true));
	}

	private void createGame(ReadonlyGameplayState state, boolean editable) {
		Controller control = new CommunicationController(state, null);
		View view = new View(control);
		view.setEditable(editable);
		Stage stage = new Stage();
		Scene scene = new Scene(view.getObject());
		stage.setScene(scene);
		stage.show();

	}

	@Override
	public void update() {
		//Does not change dependent on state
	}

}
