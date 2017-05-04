package frontend.factory.conditionspane;

import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.util.AddRemoveButton;
import frontend.util.SelectableUIComponent;
import javafx.event.Event;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * A ConditionBox serves to visually represent a condition, such as a
 * requirement to end a turn, an event that happens at the beginning of a turn,
 * or a win/loss/tie condition. It also provides an interface with which the
 * user can activate and deactivate the rules which they have created.
 * Communications to pass requests to the model and visually update the
 * resulting changes are done through a Controller.
 * <p>
 * Activation/deactivation is done through the use of a CheckBox. The behavior
 * of this CheckBox is defined by the checkBoxAction() method. This method must
 * be implemented by any concrete subclasses.
 *
 * @author Stone Mathers Created 4/25/2017
 */
public abstract class ConditionBox extends SelectableUIComponent<Region> {
	private String myName;
	private String myCategory;
	private HBox myBox = new HBox();
	private CheckBox cb;

	/**
	 * Constructs a ConditionBox using the passed parameters.
	 *
	 * @param conditionName Name used to identify the condition being represented.
	 * @param category      Category that the condition belongs to.
	 * @param controller    Controller through which the condition is
	 *                      activated/deactivated.
	 * @param clickHandler  ClickHandler that the ConditionBox belongs to.
	 */
	public ConditionBox(String conditionName, String category, Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		myName = conditionName;
		myCategory = category;
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
	public String getName() {
		return myName;
	}

	@Override
	public void actInAuthoringMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler,
	                               Event event) {
		if (target instanceof AddRemoveButton) {
			getController().removeTemplatesByCategory(myCategory, myName);
			clickHandler.cancel();
		}
		getClickHandler().setSelectedComponent(this);
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler,
	                              Event event) {
		actInAuthoringMode(target, additionalInfo, clickHandler, event);
	}

	@Override
	public VoogaEntity getEntity() {
		return null;
	}

	/**
	 * Carries out actions when the state of the CheckBox is changed, generally
	 * dependent on the value of newValue.
	 *
	 * @param newValue New state of the CheckBox
	 */
	protected abstract void checkBoxAction(Boolean newValue);

	private void initBox() {
		cb = new CheckBox(myName);
		cb.setOnAction(event -> checkBoxAction(cb.isSelected()));
		myBox.getChildren().add(cb);
		myBox.setOnMouseClicked(event -> handleClick(event, null));
	}

	@Override
	public void update() {
		super.update();
		cb.setSelected(Stream.of(getController().getAuthoringGameState().getActiveObjectives(), getController().getAuthoringGameState().getActiveTurnActions(), getController().getAuthoringGameState().getActiveTurnRequirements()).flatMap(Collection::stream).map(VoogaEntity::getName).anyMatch(myName::contains));
	}
}
