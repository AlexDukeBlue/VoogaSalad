package frontend.factory.templatepane;

import backend.cell.Cell;
import backend.grid.BoundsHandler;
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
import javafx.scene.input.MouseEvent;

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
	public void actInAuthoringMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler, MouseEvent event) {
		String clickedEntityName = getEntity().getName();
		if (target instanceof AddRemoveButton) {
			getController().removeTemplatesByCategory(templateCategory, getEntity().getName());
		} else if (getEntity() instanceof BoundsHandler) {
			getController().sendModifier((AuthoringGameState state) -> {
				state.getGrid().setBoundsHandler((BoundsHandler) state.getTemplateByName(clickedEntityName));
				return state;
			});
		} else if (target instanceof GameBoardObjectView && ((GameBoardObjectView) target).getEntity() instanceof HasLocation) {
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
						e.printStackTrace();
					}
				}
				return gameState;
			});
		}
		super.actInAuthoringMode(target, additionalInfo, clickHandler, event);
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler, MouseEvent event) {
		actInAuthoringMode(target, null, clickHandler, event);
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
