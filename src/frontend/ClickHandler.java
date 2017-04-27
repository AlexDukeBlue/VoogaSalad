package frontend;

import backend.grid.CoordinateTuple;
import frontend.factory.abilitypane.AbilityPane;
import frontend.interfaces.detailpane.DetailPaneExternal;
import frontend.interfaces.worldview.GridViewExternal;
import frontend.util.GameBoardObjectView;
import frontend.util.SelectableUIComponent;
import frontend.util.highlighter.Highlighter;
import frontend.util.highlighter.ShadowHighlighter;
import javafx.event.Event;
import javafx.scene.Node;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Created by th174 on 4/22/17.
 */
public class ClickHandler {
	private static final Highlighter SELECTED_HIGHLIGHTER = new ShadowHighlighter();
	private SelectableUIComponent<? extends Node> selectedComponent;
	private Object additionalInfo;
	private final DetailPaneExternal detailPane;
	private final AbilityPane abilityPane;
	private final GridViewExternal gridPane;
	private Mode currentMode;

	public ClickHandler(DetailPaneExternal detailPane, AbilityPane abilityPane, GridViewExternal gridPane, Mode currentMode) {
		this.detailPane = detailPane;
		this.abilityPane = abilityPane;
		this.gridPane = gridPane;
		this.currentMode = currentMode;
	}

	public final void handleClick(Event event, ClickableUIComponent<? extends Node> clickedComponent, Object additionalInfo) {
		if (Objects.isNull(selectedComponent) && clickedComponent instanceof SelectableUIComponent) {
			setSelectedComponent((SelectableUIComponent<? extends Node>) clickedComponent);
			this.additionalInfo = additionalInfo;
			event.consume();
		} else if (Objects.nonNull(selectedComponent)) {
			triggerAction(selectedComponent, clickedComponent, this.additionalInfo, event);
		} else {
			event.consume();
			cancel();
			showDetail(clickedComponent);
		}
	}

	public final void setMode(Mode currentMode) {
		this.currentMode = currentMode;
	}

	public final void setSelectedComponent(SelectableUIComponent<? extends Node> selectedComponent) {
		cancel();
		this.selectedComponent = selectedComponent;
		SELECTED_HIGHLIGHTER.highlight(selectedComponent.getObject());
		selectedComponent.select(this);
		showDetail(selectedComponent);
	}

	protected void triggerAction(SelectableUIComponent selectedComponent, ClickableUIComponent actionTarget, Object additionalInfo, Event event) {
		if (currentMode.equals(Mode.AUTHORING)) {
			selectedComponent.actInAuthoringMode(actionTarget, additionalInfo, this, event);
		} else if (currentMode.equals(Mode.GAMEPLAY)) {
			selectedComponent.actInGameplayMode(actionTarget, additionalInfo, this, event);
		}
	}

	public final void cancel() {
		if (selectedComponent != null) {
			selectedComponent.deselect(this);
			SELECTED_HIGHLIGHTER.removeHighlight(selectedComponent.getObject());
		}
		selectedComponent = null;
		additionalInfo = null;
		abilityPane.setContent(null);
	}

	void showDetail(ClickableUIComponent clickedComponent) {
		if (clickedComponent instanceof GameBoardObjectView) {
			detailPane.setContent(((GameBoardObjectView) clickedComponent).getEntity());
			abilityPane.setContent(((GameBoardObjectView) clickedComponent).getEntity());
		}
	}

	public void highlightRange(Collection<CoordinateTuple> coordinates) {
		gridPane.highlightRange(coordinates);
	}

	public void resetHighlighting() {
		gridPane.resetHighlighting();
	}

	public enum Mode {
		AUTHORING, GAMEPLAY
	}
}
