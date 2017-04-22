package frontend.util;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;

/**
 * @author Created by th174 on 4/22/17.
 */
public class AddRemoveButton extends BaseUIManager<HBox> {
	private HBox content;
	private Button addButton;
	private Button removeButton;

	public AddRemoveButton() {
		super();
		content = createAddRemoveButton();
	}
	private HBox createAddRemoveButton() {
		addButton = new Button("+");
		removeButton = new Button("-");
		addButton.setAlignment(Pos.CENTER);
		removeButton.setAlignment(Pos.CENTER);
		addButton.setMinSize(20, 20);
		addButton.setMaxSize(20, 20);
		removeButton.setMinSize(20, 20);
		removeButton.setMaxSize(20, 20);
		addButton.setBorder(Border.EMPTY);
		addButton.setPadding(Insets.EMPTY);
		removeButton.setBorder(Border.EMPTY);
		removeButton.setPadding(Insets.EMPTY);
		HBox box = new HBox(addButton, removeButton);
		box.setAlignment(Pos.TOP_RIGHT);
		box.setSpacing(0);
		box.setPadding(Insets.EMPTY);
		box.setMaxWidth(Double.MAX_VALUE);
		return box;
	}

	public void setOnAddClicked(EventHandler<MouseEvent> onAddClicked){
		addButton.setOnMouseClicked(onAddClicked);
	}

	public void setOnRemovedClicked(EventHandler<MouseEvent> onRemoveClicked){
		removeButton.setOnMouseClicked(onRemoveClicked);
	}

	@Override
	public HBox getObject() {
		return content;
	}
}
