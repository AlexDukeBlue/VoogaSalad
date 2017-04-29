package frontend.factory.wizard.strategies.wizard_pages;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * WizardUnsupportedPage is used to tell the user that he/she has attempted to
 * create a wizard that cannot be made.
 * 
 * @author Dylan Peters
 *
 */
public class WizardUnsupportedPage extends BaseWizardPage {

	private VBox vbox;

	/**
	 * Creates a new instance of WizardUnsupportedPage
	 */
	public WizardUnsupportedPage() {
		vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(new Label("Wizard not supported."));
	}

	@Override
	public Region getNode() {
		return vbox;
	}

}
