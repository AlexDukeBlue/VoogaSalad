package frontend.wizards.util;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * SettingNode for the Grid Size
 * @author Stone Mathers
 * Created 4/5/2017
 */
public class GridSizeSetting extends SettingNode {

	private TextField widthField;
	private TextField heightField;

	public GridSizeSetting() { //TODO Make this constructor take in something to determine language
		super();
	}

	@Override
	public String getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected HBox fillSettingBox(HBox box) {
		Text type = new Text(getResources().getString("GridSize"));
		heightField = new TextField(getResources().getString("100"));
		Text by = new Text(getResources().getString("X"));
		widthField = new TextField(getResources().getString("100"));
		
		box.getChildren().addAll(type, heightField, by, widthField);
		return box;
	}

}
