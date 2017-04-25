package frontend;

import frontend.factory.abilitypane.AbilityPane;
import frontend.interfaces.detailpane.DetailPaneExternal;
import frontend.interfaces.worldview.GridViewExternal;
import frontend.util.GameBoardObjectView;
import frontend.util.SelectableUIComponent;
import frontend.util.highlighter.Highlighter;
import frontend.util.highlighter.ShadowHighlighter;
import javafx.scene.Node;

import java.util.Objects;

/**
 * @author Created by th174 on 4/22/17.
 */
public abstract class ClickHandler {
	private static final Highlighter SELECTED_HIGHLIGHTER = new ShadowHighlighter();
	private SelectableUIComponent<? extends Node> selectedComponent;
	private Object additionalInfo;
	private DetailPaneExternal detailPane;
	private AbilityPane abilityPane;
	private GridViewExternal gridPane;

	public final void setDetailPane(DetailPaneExternal detailPane) {
		this.detailPane = detailPane;
	}

	public final void setAbilityPane(AbilityPane abilityPane) {
		this.abilityPane = abilityPane;
	}

	public final void handleClick(ClickableUIComponent<? extends Node> clickedComponent, Object additionalInfo) {
		if (Objects.isNull(selectedComponent) && clickedComponent instanceof SelectableUIComponent) {
			setSelectedComponent((SelectableUIComponent<? extends Node>) clickedComponent);
			this.additionalInfo = additionalInfo;
		} else if (Objects.nonNull(selectedComponent)) {
			triggerAction(selectedComponent, clickedComponent, this.additionalInfo);
		} else {
			cancel();
			showDetail(clickedComponent);
		}
	}

	public final void setSelectedComponent(SelectableUIComponent<? extends Node> selectedComponent) {
		cancel();
		this.selectedComponent = selectedComponent;
		SELECTED_HIGHLIGHTER.highlight(selectedComponent.getObject());
		selectedComponent.select(this);
		showDetail(selectedComponent);
	}

	protected abstract void triggerAction(SelectableUIComponent selectedComponent, ClickableUIComponent actionTarget, Object additionalInfo);

	public final void cancel() {
		if (selectedComponent != null) {
			selectedComponent.deselect(this);
			SELECTED_HIGHLIGHTER.removeHighlight(selectedComponent.getObject());
		}
		selectedComponent = null;
		additionalInfo = null;
		abilityPane.setContent(null);
	}

	private void showDetail(ClickableUIComponent clickedComponent) {
		if (clickedComponent instanceof GameBoardObjectView) {
			detailPane.setContent(((GameBoardObjectView) clickedComponent).getEntity());
			abilityPane.setContent(((GameBoardObjectView) clickedComponent).getEntity());
		}
	}

	public GridViewExternal getGridPane() {
		return gridPane;
	}

	public void setGridPane(GridViewExternal gridPane) {
		this.gridPane = gridPane;
	}
}
