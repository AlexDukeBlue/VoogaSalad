package frontend.factory.wizard.wizards.strategies.wizard_pages.util;

import frontend.util.BaseUIManager;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * SelectableInputRow extends the BaseUIManager and is a UI structure used in the creation of
 * many wizard pages used to create objects, that allows the user to select items with check boxes
 * @author Andreas
 *
 */
public class SelectableInputRow extends BaseUIManager<Region> {
	//private static final double ICON_SIZE = 30;

	HBox hbox;
	CheckBox checkbox;
	ImageView icon;
	Label name;
	Label description;

	public SelectableInputRow() {
		this(null, null, null);
	}

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

	public void setImage(Image image) {
		icon.setImage(image);
		if (image != null){
			icon.setFitWidth((Double)getResourceBundle().getObject("ICON_SIZE"));
			icon.setFitHeight((Double)getResourceBundle().getObject("ICON_SIZE"));
		}
	}

	public Image getImage() {
		return icon.getImage();
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public String getName() {
		return name.getText();
	}

	public void setDescription(String description) {
		this.description.setText(description);
	}

	public String getDescription() {
		return description.getText();
	}

	@Override
	public Region getObject() {
		return hbox;
	}

	public BooleanProperty getSelectedProperty() {
		return checkbox.selectedProperty();
	}

	public boolean getSelected() {
		return checkbox.isSelected();
	}

	public void setSelected(boolean selected) {
		checkbox.setSelected(selected);
	}

}
