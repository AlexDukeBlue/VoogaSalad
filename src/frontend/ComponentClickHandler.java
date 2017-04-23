package frontend;

import frontend.factory.abilitypane.AbilityPane;
import frontend.interfaces.detailpane.DetailPaneExternal;
import frontend.util.GameBoardObjectView;
import frontend.util.SelectableUIComponent;

import java.util.Objects;

/**
 * @author Created by th174 on 4/22/17.
 */
public abstract class ComponentClickHandler {
	private SelectableUIComponent selectedComponent;
	private Object additionalInfo;
	private DetailPaneExternal detailPane;
	private AbilityPane abilityPane;

	public final void setDetailPane(DetailPaneExternal detailPane) {
		this.detailPane = detailPane;
	}

	public final void setAbilityPane(AbilityPane abilityPane) {
		this.abilityPane = abilityPane;
	}

	public final void handleClick(ClickableUIComponent clickedComponent, Object additionalInfo) {
		if (Objects.isNull(selectedComponent) && clickedComponent instanceof SelectableUIComponent) {
			setSelectedComponent((SelectableUIComponent) clickedComponent);
			this.additionalInfo = additionalInfo;
		} else {
			triggerAction(selectedComponent, clickedComponent, this.additionalInfo);
			cancel();
		}
	}

	protected void setSelectedComponent(SelectableUIComponent selectedComponent) {
		this.selectedComponent = selectedComponent;
		if (selectedComponent instanceof GameBoardObjectView) {
			detailPane.setContent(((GameBoardObjectView) selectedComponent).getEntity());
			abilityPane.setContent(((GameBoardObjectView) selectedComponent).getEntity());
		}
	}

	protected abstract void triggerAction(SelectableUIComponent selectedComponent, ClickableUIComponent actionTarget, Object additionalInfo);

	public void cancel(){
		selectedComponent.deselect();
		selectedComponent = null;
		additionalInfo = null;
	}
}
