package frontend.factory.abilitypane;

import backend.grid.CoordinateTuple;
import backend.util.*;
import frontend.ComponentClickHandler;
import frontend.util.BaseUIManager;
import frontend.util.VoogaEntityButton;
import frontend.util.GameBoardObjectView;

/**
 * @author Created by th174 on 4/22/17.
 */
public class AbilityButton extends VoogaEntityButton {
	private final String unitName;
	private final CoordinateTuple unitLocation;

	public AbilityButton(HasLocation unit, Ability entity, int size, ComponentClickHandler clickHandler) {
		super(entity, size, clickHandler);
		unitName = unit.getName();
		unitLocation = unit.getLocation();
	}

	@Override
	public void actInAuthoringMode(BaseUIManager target, Object additonalInfo) {
		actInGameplayMode(target, additonalInfo);
	}

	@Override
	public void actInGameplayMode(BaseUIManager target, Object additionalInfo) {
		if (target instanceof GameBoardObjectView) {
			VoogaEntity abilityTarget = ((GameBoardObjectView) target).getEntity();
			String abilityName = getEntityName();
			getController().sendModifier((GameplayState gameState) -> {
				gameState.getGrid().get(this.unitLocation).getOccupantByName(this.unitName).useActiveAbility(abilityName, abilityTarget, gameState);
				return gameState;
			});
		}
	}
}
