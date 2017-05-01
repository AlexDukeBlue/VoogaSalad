package frontend.factory.wizard.strategies.wizard_pages;

import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Region;

/**
 * BaseWizardPage is an abstract class whose sub-classes implement the full UIs
 * of a single page dialogue in a specific wizard. It is composed with
 * wizards.util package classes and also knows if it can Next.
 *
 * @author Andreas
 */
abstract class BaseWizardPage extends BaseUIManager<Region> implements WizardPage {

	private BooleanProperty canNext;
	private StringBinding description;

	/**
	 * Creates a new BaseWizardPage. Sets the description to the default for all
	 * BaseWizardPages.
	 */
	public BaseWizardPage(Controller controller) {
		this(controller, "");
	}

	/**
	 * Creates a new instance of BaseWizardPage
	 *
	 * @param descriptionKey a String that can be used as a key to a ResourceBundle to set
	 *                       the description of the page
	 */
	public BaseWizardPage(Controller controller, String descriptionKey) {
		super(controller);
		description = getPolyglot().get(descriptionKey);
		canNext = new SimpleBooleanProperty(false);
	}

	@Override
	public ReadOnlyBooleanProperty canNext() {
		return BooleanProperty.readOnlyBooleanProperty(canNext);
	}

	@Override
	public StringBinding getDescriptionLabelBinding() {
		return description;
	}

	@Override
	public void setLanguage(String language) {
		getPolyglot().setLanguage(language);
	}

	void setDescription(StringBinding description) {
		this.description = description;
	}

	protected BooleanProperty canNextWritable() {
		return canNext;
	}

}
