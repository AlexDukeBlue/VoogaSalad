package frontend.util;

import frontend.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.net.URL;

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
		MenuItem item = new MenuItem(name);
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
		return new Menu(name);
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

	/**
	 * Returns an image of size x,y with the image from the given URLString.
	 * If invalid, returns an "oopsImage" of pre-set URLString
	 *
	 * @param imageURL
	 * @param x
	 * @param y
	 * @return Image
	 */
	public Image getURLImage(String imageURL, int x, int y) {
		Image img = null;
		try {
			URL url = new URL(imageURL);
			img = (Image) url.getContent();
			url.openConnection();
			//don't create images, use View.getImg
			img = new Image(imageURL, x, y, true, false);
		} catch (Exception e) {
			return View.getImg(oopsURL);
//			System.out.println("No Image");
		}
		return img;
	}
}

