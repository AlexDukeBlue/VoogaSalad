package frontend.factory.wizard.wizards.strategies;

import backend.unit.properties.Attack;

/**
 * ActiveAbilityStrategy implements the SelectionStrategy interface in order to
 * allow the user to instantiate new Attacks.
 * 
 * @author Dylan Peters
 *
 */
class ActiveAbilityStrategy extends BaseStrategy<Attack> {

	public ActiveAbilityStrategy() {

	}

	@Override
	public Attack finish() {
		return null;
	}

}
