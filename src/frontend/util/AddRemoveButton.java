package frontend.util;

import frontend.ClickableUIComponent;
import frontend.ClickHandler;
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
public class AddRemoveButton extends ClickableUIComponent<HBox> {
	private final HBox content;
	private final Button addButton;

	public AddRemoveButton(ClickHandler clickHandler) {
		super(clickHandler);
		addButton = new Button("+");
		Button removeButton = new Button("-");
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
		removeButton.setOnMouseClicked(event -> handleClick(event, ButtonClicked.REMOVE));
		content = new HBox(addButton, removeButton);
		content.setAlignment(Pos.TOP_RIGHT);
		content.setSpacing(0);
		content.setPadding(Insets.EMPTY);
		content.setMaxWidth(Double.MAX_VALUE);
	}

	@Override
	public HBox getObject() {
		return content;
	}

	public void setOnAddClicked(EventHandler<MouseEvent> onAddClicked) {
		addButton.setOnMouseClicked(onAddClicked);
	}

	public enum ButtonClicked {
		ADD, REMOVE
	}
}
