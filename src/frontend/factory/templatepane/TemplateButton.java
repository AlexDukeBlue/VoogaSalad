package frontend.factory.templatepane;

import backend.grid.CoordinateTuple;
import backend.util.AuthoringGameState;
import backend.util.HasLocation;
import frontend.ComponentClickHandler;
import frontend.interfaces.worldview.CellViewExternal;
import frontend.util.AddRemoveButton;
import frontend.util.BaseUIManager;
import frontend.util.VoogaEntityButton;

/**
 * @author Created by th174 on 4/22/17.
 */
public class TemplateButton extends VoogaEntityButton {
	private final String templateCategory;

	public TemplateButton(HasLocation entity, String templateCategory, int size, ComponentClickHandler clickHandler) {
		super(entity, size, clickHandler);
		this.templateCategory = templateCategory;
	}

	@Override
	public void actInAuthoringMode(BaseUIManager target, Object additionalInfo) {
		if (target instanceof AddRemoveButton) {
			getController().removeTemplatesByCategory(templateCategory, getEntityName());
		} else if (target instanceof CellViewExternal) {
			String unitClickedName = getEntityName();
			CoordinateTuple location = ((CellViewExternal) target).getLocation();
			getController().sendModifier((AuthoringGameState gameState) -> {
				gameState.getGrid().get(location).addVoogaEntity(gameState.getTemplateByName(unitClickedName).copy());
				return gameState;
			});
		}
	}

	@Override
	public void actInGameplayMode(BaseUIManager target, Object additionalInfo) {
		actInAuthoringMode(target, null);
	}
}
