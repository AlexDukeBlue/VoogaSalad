package frontend.factory.wizard.strategies.wizard_pages;

import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.util.NumericInputRow;
import frontend.factory.wizard.strategies.wizard_pages.util.TableInputView;
import frontend.factory.wizard.strategies.wizard_pages.util.VerticalTableInputView;
import javafx.scene.layout.Region;

/**
 * NumTurnsPage prompts the user to enter a number of turns that something will
 * last.
 * 
 * @author Dylan Peters
 *
 */
public class NumTurnsPage extends BaseWizardPage {

	private TableInputView table;
	private NumericInputRow input;

	/**
	 * Creates a new instance of NumTurnsPage
	 * 
	 * @param descriptionKey
	 *            a String that can be used as a key to a ResourceBundle to set
	 *            the description of the page
	 */
	public NumTurnsPage(Controller controller, String descriptionKey) {
		super(controller, descriptionKey);
		initialize();
	}

	@Override
	public Region getNode() {
		return table.getNode();
	}

	/**
	 * Returns the value input by the user that describes the number of turns
	 * he/she wants something to last.
	 * 
	 * @return the value input by the user that describes the number of turns
	 *         he/she wants something to last.
	 */
	public int getNumTurns() {
		return input.getValue();
	}

	private void initialize() {
		input = new NumericInputRow(null, getPolyglot().get("NumTurnsPrompt").getValueSafe(),
				getPolyglot().get("NumTurnsLabel").getValueSafe());
		input.setValue(Integer.MAX_VALUE);
		table = new VerticalTableInputView();
		table.getChildren().add(input);
		canNextWritable().setValue(true);
	}

}
