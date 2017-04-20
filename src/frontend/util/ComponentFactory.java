package frontend.util;

import java.io.File;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ComponentFactory {

	private File initialDirectory;
	private String oopsURL;

	public Button getButton(String name, EventHandler<ActionEvent> eventHandler) {
		return new Button(name) {{setOnAction(eventHandler);}};
	}
	
	public MenuItem getMenuItem(String name, EventHandler<ActionEvent> eventHandler){
		return new MenuItem(name) {{setOnAction(eventHandler);}};
	}
	
	public Menu getMenu(String name){
		return new Menu(name);
	}

	public FileChooser getFileChooser() {
		FileChooser myChooser = new FileChooser();
		myChooser.setInitialDirectory(initialDirectory);
		myChooser.getExtensionFilters().setAll(new ExtensionFilter("Choose an appropriate XML file.", "*.XML"));
		return myChooser;
	}
  
	public Image getURLImage(String imageURL, int x, int y){
		Image img = null;
		try{
			URL url = new URL(imageURL);
			img = (Image) url.getContent();
			url.openConnection();
			img = new Image(imageURL, x, y, true, false);
		}catch (Exception e){
			img = new Image(oopsURL);
			System.out.println("No Image");
		}
		return img;
	}
}

