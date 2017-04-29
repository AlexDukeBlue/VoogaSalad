package frontend.factory.wizard;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import backend.util.io.XMLSerializer;
import frontend.util.BaseUIManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

/**
 * The Wizard Menu Bar produces a menu bar object with the items necessary to
 * run a Wizard that can create any object. Some of its possible functions
 * include: loading an object from a file instead of having to go through the
 * whole wizard, changing the Wizard's language, changing the wizard's
 * StyleSheet/theme.
 * 
 * @author Dylan Peters
 *
 * @param <T>
 *            the object type of the wizard; this is the type of object that the
 *            wizard is attempting to create.
 */
class WizardMenuBar<T> extends BaseUIManager<MenuBar> {

	private MenuBar menuBar;
	private T objectToReturn;

	/**
	 * Creates a new instance of WizardMenuBar. Sets all values to defaults.
	 */
	WizardMenuBar() {
		menuBar = new MenuBar();
		menuBar.setUseSystemMenuBar(Boolean.parseBoolean(getResourceBundle().getString("Use_System_Menu_Bar")));
		Menu file = new Menu();
		file.textProperty().bind(getPolyglot().get("File"));
		MenuItem load = new MenuItem();
		load.textProperty().bind(getPolyglot().get("Load"));

		file.getItems().add(load);
		menuBar.getMenus().add(file);

		load.setOnAction(event -> {
			load();
		});
	}

	Object finish() {
		return objectToReturn;
	}

	@Override
	public MenuBar getNode() {
		return menuBar;
	}

	private void load() {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".xml Files", "*.xml"));
			objectToReturn = (T) (new XMLSerializer<>().unserialize(
					new String(Files.readAllBytes(Paths.get(fileChooser.showOpenDialog(null).getAbsolutePath())))));
			setChanged();
			notifyObservers();
			clearChanged();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.titleProperty().bind(getPolyglot().get("CouldNotLoad"));
			alert.headerTextProperty().bind(getPolyglot().get("FailedToLoad"));
			alert.contentTextProperty().bind(getPolyglot().get("TryAgain"));
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				load();
			}
		}
	}

}
