package frontend.factory.abilitypane;

import backend.util.Ability;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.util.VoogaEntityButton;

/**
 * @author Created by th174 on 4/22/17.
 */
public class AbilityButton extends VoogaEntityButton {
	private final VoogaEntity abilityOwner;

	public AbilityButton(VoogaEntity unit, Ability entity, int size, Controller controller, ClickHandler clickHandler) {
		super(entity, size, controller, clickHandler);
		this.abilityOwner = unit;
	}

	protected VoogaEntity getAbilityOwner() {
		return abilityOwner;
	}
}
