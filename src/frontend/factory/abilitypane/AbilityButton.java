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
		if (target instanceof GameBoardObjectView && Objects.nonNull(unitLocation)) {
			VoogaEntity abilityTarget = ((GameBoardObjectView) target).getEntity();
			String abilityName = getEntity().getName();
			getController().sendModifier((GameplayState gameState) -> {
				gameState.getGrid().get(this.unitLocation).getOccupantByName(this.unitName).useActiveAbility(abilityName, abilityTarget, gameState);
				return gameState;
			});
		}
	}
}
