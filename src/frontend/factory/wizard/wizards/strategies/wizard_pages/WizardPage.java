package frontend.factory.wizard.wizards.strategies.wizard_pages;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Node;

public interface WizardPage {

	ReadOnlyBooleanProperty canNext();

	StringBinding getDescriptionLabelBinding();

	Node getNode();
	
	void setLanguage(String language);

}