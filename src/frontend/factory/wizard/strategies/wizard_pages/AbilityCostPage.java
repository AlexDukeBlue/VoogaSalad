package frontend.factory.wizard.strategies.wizard_pages;

import controller.Controller;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * @author th174
 */
public class AbilityCostPage extends BaseWizardPage {
	private TextField costField;
	private double cost;


	public AbilityCostPage(Controller controller) {
		super(controller, "AbilityCost");
		costField = new TextField("1");
		costField.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				cost = Double.parseDouble(newValue);
				canNextWritable().setValue(true);
			} catch (NumberFormatException e) {
				canNextWritable().setValue(false);
			}
		});
	}

	@Override
	public Region getNode() {
		return new Pane(costField);
	}

	public double getCost() {
		return cost;
	}
}
