package frontend.factory.wizard.wizards.strategies.wizard_pages;

import java.util.Map;

import javafx.beans.binding.StringBinding;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class GridPatternPage extends BaseWizardPage{

	//private static final String DEFAULT_TITLE = "Set GridPattern";
	//private static final String DEFAULT_DESCRIPTION = "Choose the GridPattern for the Unit you are making.";
	
	private VBox vb;
	private WizardGrid grid;
	TextField textField;
	
	public GridPatternPage() {
		this(new StringBinding() {

			@Override
			protected String computeValue() {
				return "";
			}
			
		});
	}

	public GridPatternPage(StringBinding title) {
		this(title, new StringBinding() {

			@Override
			protected String computeValue() {
				return "";
			}
			
		});
	}

	public GridPatternPage(StringBinding title, StringBinding description) {
		super();
		this.setTitle(title);
		this.setDescription(description);
		initialize();
	}
	
	private void initialize() {
		vb = new VBox();
		textField = new TextField();
		textField.promptTextProperty().bind(getPolyglot().get("Grid_Pattern_Prompt"));;
		Button submit = new Button();
		submit.textProperty().bind(getPolyglot().get("Submit"));
		submit.setOnMouseClicked(event -> {if(!textField.getText().equals(null)) grid = new WizardGrid(Integer.parseInt(textField.getText()), vb); vb.getChildren().add(grid.getPane());});
		vb.getChildren().add(new HBox(textField, submit));
		canNextWritable().setValue(true); //line change here
	}

	@Override
	public Region getObject() {
		return vb;
	}

}
