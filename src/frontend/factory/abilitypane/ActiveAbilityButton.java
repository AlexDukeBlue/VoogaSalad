package frontend.factory.abilitypane;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.unit.properties.ActiveAbility;
import backend.util.HasLocation;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.util.GameBoardObjectView;
import javafx.event.Event;

import java.util.Collections;

/**
 * @author Created by th174 on 4/27/2017.
 */
public class ActiveAbilityButton extends AbilityButton {
	public ActiveAbilityButton(Unit unit, ActiveAbility entity, int size, Controller controller, ClickHandler clickHandler) {
		super(unit, entity, size, controller, clickHandler);
	}

	@Override
	protected Unit getAbilityOwner() {
		return (Unit) super.getAbilityOwner();
	}

	@Override
	public ActiveAbility getEntity() {
		return (ActiveAbility) super.getEntity();
	}

	@Override
	public void actInAuthoringMode(ClickableUIComponent target, Object additonalInfo, ClickHandler clickHandler, Event event) {
		if (isThisUsableActiveAbility(target)) {
			System.out.println("used");
			HasLocation abilityTarget = (HasLocation) ((GameBoardObjectView) target).getEntity();
			getController().useUnitActiveAbility(getEntity().getName(), getAbilityOwner().getName(), getAbilityOwner().getLocation(), abilityTarget.getName(), abilityTarget.getLocation());
		}
		clickHandler.cancel();
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler, Event event) {
		if (isThisLegalActiveAbility(target)) {
			actInAuthoringMode(target, additionalInfo, clickHandler, event);
		}
	}

	@Override
	public void select(ClickHandler clickHandler) {
		System.out.println("Selected");
		clickHandler.highlightRange(canUseAbility() ? getEntity().getLegalTargetCells(getAbilityOwner(), getController().getReadOnlyGameState()) : Collections.emptyList());
	}

	@Override
	public void deselect(ClickHandler clickHandler) {
		clickHandler.resetHighlighting();
	}

	private boolean isThisUsableActiveAbility(ClickableUIComponent target) {
		return target instanceof GameBoardObjectView && ((GameBoardObjectView) target).getEntity() instanceof HasLocation;
	}

	private boolean canUseAbility() {
		return !getAbilityOwner().getOwner().isPresent()
				|| !getController().getActivePlayer().getTeam().isPresent()
				|| !getAbilityOwner().getTeam().isPresent()
				|| getController().isMyPlayerTurn() && getController().getActivePlayer().getTeam().equals((getAbilityOwner()).getTeam());
	}

	private boolean isThisLegalActiveAbility(ClickableUIComponent target) {
		CoordinateTuple targetLocation = ((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation();
		return isThisUsableActiveAbility(target) && canUseAbility() && getEntity().getLegalTargetCells(getAbilityOwner(), getController().getReadOnlyGameState()).contains(targetLocation);
	}
}