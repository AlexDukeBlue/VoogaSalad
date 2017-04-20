/**
 * 
 */
package frontend.menubar;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import backend.cell.Terrain;
import backend.unit.Unit;
import backend.util.io.XMLSerializer;
import controller.Controller;
import frontend.View;
import frontend.startup.StartupScreen;
import frontend.util.BaseUIManager;
import frontend.util.ComponentFactory;
import frontend.wizards.TerrainWizard;
import frontend.wizards.UnitWizard;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Window;


/**
 * @author Stone Mathers
 * Created 4/18/2017
 */
public class VoogaMenuBar extends BaseUIManager<MenuBar> {

	private Menu file, edit, language, theme, view, help, setLanguageItem, setThemeItem;
	private MenuItem loadItem, saveItem, homeScreenItem, quitItem, newUnitItem, newTerrainItem,
		newActiveAbilityItem, newTriggeredAbilityItem, newInteractionModifierItem, rulesPaneItem, templatePaneItem, detailsPaneItem, statsPaneItem,
		 editModeItem, playModeItem, helpItem, aboutItem;
	private ComponentFactory factory;
	private MenuBar menuBar;
	private View myView;
	
	public VoogaMenuBar(View view, Controller controller, boolean editable){
		super(controller);
		myView = view;
		menuBar = new MenuBar();
		menuBar.setUseSystemMenuBar(true);
		factory = new ComponentFactory();
		populateMenuBar();
		setEditable(editable);
		getLanguage().addListener((observable, oldLanguage, newLanguage) -> {getObject().getMenus().clear(); populateMenuBar();});
	}
	
	public void setEditable(boolean editable){
		loadItem.setDisable(!editable);
		edit.setDisable(!editable);
		rulesPaneItem.setDisable(!editable);
		templatePaneItem.setDisable(!editable);
		playModeItem.setDisable(!editable);
		
		editModeItem.setDisable(editable);
	}

	protected void populateMenuBar() {
		initMenuItems();
		initMenus();
	}
	
	private void initMenuItems(){
		saveItem = factory.getMenuItem(getLanguage().getValue().getString("Save"), e -> save());
		loadItem = factory.getMenuItem(getLanguage().getValue().getString("Load"), e -> load());
		homeScreenItem = factory.getMenuItem("Home Screen", e -> {

			StartupScreen su = new StartupScreen(myView.getStage(), StartupScreen.DEFAULT_WIDTH, StartupScreen.DEFAULT_HEIGHT);
			myView.getStage().setScene(new Scene(su.getPrimaryPane()));

			}); //TODO resource file
		quitItem = factory.getMenuItem(getLanguage().getValue().getString("Quit"), e -> System.exit(0));
		
		newUnitItem = factory.getMenuItem("Create New Unit", e -> createUnit()); //TODO resource file
		newTerrainItem = factory.getMenuItem("Create New Terrain", e -> createTerrain()); //TODO resource file
		newActiveAbilityItem = factory.getMenuItem("Create New Active Ability", e -> createActiveAbility()); //TODO resource file
		newTriggeredAbilityItem = factory.getMenuItem("Create New Triggered Ability", e -> createTriggeredAbility()); //TODO resource file
		newInteractionModifierItem = factory.getMenuItem("Create New Interaction Modifier", e -> createInteractionModifier()); //TODO resource file
		
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
			menuItem.setOnAction(event -> getStyleSheet().setValue(fileName));
			setThemeItem.getItems().add(menuItem);
		});
		
		rulesPaneItem = factory.getMenuItem("Show/Hide Rules Pane", e -> {}); ////TODO implement, resource file
		templatePaneItem = factory.getMenuItem("Show/Hide Template Pane", e -> {}); ////TODO implement, resource file
		detailsPaneItem = factory.getMenuItem("Show/Hide Details Pane", e -> {}); ////TODO implement, resource file
		statsPaneItem = factory.getMenuItem("Show/Hide Stats Pane", e -> {}); ////TODO implement, resource file
		editModeItem = factory.getMenuItem("Edit Mode", e -> getView().setEditable(true)); //TODO resource file, operate through controller
		playModeItem = factory.getMenuItem("Play Mode", e -> getView().setEditable(false)); //TODO resource file, operate through controller
		
		helpItem = factory.getMenuItem(getLanguage().getValue().getString("Help"), e -> {});  //TODO implement
		aboutItem = factory.getMenuItem("About", e -> {});  //TODO implement, resource file
	}

	private void initMenus(){
		file = factory.getMenu(getLanguage().getValue().getString("File"));
		file.getItems().add(loadItem);
		file.getItems().add(saveItem);
		file.getItems().add(homeScreenItem);
		file.getItems().add(quitItem);
		
		edit = factory.getMenu("Edit"); //TODO resource file
		edit.getItems().add(newUnitItem);
		edit.getItems().add(newTerrainItem);
		edit.getItems().add(newActiveAbilityItem);
		edit.getItems().add(newTriggeredAbilityItem);
		edit.getItems().add(newInteractionModifierItem);
		
		language = factory.getMenu(getLanguage().getValue().getString("Language"));
		language.getItems().add(setLanguageItem);
		
		theme = factory.getMenu(getLanguage().getValue().getString("Theme"));
		theme.getItems().add(setThemeItem);
		
		view = factory.getMenu("View"); //TODO get from resource files
		view.getItems().add(rulesPaneItem);
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

	private void createUnit(){
		UnitWizard wiz = new UnitWizard(getController().getAuthoringGameState());
		wiz.addObserver((wizard, unit) -> getController().addUnitTemplates((Unit) unit));		
	}
	
	private void createTerrain() {
		TerrainWizard wiz = new TerrainWizard(getController().getAuthoringGameState());
		wiz.addObserver((wizard, terrain) -> getController().addTerrainTemplates((Terrain) terrain));
	}
	
	private void createActiveAbility(){
		//TODO 
		//ActiveAbilityWizard wiz = new ActiveAbilityWizard(getController().getAuthoringGameState());
		//wiz.addObserver((wizard, ability) -> getController().addTerrainTemplates((ActiveAbility) ability));
	}
	
	private void createTriggeredAbility(){
		//TODO 
		//TriggeredAbilityWizard wiz = new TriggeredAbilityWizard(getController().getAuthoringGameState());
		//wiz.addObserver((wizard, ability) -> getController().addTerrainTemplates((TriggeredAbility) ability));
	}
	
	private void createInteractionModifier(){
		//TODO 
		//InteractionModifierWizard wiz = new InteractionModifierWizard(getController().getAuthoringGameState());
		//wiz.addObserver((wizard, modifier) -> getController().addTerrainTemplates((InteractionModifier) modifier));
	}
	
	@Override
	public MenuBar getObject() {
		return menuBar;
	}
	
	public View getView(){
		return myView;

	}

}
