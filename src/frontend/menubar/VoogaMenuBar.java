/**
 *
 */
package frontend.menubar;

import backend.unit.Unit;
import backend.util.io.XMLSerializer;
import controller.Controller;
import frontend.View;
import frontend.factory.wizard.WizardFactory;
import frontend.startup.StartupScreen;
import frontend.util.BaseUIManager;
import frontend.util.ComponentFactory;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author Stone Mathers Created 4/18/2017
 */
public class VoogaMenuBar extends BaseUIManager<MenuBar> {

	private Menu file, edit, language, theme, view, help, setLanguageItem, setThemeItem;
	private MenuItem loadItem, saveItem, homeScreenItem, quitItem, newUnitItem, newTerrainItem, newActiveAbilityItem,
			newTriggeredAbilityItem, newInteractionModifierItem, conditionsPaneItem, templatePaneItem, detailsPaneItem,
			statsPaneItem, editModeItem, playModeItem, helpItem, aboutItem;
	private ComponentFactory factory;
	private MenuBar menuBar;
	private View myView;

	public VoogaMenuBar(View view, Controller controller, boolean editable) {
		super(controller);
		myView = view;
		menuBar = new MenuBar();
		menuBar.setUseSystemMenuBar(true);
		factory = new ComponentFactory();
		populateMenuBar();
		setEditable(editable);
		getLanguage().addListener((observable, oldLanguage, newLanguage) -> {
			getObject().getMenus().clear();
			populateMenuBar();
		});
	}

	public void setEditable(boolean editable) {
		loadItem.setDisable(!editable);
		edit.setDisable(!editable);
		conditionsPaneItem.setDisable(!editable);
		templatePaneItem.setDisable(!editable);
		playModeItem.setDisable(!editable);

		editModeItem.setDisable(editable);
	}

	protected void populateMenuBar() {
		initMenuItems();
		initMenus();
	}

	private void initMenuItems() {
		saveItem = factory.getMenuItem(getLanguage().getValue().getString("Save"), e -> save());
		loadItem = factory.getMenuItem(getLanguage().getValue().getString("Load"), e -> load());
		homeScreenItem = factory.getMenuItem("Home Screen", e -> {

			StartupScreen su = new StartupScreen(myView.getStage(), StartupScreen.DEFAULT_WIDTH,
					StartupScreen.DEFAULT_HEIGHT);
			myView.getStage().setScene(new Scene(su.getPrimaryPane()));

		}); // TODO resource file
		quitItem = factory.getMenuItem(getLanguage().getValue().getString("Quit"), e -> System.exit(0));

		newUnitItem = factory.getMenuItem("Create New Unit", e -> create("unit")); // TODO
		// resource
		// file
		newTerrainItem = factory.getMenuItem("Create New Terrain", e -> create("terrain")); // TODO
		// resource
		// file
		newActiveAbilityItem = factory.getMenuItem("Create New Active Ability", e -> create("activeability")); // TODO
		// resource
		// file
		newTriggeredAbilityItem = factory.getMenuItem("Create New Triggered Ability", e -> create("triggeredability")); // TODO
		// resource
		// file
		newInteractionModifierItem = factory.getMenuItem("Create New Interaction Modifier",
				e -> create("interactionmodifier")); // TODO resource file

		setLanguageItem = factory.getMenu(getLanguage().getValue().getString("SetLanguage"));
		getPossibleResourceBundleNamesAndResourceBundles().forEach((name, bundle) -> {
			MenuItem menuItem = new MenuItem(name);
			menuItem.setOnAction(e -> getLanguage().setValue(bundle));
			setLanguageItem.getItems().add(menuItem);
		});

		setThemeItem = factory.getMenu(getLanguage().getValue().getString("SetTheme"));
		getPossibleStyleSheetNamesAndFileNames().forEach((name, fileName) -> {
			MenuItem menuItem = new MenuItem(name);
			menuItem.setOnAction(event -> getStyleSheet().setValue(fileName));
			setThemeItem.getItems().add(menuItem);
		});

		conditionsPaneItem = factory.getMenuItem("Show/Hide Conditions Pane", e -> myView.toggleConditionsPane()); // TODO
		// resource
		// file
		templatePaneItem = factory.getMenuItem("Show/Hide Template Pane", e -> myView.toggleTemplatePane()); // TODO
		// resource
		// file
		detailsPaneItem = factory.getMenuItem("Show/Hide Details Pane", e -> myView.toggleDetailsPane()); // TODO
		// resource
		// file
		statsPaneItem = factory.getMenuItem("Show/Hide Stats Pane", e -> myView.toggleStatsPane()); // TODO
		// resource
		// file
		editModeItem = factory.getMenuItem("Edit Mode", e -> getController().enterAuthoringMode()); // TODO
		// resource
		// file,
		// operate
		// through
		// controller
		playModeItem = factory.getMenuItem("Play Mode", e -> getController().enterGamePlayMode()); // TODO
		// resource
		// file,
		// operate
		// through
		// controller

		helpItem = factory.getMenuItem(getLanguage().getValue().getString("Help"), e -> {
		}); // TODO implement
		aboutItem = factory.getMenuItem("About", e -> {
		}); // TODO implement, resource file
	}

	private void initMenus() {
		file = factory.getMenu(getLanguage().getValue().getString("File"));
		file.getItems().add(loadItem);
		file.getItems().add(saveItem);
		file.getItems().add(homeScreenItem);
		file.getItems().add(quitItem);

		edit = factory.getMenu("Edit"); // TODO resource file
		edit.getItems().add(newUnitItem);
		edit.getItems().add(newTerrainItem);
		edit.getItems().add(newActiveAbilityItem);
		edit.getItems().add(newTriggeredAbilityItem);
		edit.getItems().add(newInteractionModifierItem);

		language = factory.getMenu(getLanguage().getValue().getString("Language"));
		language.getItems().add(setLanguageItem);

		theme = factory.getMenu(getLanguage().getValue().getString("Theme"));
		theme.getItems().add(setThemeItem);

		view = factory.getMenu("View"); // TODO get from resource files
		view.getItems().add(conditionsPaneItem);
		view.getItems().add(templatePaneItem);
		view.getItems().add(detailsPaneItem);
		view.getItems().add(statsPaneItem);
		view.getItems().add(editModeItem);
		view.getItems().add(playModeItem);

		help = factory.getMenu(getLanguage().getValue().getString("Help"));
		help.getItems().add(helpItem);
		help.getItems().add(aboutItem);

		getObject().getMenus().add(file);
		getObject().getMenus().add(edit);
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
			Files.write(Paths.get(file.getPath()),
					((String) new XMLSerializer<>().serialize(getController().getGameState())).getBytes());

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
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".xml Files", "*.xml"));
			getController().setGameState(getController().unserialize(new String(Files.readAllBytes(Paths.get(fileChooser.showOpenDialog(null).getAbsolutePath())))));
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

	private void create(String categoryName) {
		WizardFactory.newWizard(categoryName, getController().getAuthoringGameState()).addObserver((wizard, unit) -> getController().addUnitTemplates((Unit) unit));
	}

	@Override
	public MenuBar getObject() {
		return menuBar;
	}

	public View getView() {
		return myView;

	}
	
	@Override
	public void update(){
		setEditable(getController().getGameState().isAuthoringMode());
	}

}
