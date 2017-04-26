package frontend.factory.wizard.wizards.strategies.wizard_pages;

import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class ColorPage extends BaseWizardPage{
	
	private ColorPicker colorPicker;
	private BorderPane borderPane;
	
	public ColorPage() {
		colorPicker = new ColorPicker();
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		Label prompt = new Label();
		prompt.textProperty().bind(getPolyglot().get("ColorPrompt"));
		hbox.getChildren().addAll(prompt,colorPicker);
		borderPane = new BorderPane(hbox);
		canNextWritable().setValue(true);
	}

	@Override
	public Region getObject() {
		return borderPane;
	}
	
	public String getColorString(){
		return colorPicker.getValue().toString().replaceAll("0x", "").replaceAll("#", "");
	}

}