package frontend.factory.wizard.wizards.strategies.wizard_pages;

import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.NumericInputRow;
import javafx.scene.layout.Region;

/**
 * The WizardPage for setting the move cost of units across certain types of terrain
 * @author Andreas
 *
 */
public class UnitMovePointPage extends BaseWizardPage {
	private static final String DEFAULT_TITLE = "Default Move Cost:";
	private static final String DEFAULT_DESCRIPTION = "Enter the default amount of movement points it takes to cross the terrain type.";

	private NumericInputRow numericInputRow;

	public UnitMovePointPage(AuthoringGameState gameState) {
		this(DEFAULT_TITLE, gameState);
	}

	public UnitMovePointPage(String title, AuthoringGameState gameState) {
		this(title, DEFAULT_DESCRIPTION, gameState);
	}

	public UnitMovePointPage(String title, String description, AuthoringGameState gameState) {
		super(title, description);
		initialize(gameState);
	}

	private void initialize(AuthoringGameState gameState) {
		numericInputRow = new NumericInputRow(null, DEFAULT_TITLE, "");
		canNextWritable().setValue(true);
	}

	@Override
	public Region getObject() {
		return numericInputRow.getObject();
	}

	public Integer getValue() {
		return numericInputRow.getValue();
	}

}
