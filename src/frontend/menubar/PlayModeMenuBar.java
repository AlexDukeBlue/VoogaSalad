/**
 * 
 */
package frontend.menubar;

import java.util.ResourceBundle;

import controller.Controller;
import frontend.View;
import frontend.util.ComponentFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * @author Stone Mathers
 * Created 4/19/2017
 */
public class PlayModeMenuBar extends VoogaMenuBar {

	private Menu file, language, theme, view, help, setLanguageItem, setThemeItem;
	private MenuItem saveItem, quitItem, mainMenuItem, editModeItem, detailsItem, statsItem, helpItem, aboutItem;
	private ComponentFactory factory;	
	
	/**
	 * @param view
	 * @param controller
	 */
	public PlayModeMenuBar(View view, Controller controller) {
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

	private void populateMenuBar(){
		
	}
	
}
