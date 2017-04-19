/**
 * 
 */
package frontend.menubar;


import controller.Controller;
import frontend.View;
import frontend.util.BaseUIManager;
import javafx.scene.control.MenuBar;


/**
 * @author Stone Mathers
 * Created 4/18/2017
 */
public abstract class VoogaMenuBar extends BaseUIManager<MenuBar> {

	private MenuBar menuBar;
	private View myView;
	
	public VoogaMenuBar(View view, Controller controller){
		super(controller);
		myView = view;
		menuBar = new MenuBar();
		menuBar.setUseSystemMenuBar(true); //Because it looks badass as fuck -Timmy
	}
	
	@Override
	public MenuBar getObject() {
		return menuBar;
	}
	
	public View getView(){
		return myView;

	}

}
