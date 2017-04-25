package frontend.factory.wizard.wizards.strategies.wizard_pages;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class WizardUnsupportedPage extends BaseWizardPage{
	
	VBox vbox;
	
	public WizardUnsupportedPage(){
		vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(new Label("Wizard not supported."));
	}

	@Override
	public Region getObject() {
		return vbox;
	}

}
