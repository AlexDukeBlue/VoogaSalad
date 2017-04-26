package frontend.factory.wizard.wizards.strategies.wizard_pages;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Node;

public interface WizardPage {

	ReadOnlyBooleanProperty canNext();

	void setTitle(StringBinding title);

	StringBinding getTitle();

	void setDescription(StringBinding description);

	StringBinding getDescription();
	
	Node getObject();

}