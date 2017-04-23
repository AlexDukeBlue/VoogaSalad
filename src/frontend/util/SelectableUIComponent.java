package frontend.util;

import controller.Controller;
import frontend.ClickableUIComponent;
import frontend.ComponentClickHandler;
import javafx.scene.Node;

/**
 * @author Created by th174 on 4/22/17.
 */
public abstract class SelectableUIComponent<T extends Node> extends ClickableUIComponent<T> {

	public SelectableUIComponent(ComponentClickHandler clickHandler) {
		super(clickHandler);
	}

	public SelectableUIComponent(Controller controller, ComponentClickHandler componentClickHandler) {
		super(controller, componentClickHandler);
	}

	public abstract void actInAuthoringMode(BaseUIManager target, Object additonalInfo);

	public abstract void actInGameplayMode(BaseUIManager target, Object additionalInfo);
}
