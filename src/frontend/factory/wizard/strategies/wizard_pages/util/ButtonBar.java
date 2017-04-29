package frontend.factory.wizard.strategies.wizard_pages.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import frontend.util.BaseUIManager;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * ButtonBar extends the BaseUIManager and is a UI structure used in the
 * creation of many wizard pages used to create objects that creates a bar of
 * buttons
 * 
 * @author Andreas
 *
 */
public class ButtonBar extends BaseUIManager<Region> {

	private HBox hbox;
	private Map<StringBinding, Button> buttons;

	/**
	 * Creates a new instance of ButtonBar. It creates a button for each
	 * buttonName passed in.
	 * 
	 * @param buttonNames
	 *            a collection of StringBindings to use as the text values for
	 *            each button.
	 */
	public ButtonBar(Collection<StringBinding> buttonNames) {
		super(null);
		initialize(buttonNames);
	}

	/**
	 * Returns the button with the name specified. If there is no button with
	 * the specified name, it returns null.
	 * 
	 * @param buttonName
	 *            the name of the button to return.
	 * @return Button with given name.
	 */
	public Button getButton(StringBinding buttonName) {
		return buttons.get(buttonName);
	}

	/**
	 * Sets the action of the button with the given name.
	 * 
	 * @param buttonName
	 *            the button to set the action for.
	 * @param eventHandler
	 *            the event to trigger when the button is pressed.
	 */
	public void setOnAction(String buttonName, EventHandler<ActionEvent> eventHandler) {
		if (buttons.get(buttonName) != null) {
			buttons.get(buttonName).setOnAction(eventHandler);
		}
	}

	@Override
	public Region getNode() {
		return hbox;
	}

	private void initialize(Collection<StringBinding> buttonNames) {
		hbox = new HBox();
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		buttons = new HashMap<>();
		buttonNames.stream().forEachOrdered(buttonName -> {
			Button button = new Button();
			button.textProperty().bind(buttonName);
			buttons.put(buttonName, button);
			hbox.getChildren().add(button);
		});
	}

}