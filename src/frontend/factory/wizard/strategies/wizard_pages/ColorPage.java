package frontend.factory.wizard.strategies.wizard_pages;

import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * ColorPage is a WizardPage that allows the user to select a color to apply to
 * a VoogaEntity. This color can be used for anything: a team color, the
 * highlighting color for units, the color of a terrain, etc.
 * 
 * @author Dylan Peters
 *
 */
public class ColorPage extends BaseWizardPage {

	private ColorPicker colorPicker;
	private BorderPane borderPane;

	/**
	 * Creates a new instance of ColorPage.
	 * 
	 * @param descriptionKey
	 *            a String that can be used as a key to a ResourceBundle to set
	 *            the description of the page
	 */
	public ColorPage(String descriptionKey) {
		super(descriptionKey);
		colorPicker = new ColorPicker();
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		Label prompt = new Label();
		prompt.textProperty().bind(getPolyglot().get("ColorPrompt"));
		hbox.getChildren().addAll(prompt, colorPicker);
		borderPane = new BorderPane(hbox);
		canNextWritable().setValue(true);
	}

	@Override
	public Region getNode() {
		return borderPane;
	}

	/**
	 * Returns a string describing the color selected by the user. The string is
	 * returned in rgb-hexadecimal format.
	 * 
	 * @return a string describing the color selected by the user. The string is
	 *         returned in rgb-hexadecimal format.
	 */
	public String getColorString() {
		return colorPicker.getValue().toString().replaceAll("0x", "").replaceAll("#", "");
	}

}
