package frontend.wizards.strategies;

import backend.unit.properties.Attack;

/**
 * ActiveAbilityStrategy implements the SelectionStrategy interface in order to
 * allow the user to instantiate new Attacks.
 * 
 * @author Dylan Peters
 *
 */
public class ActiveAbilityStrategy extends BaseStrategy<Attack> {

	@Override
	public Attack finish() {
		return null;
	}

}
