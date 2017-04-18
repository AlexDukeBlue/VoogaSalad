package frontend.wizards.util;

import frontend.util.BaseUIManager;
import frontend.wizards.Wizard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class AdditionalWizardRow<T> extends BaseUIManager<Region>{
	
	private HBox hbox;
	private CheckBox checkbox;
	private Label label;
	private Button button;
	private ObjectProperty<T> objectProperty;
	
	public AdditionalWizardRow(String description, Wizard<T> wizard){
		initialize(description,wizard);
	}

	@Override
	public Region getObject() {
		return hbox;
	}
	
	public ReadOnlyObjectProperty<T> getObjectProperty(){
		return objectProperty;
	}
	
	private void initialize(String description,Wizard<T> wizard){
		hbox = new HBox();
		checkbox = new CheckBox();
		checkbox.setDisable(true);
		label = new Label(description);
		objectProperty = new SimpleObjectProperty<>(null);
		button = new Button("Create New");
		button.setOnAction(event -> {
			wizard.show();
			wizard.addObserver((observable,object) -> {
				objectProperty.setValue((T)object);
			});
		});
		hbox.getChildren().addAll(checkbox,label,button);
	}

}
