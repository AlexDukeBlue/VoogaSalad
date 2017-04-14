package frontend.wizards.wizard_pages;

import frontend.util.BaseUIManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Region;

/**
 * WizardPage is an abstract class who's sub-classes implement the full UI's of a single
 * page dialogue in a specific wizard. It is composed with wizards.util package classes and also knows if it 
 * can Next.
 * @author Andreas
 *
 */
public abstract class WizardPage extends BaseUIManager<Region> {

	private BooleanProperty canNext;
	private String title, description;

	public WizardPage() {
		this("");
	}

	public WizardPage(String title) {
		this(title, "");
	}

	public WizardPage(String title, String description) {
		this.title = title;
		this.description = description;
		canNext = new SimpleBooleanProperty(false);
	}

	public ReadOnlyBooleanProperty canNext() {
		return BooleanProperty.readOnlyBooleanProperty(canNext);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	protected BooleanProperty canNextWritable() {
		return canNext;
	}

}
