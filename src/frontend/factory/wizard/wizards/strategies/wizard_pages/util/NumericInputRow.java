package frontend.factory.wizard.wizards.strategies.wizard_pages.util;

import frontend.util.BaseUIManager;
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
 * NumericInputRow extends the BaseUIManager and is a UI structure used in the creation of
 * many wizard pages used to create objects that allows users to specify a numeric value of interaction between two
 * objects "name" and "label"
 * @author Andreas
 *
 */
public class NumericInputRow extends BaseUIManager<Region> {
	private static final double IMAGE_SIZE = 30;

	private HBox myNumericInputRow;
	private TextField myNumericalInputField;
	Label myNameField, myLabelField;

	public NumericInputRow(Image image, String name, String label) {
		initialize(image, name, label);
	}

	private void initialize(Image image, String name, String label) {
		myNumericInputRow = new HBox();

		myNameField = new Label(name);

		myNumericalInputField = new TextField("0");
		myNumericalInputField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					Integer.parseInt(newValue);
				} catch (Exception e) {
					if (!newValue.isEmpty()){
						myNumericalInputField.setText("0");
					}
				}
				setChanged();
				notifyObservers(getValue());
				clearChanged();
			}
		});

		myLabelField = new Label(label);
		
		ImageView imageView = new ImageView(image);
		if (image != null){
			imageView.setFitWidth(IMAGE_SIZE);
			imageView.setFitHeight(IMAGE_SIZE);
		}

		myNumericInputRow.getChildren().addAll(imageView, myNameField, myNumericalInputField, myLabelField);
	}

	void setName(String name) {
		myNameField.setText(name);
	}

	String getName() {
		return myNameField.getText();
	}

	public void setValue(Integer value) {
		myNumericalInputField.setText(value.toString());
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

	@Override
	public Region getObject() {
		return myNumericInputRow;
	}
	
	public void setOnAction(EventHandler<ActionEvent> eventHandler){
		myNumericalInputField.setOnAction(eventHandler);
	}

}
