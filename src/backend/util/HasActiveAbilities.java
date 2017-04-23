package backend.util;

import backend.unit.properties.ActiveAbility;

import java.util.Collection;

/**
 * @author Created by th174 on 4/21/17.
 */
public interface HasActiveAbilities extends HasLocation {
	default void useActiveAbility(String activeAbilityName, VoogaEntity target, GameplayState gameState) {
		useActiveAbility(getActiveAbilityByName(activeAbilityName), target, gameState);
	}

	void useActiveAbility(ActiveAbility activeAbility, VoogaEntity target, GameplayState gameState);

	ActiveAbility getActiveAbilityByName(String name);

	Collection<? extends ActiveAbility> getActiveAbilities();
}
