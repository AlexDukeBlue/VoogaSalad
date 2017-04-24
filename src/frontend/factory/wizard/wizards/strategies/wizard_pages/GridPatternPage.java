package frontend.factory.wizard.wizards.strategies.wizard_pages;

import backend.grid.GridPattern;
import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.NumericInputRow;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.WizardGrid;
import javafx.beans.binding.StringBinding;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GridPatternPage extends BaseWizardPage {
	private static final Double PREF_CELL_WIDTH = 30.0;
	private static final Double PREF_CELL_HEIGHT = 30.0;

	// private static final String DEFAULT_TITLE = "Set GridPattern";
	// private static final String DEFAULT_DESCRIPTION = "Choose the GridPattern
	// for the Unit you are making.";

	private VBox vb;
	private WizardGrid grid;
	private NumericInputRow width, height;

	public GridPatternPage(AuthoringGameState gameState) {
		this(new StringBinding() {
			@Override
			protected String computeValue() {
				return "";
			}
		}, gameState);
	}

	public GridPatternPage(StringBinding title, AuthoringGameState gameState) {
		this(title, new StringBinding() {
			@Override
			protected String computeValue() {
				return "";
			}
		}, gameState);
	}

	public GridPatternPage(StringBinding title, StringBinding description, AuthoringGameState gameState) {
		super();
		this.setTitle(title);
		this.setDescription(description);
		initialize(gameState);
	}

	private void initialize(AuthoringGameState gameState) {
		// vb = new VBox();
		// width = new NumericInputRow(null, "Enter the width of the grid
		// pattern: ", "Cells");
		// height = new NumericInputRow(null, "Enter the height of the grid
		// pattern: ", "Cells");
		// Button submit = new Button();
		// vb.getChildren().addAll(width.getObject(), height.getObject(),
		// submit);
		// submit.textProperty().bind(getPolyglot().get("Submit"));
		// submit.setOnMouseClicked(event -> {
		// vb.getChildren().clear();
		// vb.getChildren().addAll(width.getObject(), height.getObject(),
		// submit);
		// grid = new WizardGrid(gameState);
		// vb.getChildren().add(grid.getObject());
		// });
		grid = new WizardGrid(gameState);
		canNextWritable().setValue(true); // line change here
	}

	@Override
	public Region getObject() {
		return grid.getObject();
	}

	public GridPattern getGridPattern() {
		return grid.getGridPattern();
	}

}
