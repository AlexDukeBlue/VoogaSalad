package frontend.wizards.wizard_pages;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Node;

public interface WizardPage {

	ReadOnlyBooleanProperty canNext();

	void setTitle(String title);

	String getTitle();

	void setDescription(String description);

	String getDescription();
	
	Node getObject();

}