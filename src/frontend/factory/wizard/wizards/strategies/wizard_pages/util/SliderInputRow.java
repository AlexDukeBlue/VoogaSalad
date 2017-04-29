package frontend.factory.wizard.wizards.strategies.wizard_pages.util;

import frontend.util.BaseUIManager;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class SliderInputRow extends BaseUIManager<Region> {

	private HBox myNumericInputRow;
	private Slider mySlider;
	Label myNameField, myLabelField;

	public SliderInputRow(Image image, String name) {
		this(image, new StringBinding() {
			@Override
			protected String computeValue() {
				return name;
			}
		});
	}

	public SliderInputRow(Image image, StringBinding name) {
		initialize(image, name);
	}

	public void setMin(int min) {
		mySlider.setMin(min);
	}

	public void setMax(int max) {
		mySlider.setMax(max);
	}

	private void initialize(Image image, StringBinding name) {
		myNumericInputRow = new HBox();

		myNameField = new Label();
		myNameField.textProperty().bind(name);

		mySlider = new Slider();
		mySlider.setMajorTickUnit(1);
		mySlider.setShowTickLabels(true);

		myLabelField = new Label();
		mySlider.valueProperty().addListener((obs, oldval, newVal) -> mySlider.setValue(newVal.intValue()));
		myLabelField.textProperty().bind(mySlider.valueProperty().asString());

		ImageView imageView = new ImageView(image);
		if (image != null) {
			imageView.setFitWidth(Double.parseDouble(getResourceBundle().getString("IMAGE_SIZE")));
			imageView.setFitHeight(Double.parseDouble(getResourceBundle().getString("IMAGE_SIZE")));
		}

		myNumericInputRow.getChildren().addAll(imageView, myNameField, mySlider, myLabelField);
	}

	void setName(String name) {
		myNameField.setText(name);
	}

	String getName() {
		return myNameField.getText();
	}

	public Integer getValue() {
		return (int) mySlider.getValue();
	}


	public void setValue(int value) {
		mySlider.setValue(value);
	}


	void setLabel(String label) {
		myLabelField.setText(label);
	}

	String getLabel() {
		return myLabelField.getText();
	}

	@Override
	public Region getNode() {
		return myNumericInputRow;
	}
	
}