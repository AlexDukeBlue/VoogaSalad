package frontend.wizards.strategies.wizard_pages;

import frontend.util.BaseUIManager;
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
	private String title, description;

	public BaseWizardPage() {
		this("");
	}

	public BaseWizardPage(String title) {
		this(title, "");
	}

	public BaseWizardPage(String title, String description) {
		this.title = title;
		this.description = description;
		canNext = new SimpleBooleanProperty(false);
	}

	@Override
	public ReadOnlyBooleanProperty canNext() {
		return BooleanProperty.readOnlyBooleanProperty(canNext);
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return description;
	}

	protected BooleanProperty canNextWritable() {
		return canNext;
	}

}
