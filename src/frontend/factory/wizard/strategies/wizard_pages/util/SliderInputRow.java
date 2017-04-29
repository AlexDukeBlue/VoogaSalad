package frontend.factory.wizard.strategies.wizard_pages.util;

import frontend.util.BaseUIManager;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * The SliderInputRow is a WizardPage utility that allows the user to input
 * integers via a slider. This is useful because it prevents the user from
 * inputting values outside of an acceptable range.
 * 
 * @author Dylan Peters
 *
 */
public class SliderInputRow extends BaseUIManager<Region> {

	private HBox myNumericInputRow;
	private Slider mySlider;
	Label myNameField, myLabelField;

	/**
	 * Creates a new instance of SliderInputRow. Sets all values to default.
	 * 
	 * @param image
	 *            The image to display in the SliderInputRow.
	 * @param name
	 *            The name to put in the sliderInputRow.
	 */
	public SliderInputRow(Image image, String name) {
		this(image, new StringBinding() {
			@Override
			protected String computeValue() {
				return name;
			}
		});
	}

	/**
	 * Creates a new instance of SliderInputRow. Sets all values to default.
	 * 
	 * @param image
	 *            The image to display in the SliderInputRow.
	 * @param name
	 *            The name to put in the sliderInputRow.
	 */
	public SliderInputRow(Image image, StringBinding name) {
		super(null);
		initialize(image, name);
	}

	/**
	 * Sets the minimum value for the slider.
	 * 
	 * @param min
	 *            an integer minimum for the slider.
	 */
	public void setMin(int min) {
		mySlider.setMin(min);
	}

	/**
	 * Sets the maximum value for the slider.
	 * 
	 * @param max
	 *            an integer maximum for the slider.
	 */
	public void setMax(int max) {
		mySlider.setMax(max);
	}

	/**
	 * Returns the integer value of the slider.
	 * 
	 * @return the integer value of the slider.
	 */
	public Integer getValue() {
		return (int) mySlider.getValue();
	}

	/**
	 * Sets the value of the slider.
	 * 
	 * @param value
	 *            the value of the slider. Must be between the slider's min and
	 *            max.
	 */
	public void setValue(int value) {
		mySlider.setValue(value);
	}

	@Override
	public Region getNode() {
		return myNumericInputRow;
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

	void setLabel(String label) {
		myLabelField.setText(label);
	}

	String getLabel() {
		return myLabelField.getText();
	}

}