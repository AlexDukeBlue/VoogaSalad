package frontend.factory.wizard.strategies.wizard_pages;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Node;

/**
 * A WizardPage is an object that can be displayed within a WizardStrategy to
 * get information from the user. The WizardPage has a getNode method so that it
 * can be displayed; it can put anything on that node, so the user can input
 * text, images, filepaths to sound files, or numbers for stats. This allows the
 * WizardPage to have a lot of flexibility with what it can be used to
 * instantiate.
 * 
 * @author Dylan Peters
 *
 */
public interface WizardPage {

	/**
	 * The canNext booleanproperty is used by the WizardPage because some wizard
	 * pages may have mandatory fields that the user must fill in. This canNext
	 * property can be used within the wizard or wizard strategy to prevent the
	 * user from going to the next page without filling out the required fields.
	 * 
	 * @return ReadOnlyBooleanProperty canNext that says whether or not the user
	 *         has filled in required fields and can move on to the next screen.
	 */
	ReadOnlyBooleanProperty canNext();

	/**
	 * The desciptionLabelBinding is a StringBinding object that contains a
	 * String with the description of the WizardPage. This description should be
	 * displayed to the user in some way, as it contains instructions about how
	 * to interact with the WizardPage. Because it is a StringBinding, the
	 * WizardPage can change it based on the user's input, and the object
	 * displaying it to the user will be automatically updated.
	 * 
	 * @return StringBinding with the page's description
	 */
	StringBinding getDescriptionLabelBinding();

	/**
	 * Returns the node for the WizardPage; this Node contains all of the UI
	 * components that the user needs to interact with to gather the information
	 * that the WizardPage needs.
	 * 
	 * @return Node to display the page.
	 */
	Node getNode();

	/**
	 * Allows client code to change the language of the WizardPage to the
	 * specified language.
	 * 
	 * @param language
	 *            The language that the client code wants to set the WizardPage
	 *            to use. The language should be specified using English
	 *            representation of the language's name (i.e. "English",
	 *            "Spanish", "French", etc.)
	 */
	void setLanguage(String language);

}