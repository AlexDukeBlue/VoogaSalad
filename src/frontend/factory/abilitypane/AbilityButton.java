package frontend.factory.abilitypane;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.unit.properties.ActiveAbility;
import backend.util.Ability;
import backend.util.GameplayState;
import backend.util.HasLocation;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.util.GameBoardObjectView;
import frontend.util.VoogaEntityButton;
import javafx.event.Event;

/**
 * @author Created by th174 on 4/22/17.
 */
public class AbilityButton extends VoogaEntityButton {
	private VoogaEntity abilityOwner;

	public AbilityButton(VoogaEntity unit, Ability entity, int size, Controller controller, ClickHandler clickHandler) {
		super(entity, size, controller, clickHandler);
		this.abilityOwner = unit;
	}

	@Override
	public void actInAuthoringMode(ClickableUIComponent target, Object additonalInfo, ClickHandler clickHandler, Event event) {
		if (target instanceof GameBoardObjectView && ((GameBoardObjectView) target).getEntity() instanceof HasLocation && getEntity() instanceof ActiveAbility &&
				((ActiveAbility) getEntity()).getLegalTargetCells((Unit) abilityOwner, getController().getGameState()).contains(((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation())) {
			String abilityTargetName = ((GameBoardObjectView) target).getEntity().getName();
			CoordinateTuple abilityTargetLocation = ((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation();
			String abilityName = getEntity().getName();
			String unitName = abilityOwner.getName();
			CoordinateTuple unitLocation = ((Unit) abilityOwner).getLocation();
			getController().sendModifier((GameplayState gameState) -> {
				VoogaEntity abilityTarget = gameState.getGrid().get(abilityTargetLocation).getOccupantByName(abilityTargetName);
				gameState.getGrid().get(unitLocation).getOccupantByName(unitName).useActiveAbility(abilityName, abilityTarget, gameState);
				return gameState;
			});
		}
		clickHandler.cancel();
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler, Event event) {
		if (target instanceof GameBoardObjectView && ((GameBoardObjectView) target).getEntity() instanceof HasLocation &&
				abilityOwner instanceof Unit && getEntity() instanceof ActiveAbility &&
				((ActiveAbility) getEntity()).getLegalTargetCells((Unit) abilityOwner, getController().getGameState()).contains(((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation())) {
			actInAuthoringMode(target, additionalInfo, clickHandler, event);
		}
	}

	@Override
	public void select(ClickHandler clickHandler) {
		if (getEntity() instanceof ActiveAbility && abilityOwner instanceof Unit) {
			clickHandler.getGridPane().highlightRange(((ActiveAbility<?>) getEntity()).getLegalTargetCells((Unit) abilityOwner, getController().getGameState()));
		}
	}

	@Override
	public void deselect(ClickHandler clickHandler) {
		clickHandler.getGridPane().resetHighlighting();
	}
}
