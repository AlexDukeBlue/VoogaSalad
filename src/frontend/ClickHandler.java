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
 * 
 * This class handles clicking events for all components in the frontend of this project and distinguishes
 * between the appropriate responses in authoring and playing mode.
 * 
 * DEPENDENCIES
 */
public class ClickHandler {
	private static final Highlighter SELECTED_HIGHLIGHTER = new ShadowHighlighter();
	private SelectableUIComponent<? extends Node> selectedComponent;
	private Object additionalInfo;
	private final DetailPaneExternal detailPane;
	private final AbilityPane abilityPane;
	private final GridViewExternal gridPane;
	private Mode currentMode;
	private SoundHandler sound;

	public ClickHandler(DetailPaneExternal detailPane, AbilityPane abilityPane, GridViewExternal gridPane, Mode currentMode) {
		this.detailPane = detailPane;
		this.abilityPane = abilityPane;
		this.gridPane = gridPane;
		this.currentMode = currentMode;
		sound = new SoundHandler();
	}

	/**
	 * Handles the response for a UI component that has been clicked
	 * @param event
	 * The event is the click that has occurred
	 * @param clickedComponent
	 * The component of the UI that is being interacted with
	 * @param additionalInfo
	 * The object that an action should be performed on as a result of the event
	 */
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

	/**
	 * Switches between edit and play mode
	 * @param currentMode
	 * The mode that the program should be in after this method is called
	 */
	public final void setMode(Mode currentMode) {
		if (this.currentMode != currentMode){
			cancel();
		}
		this.currentMode = currentMode;
	}

	/**
	 * Selects a component of the UI based on user interaction
	 * @param selectedComponent
	 * The UI component that has been selected
	 */
	public final void setSelectedComponent(SelectableUIComponent<? extends Node> selectedComponent) {
		cancel();
		this.selectedComponent = selectedComponent;
		SELECTED_HIGHLIGHTER.highlight(selectedComponent.getNode());
		selectedComponent.select(this);
		showDetail(selectedComponent);
		//sound.playSound(false, "resources/click.wav");
	}

	protected void triggerAction(SelectableUIComponent selectedComponent, ClickableUIComponent actionTarget, Object additionalInfo, Event event) {
		//sound.playSound(false, "resources/plop.wav");
		if (currentMode.equals(Mode.AUTHORING)) {
			selectedComponent.actInAuthoringMode(actionTarget, additionalInfo, this, event);
		} else if (currentMode.equals(Mode.GAMEPLAY)) {
			selectedComponent.actInGameplayMode(actionTarget, additionalInfo, this, event);
		}
	}

	/**
	 * Deselects a component of the UI
	 */
	public final void cancel() {
		if (selectedComponent != null) {
			selectedComponent.deselect(this);
			SELECTED_HIGHLIGHTER.removeHighlight(selectedComponent.getNode());
		}
		selectedComponent = null;
		additionalInfo = null;
		abilityPane.setContent(null);
	}

	/**
	 * Sets the detail and ability pane to display information about the selected component,
	 * if that component is a vooga entity
	 * @param clickedComponent
	 * The selected UI component
	 */
	public void showDetail(ClickableUIComponent clickedComponent) {
		if (clickedComponent instanceof GameBoardObjectView) {
			detailPane.setContent(((GameBoardObjectView) clickedComponent).getEntity());
			abilityPane.setContent(((GameBoardObjectView) clickedComponent).getEntity());
		}
	}

	/**
	 * Highlights the designated cells within the game grid
	 * @param coordinates
	 * The coordinate locations that should be highlighted
	 */
	public void highlightRange(Collection<CoordinateTuple> coordinates) {
		gridPane.highlightRange(coordinates);
	}

	/**
	 * Removes existing highlighting from the grid
	 */
	public void resetHighlighting() {
		gridPane.resetHighlighting();
	}

	public enum Mode {
		AUTHORING, GAMEPLAY
	}
}
