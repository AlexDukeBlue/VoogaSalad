package frontend.factory.templatepane;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.HasLocation;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.util.AddRemoveButton;
import frontend.util.GameBoardObjectView;
import frontend.util.VoogaEntityButton;

import java.util.Objects;

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
	public void actInAuthoringMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler) {
		if (target instanceof AddRemoveButton) {
			getController().removeTemplatesByCategory(templateCategory, getEntity().getName());
		} else if (target instanceof GameBoardObjectView && ((GameBoardObjectView) target).getEntity() instanceof HasLocation) {
			String clickedEntityName = getEntity().getName();
			CoordinateTuple location = ((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation();
			String targetName = ((GameBoardObjectView) target).getEntity().getName();
			getController().sendModifier((AuthoringGameState gameState) -> {
				try {
					Unit targetUnit = gameState.getGrid().get(location).getOccupantByName(targetName);
					if (Objects.nonNull(targetUnit)) {
						targetUnit.add(gameState.getTemplateByName(clickedEntityName).copy());
					} else {
						throw new Exception();
					}
				} catch (Exception e) {
					Cell targetCell = gameState.getGrid().get(location);
					if (Objects.nonNull(targetCell)) {
						targetCell.add(gameState.getTemplateByName(clickedEntityName).copy());
					} else {
						throw new Error(e);
					}
				}
				return gameState;
			});
		}
		setAsSelected();
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler) {
		actInAuthoringMode(target, null, clickHandler);
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
