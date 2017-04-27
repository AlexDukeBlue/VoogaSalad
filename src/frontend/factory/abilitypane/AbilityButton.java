package frontend.factory.abilitypane;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.unit.properties.ActiveAbility;
import backend.util.Ability;
import backend.util.HasLocation;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.util.GameBoardObjectView;
import frontend.util.VoogaEntityButton;
import javafx.event.Event;

import java.util.Collections;

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
		if (isThisUsableActiveAbility(target)) {
			getController().useUnitActiveAbility(getEntity().getName(), abilityOwner.getName(), ((Unit) abilityOwner).getLocation(), ((GameBoardObjectView) target).getEntity().getName(), ((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation());
		}
		clickHandler.cancel();
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler, Event event) {
		if (isThisLegalActiveAbility(target) && getController().isMyPlayerTurn()) {
			actInAuthoringMode(target, additionalInfo, clickHandler, event);
		}
	}

	@Override
	public void select(ClickHandler clickHandler) {
		if (getEntity() instanceof ActiveAbility) {
			clickHandler.getGridPane().highlightRange(getController().isMyPlayerTurn() ? ((ActiveAbility<?>) getEntity()).getLegalTargetCells((Unit) abilityOwner, getController().getGameState()) : Collections.emptyList());
		}
	}

	@Override
	public void deselect(ClickHandler clickHandler) {
		clickHandler.getGridPane().resetHighlighting();
	}

	private boolean isThisUsableActiveAbility(ClickableUIComponent target) {
		return target instanceof GameBoardObjectView
				&& ((GameBoardObjectView) target).getEntity() instanceof HasLocation
				&& getEntity() instanceof ActiveAbility;
	}

	private boolean isThisLegalActiveAbility(ClickableUIComponent target) {
		ActiveAbility ability = (ActiveAbility) getEntity();
		Unit abilityUser = (Unit) abilityOwner;
		CoordinateTuple targetLocation = ((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation();
		return isThisUsableActiveAbility(target) && ability.getLegalTargetCells(abilityUser, getController().getGameState()).contains(targetLocation);
	}
}
