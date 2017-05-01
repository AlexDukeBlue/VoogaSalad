package backend.util;

import backend.unit.Unit;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Created by th174 on 4/5/17.
 */
public interface TriggeredEffect extends PassiveAbility, HasSound {
	ModifiableTriggeredEffect setDuration(int duration);

	@Override
	TriggeredEffect copy();

	int getRemainingTurns();

	void affect(Unit unit, Event event, GameplayState game);

	Collection<Event> getActivationTriggers();

	boolean isExpired();

	@FunctionalInterface
	interface Effect extends Serializable {
		void affect(Unit unit, Event event, GameplayState game);
	}

	@Override
	TriggeredEffect setSoundPath(String path);
}
