package backend.util;

import backend.unit.properties.InteractionModifier;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Created by th174 on 4/28/2017.
 */
public interface HasPassiveModifiers extends VoogaEntity {
	List<? extends InteractionModifier> getOffensiveModifiers();

	List<? extends InteractionModifier> getDefensiveModifiers();

	default List<? extends InteractionModifier> getAllModifiers() {
		return Stream.of(getOffensiveModifiers(), getDefensiveModifiers()).flatMap(Collection::stream).collect(Collectors.toList());
	}
}
