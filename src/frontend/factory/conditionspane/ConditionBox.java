/**
 * 
 */
package frontend.factory.conditionspane;

import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.util.AddRemoveButton;
import frontend.util.SelectableUIComponent;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * @author Stone Mathers
 * Created 4/25/2017
 */
public abstract class ConditionBox extends SelectableUIComponent<Region> {
	private String myName;
	private HBox myBox = new HBox();

	/**
	 * @param conditionName
	 * @param controller
	 * @param clickHandler
	 */
	public ConditionBox(String conditionName, Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		myName = conditionName;
		initBox();
	}

	@Override
	public Region getNode() {
		return myBox;
	}
	
	/**
	 * Return name of condition held.
	 * 
	 * @return String name
	 */
	public String getName(){
		return myName;
	}

	@Override
	public void actInAuthoringMode(ClickableUIComponent target, Object additonalInfo, ClickHandler clickHandler, Event event) {
		if (target instanceof AddRemoveButton) {
			getController();//.removeRules(...)
		}
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler, Event event) {
		//Does nothing because this should not be accessible in GameplayMode
	}
	
	protected abstract void checkBoxAction(ObservableValue<? extends Boolean> o, Boolean oldValue, Boolean newValue);
	
	private void initBox() {
		CheckBox cb = new CheckBox(myName);
		cb.selectedProperty().addListener((o, oldVal, newVal) -> checkBoxAction(o, oldVal, newVal));
		myBox.getChildren().add(cb);
		myBox.setOnMouseClicked(event -> handleClick(event, null));
	}

	@Override
	public VoogaEntity getEntity() {
		return null;
	}
}
