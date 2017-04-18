package frontend.wizards.strategies.wizard_pages.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import frontend.util.BaseUIManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * ButtonBar extends the BaseUIManager and is a UI structure used in the creation of
 * many wizard pages used to create objects that creates a bar of buttons
 * @author Andreas
 *
 */
public class ButtonBar extends BaseUIManager<Region> {

	private HBox hbox;
	private Map<String, Button> buttons;

	public ButtonBar(Collection<String> buttonNames) {
		initialize(buttonNames);
	}
	
	public Button getButton(String buttonName){
		return buttons.get(buttonName);
	}

	public void setOnAction(String buttonName, EventHandler<ActionEvent> eventHandler) {
		if (buttons.get(buttonName) != null) {
			buttons.get(buttonName).setOnAction(eventHandler);
		}
	}

	@Override
	public Region getObject() {
		return hbox;
	}

	private void initialize(Collection<String> buttonNames) {
		hbox = new HBox();
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		buttons = new HashMap<>();
		buttonNames.stream().forEachOrdered(buttonName -> {
			Button button = new Button(buttonName);
			buttons.put(buttonName, button);
			hbox.getChildren().add(button);
		});
	}

}