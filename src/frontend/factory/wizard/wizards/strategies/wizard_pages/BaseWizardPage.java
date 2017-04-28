package frontend.factory.wizard.wizards.strategies.wizard_pages;

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
 *
 */
abstract class BaseWizardPage extends BaseUIManager<Region> implements WizardPage {

	private BooleanProperty canNext;
	private StringBinding title, description;

	public BaseWizardPage() {
		this("");
	}

	public BaseWizardPage(String title) {
		this(title, "");
	}

	public BaseWizardPage(String title, String description) {
		this.title = getPolyglot().get(title);
		this.description = getPolyglot().get(description);
		canNext = new SimpleBooleanProperty(false);
	}

	@Override
	public ReadOnlyBooleanProperty canNext() {
		return BooleanProperty.readOnlyBooleanProperty(canNext);
	}

	@Override
	public void setTitle(StringBinding title) {
		this.title = title;
	}

	@Override
	public StringBinding getTitle() {
		return title;
	}

	@Override
	public void setDescription(StringBinding description) {
		this.description = description;
	}

	@Override
	public StringBinding getDescription() {
		return description;
	}

	protected BooleanProperty canNextWritable() {
		return canNext;
	}

}
