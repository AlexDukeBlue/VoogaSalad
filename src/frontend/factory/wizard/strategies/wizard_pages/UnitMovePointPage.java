package frontend.factory.wizard.strategies.wizard_pages;

import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.util.IntegerInputRow;
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

	private IntegerInputRow numericInputRow;

	/**
	 * Creates a new instance of UnitMovePointPage
	 * 
	 * @param gameState
	 *            the AuthoringGameState that the WizardPage will use to
	 *            populate its list of terrains
	 * @param descriptionKey
	 *            a String that can be used as a key to a ResourceBundle to set
	 *            the description of the page
	 */
	public UnitMovePointPage(Controller controller, String descriptionKey) {
		super(controller, descriptionKey);
		initialize();
	}

	@Override
	public Region getNode() {
		return numericInputRow.getNode();
	}

	public Integer getValue() {
		return numericInputRow.getValue();
	}

	private void initialize() {
		numericInputRow = new IntegerInputRow(null, getPolyglot().get("Default_UnitMovePoint_Title"),
				new StringBinding() {

					@Override
					protected String computeValue() {
						return "";
					}

				});
		canNextWritable().setValue(true);
	}

}
