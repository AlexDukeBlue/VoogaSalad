package frontend.util;

import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;

/**
 * @author Alexander Zapata
 */
public class ComponentFactory {

	private File initialDirectory;
	private String oopsURL;

	/**
	 * Returns a button with the appropriate name and EventHandler<ActionEvent>
	 *
	 * @param name
	 * @param eventHandler
	 * @return Button
	 */
	public Button getButton(String name, EventHandler<ActionEvent> eventHandler) {
		Button button = new Button(name);
		button.setOnAction(eventHandler);
		return button;
	}

	/**
	 * Returns a MenuItem with the appropriate name and EventHandler<ActionEvent>
	 *
	 * @param name
	 * @param eventHandler
	 * @return MenuItem
	 */
	public MenuItem getMenuItem(String name, EventHandler<ActionEvent> eventHandler) {
		return getMenuItem(new StringBinding() {
			@Override
			protected String computeValue() {
				return name;
			}
		}, eventHandler);
	}
	
	public MenuItem getMenuItem(StringBinding name, EventHandler<ActionEvent> eventHandler){
		MenuItem item = new MenuItem();
		item.textProperty().bind(name);
		item.setOnAction(eventHandler);
		return item;
	}

	/**
	 * Returns a menu with the Appropriate name.
	 *
	 * @param name
	 * @return Menu
	 */
	public Menu getMenu(String name) {
		return getMenu(new StringBinding() {
			@Override
			protected String computeValue() {
				return name;
			}
		});
	}
	
	public Menu getMenu(StringBinding name){
		Menu menu = new Menu();
		menu.textProperty().bind(name);
		return menu;
	}

	/**
	 * Returns a FileChooser Dialog Box.
	 *
	 * @return FileChooser
	 */
	public FileChooser getFileChooser() {
		FileChooser myChooser = new FileChooser();
		myChooser.setInitialDirectory(initialDirectory);
		myChooser.getExtensionFilters().setAll(new ExtensionFilter("Choose an appropriate XML file.", "*.XML"));
		return myChooser;
	}

}

