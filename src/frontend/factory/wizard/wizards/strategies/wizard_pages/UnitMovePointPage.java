package frontend.factory.wizard.wizards.strategies.wizard_pages;

import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.NumericInputRow;
import javafx.beans.binding.StringBinding;
import javafx.scene.layout.Region;

/**
 * The WizardPage for setting the move cost of units across certain types of
 * terrain
 * 
 * @author Andreas
 *
 */
public class UnitMovePointPage extends BaseWizardPage {

	private NumericInputRow numericInputRow;

	public UnitMovePointPage(AuthoringGameState gameState, String descriptionKey) {
		super(descriptionKey);
		initialize(gameState);
	}

	private void initialize(AuthoringGameState gameState) {
		numericInputRow = new NumericInputRow(null, getPolyglot().get("Default_UnitMovePoint_Title"),
				new StringBinding() {

					@Override
					protected String computeValue() {
						return "";
					}

				});
		canNextWritable().setValue(true);
	}

	@Override
	public Region getNode() {
		return numericInputRow.getNode();
	}

	public Integer getValue() {
		return numericInputRow.getValue();
	}

}
