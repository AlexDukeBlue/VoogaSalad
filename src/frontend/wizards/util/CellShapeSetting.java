package frontend.wizards.util;

import frontend.util.CellShape;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * SettingNode for the shape of cells in the game
 * @author Stone Mathers
 * Created 4/5/2017
 */
public class CellShapeSetting extends SettingNode {
	
	private ComboBox<String> inputBox;

	public CellShapeSetting() { //TODO Make this constructor take in something to determine language
		super();
	}


	@Override
	public String getData() {
		return inputBox.getValue();
	}


	@Override
	protected HBox fillSettingBox(HBox box) {
		Text type = new Text(getResources().getString("CellShape"));

		
		ObservableList<String> options = FXCollections.observableArrayList(); //TODO: Find a way to do this with a stream
		for(CellShape shape: CellShape.values()){
			options.add(shape.toString());
		}
		
		inputBox = new ComboBox<String>(options);

		box.getChildren().addAll(type, inputBox);
		return box;
	}
	
}
