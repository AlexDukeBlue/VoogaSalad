package frontend;

import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.scene.Node;

/**
 * @author Created by th174 on 4/22/17.
 */
public abstract class ClickableUIComponent<T extends Node> extends BaseUIManager<T> {
	private ClickHandler clickHandler;

	public ClickableUIComponent(ClickHandler clickHandler) {
		this(null, clickHandler);
	}

	public ClickableUIComponent(Controller controller, ClickHandler clickHandler) {
		super(controller);
		this.clickHandler = clickHandler;
	}

	protected final ClickHandler getClickHandler() {
		return clickHandler;
	}

	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	public void handleClick(Object o) {
		clickHandler.handleClick(this, o);
	}
}
