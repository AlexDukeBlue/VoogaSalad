package frontend.factory.wizard.strategies.wizard_pages.util;

import frontend.util.BaseUIManager;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * SelectableInputRow extends the BaseUIManager and is a UI structure used in
 * the creation of many wizard pages used to create objects, that allows the
 * user to select items with check boxes
 * 
 * @author Andreas
 *
 */
public class SelectableInputRow extends BaseUIManager<Region> {

	HBox hbox;
	CheckBox checkbox;
	ImageView icon;
	Label name;
	Label description;

	/**
	 * Creates a new instance of SelectableInputRow with no image, name, or
	 * description.
	 */
	public SelectableInputRow() {
		this(null, null, null);
	}

	/**
	 * Creates a new instance of selectableInput with the given image, name, and
	 * description.
	 * 
	 * @param image
	 *            the image to use as the icon for the row.
	 * @param name
	 *            the name/title of the row.
	 * @param description
	 *            the description of the row.
	 */
	public SelectableInputRow(Image image, String name, String description) {
		super(null);
		hbox = new HBox();
		checkbox = new CheckBox();
		icon = new ImageView();
		this.name = new Label();
		this.description = new Label();
		setImage(image);
		setName(name);
		setDescription(description);
		hbox.getChildren().addAll(checkbox, icon, this.name, this.description);
	}

	/**
	 * Sets the image for the SelectableInputRow to use as an icon.
	 * 
	 * @param image
	 *            a JavaFX image
	 */
	public void setImage(Image image) {
		icon.setImage(image);
		if (image != null) {
			icon.setFitWidth(Double.parseDouble(getResourceBundle().getString("ICON_SIZE")));
			icon.setFitHeight(Double.parseDouble(getResourceBundle().getString("ICON_SIZE")));
		}
	}

	/**
	 * Returns the image displayed in the SelectableInputRow.
	 * 
	 * @return the image displayed in the SelectableInputRow.
	 */
	public Image getImage() {
		return icon.getImage();
	}

	/**
	 * Sets the name/title of the SelectableInputRow.
	 * 
	 * @param name
	 *            the name/title of the SelectableInputRow.
	 */
	public void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * Returns the name of the SelectableInputRow. This value will be displayed
	 * to the user.
	 * 
	 * @return the name of the SelectableInputRow.
	 */
	public String getName() {
		return name.getText();
	}

	/**
	 * Sets the description of the SelectableInputRow. This value will be
	 * displayed to the user.
	 * 
	 * @param description
	 *            the description of the SelectableInputRow
	 */
	public void setDescription(String description) {
		this.description.setText(description);
	}

	/**
	 * Returns the description of the SelectableInputRow
	 * 
	 * @return a string that is the description of the SelectableInputRow.
	 */
	public String getDescription() {
		return description.getText();
	}

	@Override
	public Region getNode() {
		return hbox;
	}

	/**
	 * Returns a BooleanProperty containing a boolean of whether the row is
	 * selected.
	 * 
	 * @return a BooleanProperty containing a boolean of whether the row is
	 *         selected.
	 */
	public BooleanProperty getSelectedProperty() {
		return checkbox.selectedProperty();
	}

	/**
	 * Returns a boolean of whether the row is selected.
	 * 
	 * @return a boolean of whether the row is selected.
	 */
	public boolean getSelected() {
		return checkbox.isSelected();
	}

	/**
	 * Sets the row's selected property to the specified boolean.
	 * 
	 * @param selected
	 *            the value to set the selected property to.
	 */
	public void setSelected(boolean selected) {
		checkbox.setSelected(selected);
	}

}
