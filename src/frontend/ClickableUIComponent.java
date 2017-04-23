package frontend;

import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.scene.Node;

/**
 * @author Created by th174 on 4/22/17.
 */
public abstract class ClickableUIComponent<T extends Node> extends BaseUIManager<T> {
	private ComponentClickHandler clickHandler;

	public ClickableUIComponent(ComponentClickHandler clickHandler) {
		this(null, clickHandler);
	}

	public ClickableUIComponent(Controller controller, ComponentClickHandler clickHandler) {
		super(controller);
		this.clickHandler = clickHandler;
	}

	protected final ComponentClickHandler getClickHandler() {
		return clickHandler;
	}

	public void setClickHandler(ComponentClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	public void handleClick(Object o) {
		clickHandler.handleClick(this, o);
	}

	public void deselect() {
	}
}
