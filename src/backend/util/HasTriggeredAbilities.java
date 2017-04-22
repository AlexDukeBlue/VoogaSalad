package backend.util;

import java.util.Collection;

/**
 * @author Created by th174 on 4/21/17.
 */
public interface HasTriggeredAbilities extends VoogaEntity {
	Collection<? extends TriggeredEffect> getTriggeredAbilities();
}

