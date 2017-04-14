package frontend.wizards.util;

import java.util.ResourceBundle;

import frontend.util.BaseUIManager;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * SettingNode extends the BaseUIManager and is a UI structure used in the creation of
 * many wizard pages used to create objects.
 * @author Stone Mathers
 * Created 4/5/2017
 */
public abstract class SettingNode extends BaseUIManager<Region> {

	public static final double SPACING = 10.0;
	
	private HBox settingBox;
	private String myName;
	private ResourceBundle myResources = ResourceBundle.getBundle("frontend/properties/Wizard");
	//TODO Have constructor take in something to determine language and use that to set ResourceBundle
	
	public SettingNode(){
		settingBox = fillSettingBox(new HBox(SPACING));
	}
	
	public String getName(){
		return myName;
	}
	
	protected ResourceBundle getResources(){
		return myResources;
	}
	
	public abstract String getData();
	
	protected abstract HBox fillSettingBox(HBox box);
	
	@Override
	public Region getObject(){
		return settingBox;
	}
	
}
