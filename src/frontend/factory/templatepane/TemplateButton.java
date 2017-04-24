package frontend.factory.templatepane;

import backend.grid.CoordinateTuple;
import backend.util.AuthoringGameState;
import backend.util.HasLocation;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickableUIComponent;
import frontend.ClickHandler;
import frontend.interfaces.worldview.CellViewExternal;
import frontend.util.AddRemoveButton;
import frontend.util.GameBoardObjectView;
import frontend.util.VoogaEntityButton;

/**
 * @author Created by th174 on 4/22/17.
 */
public class TemplateButton extends VoogaEntityButton implements GameBoardObjectView {
	private final String templateCategory;

	public TemplateButton(VoogaEntity entity, String templateCategory, int size, Controller controller, ClickHandler clickHandler) {
		super(entity, size, controller, clickHandler);
		this.templateCategory = templateCategory;
	}

	@Override
	public void actInAuthoringMode(ClickableUIComponent target, Object additionalInfo) {
		if (target instanceof AddRemoveButton) {
			getController().removeTemplatesByCategory(templateCategory, getEntity().getName());
		} else if (target instanceof GameBoardObjectView && ((GameBoardObjectView) target).getEntity() instanceof HasLocation) {
			String unitClickedName = getEntity().getName();
			CoordinateTuple location = ((CellViewExternal) target).getLocation();
			getController().sendModifier((AuthoringGameState gameState) -> {
				gameState.getGrid().get(location).addVoogaEntity(gameState.getTemplateByName(unitClickedName).copy());
				return gameState;
			});
		}
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo) {
		actInAuthoringMode(target, null);
	}

	@Override
	public String toString() {
		return getEntity().toString();
	}

	@Override
	public VoogaEntity getEntity() {
		return super.getEntity();
	}
}
