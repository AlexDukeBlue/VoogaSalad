package frontend.factory.wizard.strategies.wizard_pages.util;

import frontend.util.BaseUIManager;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * NumericInputRow extends the BaseUIManager and is a UI structure used in the
 * creation of many wizard pages used to create objects that allows users to
 * specify a numeric value of interaction between two objects "name" and "label"
 * 
 * @author Andreas
 *
 */
public class NumericInputRow extends BaseUIManager<Region> {

	private HBox myNumericInputRow;
	private TextField myNumericalInputField;
	Label myNameField, myLabelField;

	/**
	 * Creates a new instance of NumericInputRow with the given image, name, and
	 * label.
	 * 
	 * @param image
	 *            the image to use as the icon for the row.
	 * @param name
	 *            the name to use as the title for the row.
	 * @param label
	 *            a string to use as the description for the row.
	 */
	public NumericInputRow(Image image, String name, String label) {
		this(image, new StringBinding() {

			@Override
			protected String computeValue() {
				return name;
			}
		}, new StringBinding() {

			@Override
			protected String computeValue() {
				return label;
			}

		});
	}

	/**
	 * Creates a new instance of NumericInputRow with the given image, name, and
	 * label.
	 * 
	 * @param image
	 *            the image to use as the icon for the row.
	 * @param name
	 *            the name to use as the title for the row.
	 * @param label
	 *            a StringBinding to use as the description for the row.
	 */
	public NumericInputRow(Image image, StringBinding name, StringBinding label) {
		initialize(image, name, label);
	}

	/**
	 * Sets the value of the NumericInputRow display object to the given
	 * integer.
	 * 
	 * @param value
	 *            the integer value to set for the NumericInputRow.
	 */
	public void setValue(Integer value) {
		myNumericalInputField.setText(value.toString());
	}

	@Override
	public Region getNode() {
		return myNumericInputRow;
	}

	/**
	 * Sets the action that occurs when the value of the NumericInputRow
	 * changes.
	 * 
	 * @param eventHandler
	 *            the EventHandler to execute
	 */
	public void setOnAction(EventHandler<ActionEvent> eventHandler) {
		myNumericalInputField.setOnAction(eventHandler);
	}

	void setName(String name) {
		myNameField.setText(name);
	}

	String getName() {
		return myNameField.getText();
	}

	public Integer getValue() {
		return myNumericalInputField.getText().isEmpty() ? 0 : Integer.parseInt(myNumericalInputField.getText());
	}

	void setLabel(String label) {
		myLabelField.setText(label);
	}

	String getLabel() {
		return myLabelField.getText();
	}

	private void initialize(Image image, StringBinding name, StringBinding label) {
		myNumericInputRow = new HBox();

		myNameField = new Label();
		myNameField.textProperty().bind(name);

		myNumericalInputField = new TextField(getResourceBundle().getString("DEFAULT_ONE"));
		myNumericalInputField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					Integer.parseInt(newValue);
				} catch (Exception e) {
					if (!newValue.isEmpty()) {
						myNumericalInputField.setText(getResourceBundle().getString("DEFAULT_ONE"));
					}
				}
				setChanged();
				notifyObservers(getValue());
				clearChanged();
			}
		});

		myLabelField = new Label();
		myLabelField.textProperty().bind(label);

		ImageView imageView = new ImageView(image);
		if (image != null) {
			imageView.setFitWidth(Double.parseDouble(getResourceBundle().getString("IMAGE_SIZE")));
			imageView.setFitHeight(Double.parseDouble(getResourceBundle().getString("IMAGE_SIZE")));
		}

		myNumericInputRow.getChildren().addAll(imageView, myNameField, myNumericalInputField, myLabelField);
	}

}
