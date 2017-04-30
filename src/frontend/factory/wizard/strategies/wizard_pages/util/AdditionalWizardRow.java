package frontend.factory.wizard.strategies.wizard_pages.util;

import frontend.factory.wizard.Wizard;
import frontend.util.BaseUIManager;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * AdditionalWizardRow is a BaseUIManager that creates a set of objects that
 * allows the user to create a new wizard.
 * 
 * @author Dylan Peters
 *
 * @param <T>
 *            The type of wizard to create when the row is interacted with.
 */
public class AdditionalWizardRow<T> extends BaseUIManager<Region> {

	private HBox hbox;
	private CheckBox checkbox;
	private Label label;
	private Button button;
	private ObjectProperty<T> objectProperty;

	/**
	 * Creates a new instance of AdditionalWizardRow. Sets all values to
	 * default.
	 * 
	 * @param description
	 *            a string that will be the description of the
	 *            additionalWizardRow.
	 * @param wizard
	 *            the Wizard that will be launched when the additionalWizardRow
	 *            is interacted with.
	 */
	public AdditionalWizardRow(String description, Wizard<T> wizard) {
		this(new StringBinding() {

			@Override
			protected String computeValue() {
				return description;
			}

		}, wizard);
	}

	/**
	 * Creates a new instance of AdditionalWizardRow. Sets all values to
	 * default.
	 * 
	 * @param description
	 *            a StringBinding that will be the description of the
	 *            additionalWizardRow.
	 * @param wizard
	 *            the Wizard that will be launched when the additionalWizardRow
	 *            is interacted with.
	 */
	public AdditionalWizardRow(StringBinding description, Wizard<T> wizard) {
		super(null);
		initialize(description, wizard);
	}

	@Override
	public Region getNode() {
		return hbox;
	}

	/**
	 * Gets the Object that is being created by the row. The ObjectProperty
	 * initially holds a null object, until the wizard is completely finished.
	 * Client code can listen for changes in the ObjectProperty.
	 * 
	 * @return the Object that is being created by the row
	 */
	public ReadOnlyObjectProperty<T> getObjectProperty() {
		return objectProperty;
	}

	private void initialize(StringBinding description, Wizard<T> wizard) {
		hbox = new HBox();
		checkbox = new CheckBox();
		checkbox.setDisable(true);
		label = new Label();
		label.textProperty().bind(description);
		objectProperty = new SimpleObjectProperty<>(null);
		button = new Button();
		button.textProperty().bind(getPolyglot().get("CreateNew"));
		button.setOnAction(event -> {
			wizard.show();
			wizard.addObserver((observable, object) -> {
				objectProperty.setValue((T) object);
				checkbox.setSelected(true);
			});
		});
		hbox.getChildren().addAll(checkbox, label, button);
	}

}
