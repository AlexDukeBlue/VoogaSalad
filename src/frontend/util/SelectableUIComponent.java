package frontend.util;

import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * @author Created by th174 on 4/22/17.
 */
public abstract class SelectableUIComponent<T extends Node> extends ClickableUIComponent<T> implements GameBoardObjectView{

	public SelectableUIComponent(ClickHandler clickHandler) {
		super(clickHandler);
	}

	public SelectableUIComponent(Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
	}

	public final void setAsSelected() {
		getClickHandler().setSelectedComponent(this);
	}

	public abstract void actInAuthoringMode(ClickableUIComponent target, Object additonalInfo, ClickHandler clickHandler, MouseEvent event);

	public abstract void actInGameplayMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler, MouseEvent event);

	public void select(ClickHandler clickHandler) {
	}

	public void deselect(ClickHandler clickHandler) {
	}
}
