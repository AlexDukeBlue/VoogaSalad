package frontend.factory.abilitypane;

import backend.grid.CoordinateTuple;
import backend.util.Ability;
import backend.util.GameplayState;
import backend.util.HasLocation;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickableUIComponent;
import frontend.ClickHandler;
import frontend.util.GameBoardObjectView;
import frontend.util.VoogaEntityButton;

import java.util.Objects;

/**
 * @author Created by th174 on 4/22/17.
 */
public class AbilityButton extends VoogaEntityButton {
	private final String unitName;
	private final CoordinateTuple unitLocation;

	public AbilityButton(VoogaEntity unit, Ability entity, int size, Controller controller, ClickHandler clickHandler) {
		super(entity, size, controller, clickHandler);
		unitName = unit.getName();
		unitLocation = unit instanceof HasLocation ? ((HasLocation) unit).getLocation() : null;
	}

	@Override
	public void actInAuthoringMode(ClickableUIComponent target, Object additonalInfo) {
		actInGameplayMode(target, additonalInfo);
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo) {
		if (target instanceof GameBoardObjectView && ((GameBoardObjectView) target).getEntity() instanceof HasLocation && Objects.nonNull(unitLocation)) {
			String abilityTargetName = ((GameBoardObjectView) target).getEntity().getName();
			CoordinateTuple abilityTargetLocation = ((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation();
			String abilityName = getEntity().getName();
			String unitName = this.unitName;
			CoordinateTuple unitLocation = this.unitLocation;
			getController().sendModifier((GameplayState gameState) -> {
				VoogaEntity abilityTarget = gameState.getGrid().get(abilityTargetLocation).getOccupantByName(abilityTargetName);
				gameState.getGrid().get(unitLocation).getOccupantByName(unitName).useActiveAbility(abilityName, abilityTarget, gameState);
				return gameState;
			});
		}
	}
}
