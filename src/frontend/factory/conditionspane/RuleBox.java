/**
 *
 */
package frontend.factory.conditionspane;

import controller.Controller;
import frontend.ComponentClickHandler;
import frontend.util.BaseUIManager;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * @author Stone Mathers
 *         Created 4/20/2017
 */
public class RuleBox extends BaseUIManager<Region> {

	private String myName;
	private HBox myBox = new HBox();

	/**
	 * @param ruleName
	 * @param controller
	 * @param clickHandler
	 */
	public RuleBox(String ruleName, Controller controller, ComponentClickHandler clickHandler) {
		super(controller, clickHandler);
		myName = ruleName;
		initBox();
	}

	@Override
	public Region getObject() {
		return myBox;
	}

	private void initBox() {
		CheckBox cb = new CheckBox(myName);
		cb.selectedProperty().addListener((o, oldVal, newVal) -> {
			if (newVal) {
				//getController().addActiveRule(myName);	//TODO
			} else {
				//getController().removeActiveRule(myName);	//TODO
			}
		});
		myBox.getChildren().add(cb);
		myBox.setOnMouseClicked(event -> handleClick(null));
	}
}
