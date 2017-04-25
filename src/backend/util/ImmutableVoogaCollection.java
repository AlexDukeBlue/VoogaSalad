package backend.util;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Created by th174 on 4/8/2017.
 */
public interface ImmutableVoogaCollection<T extends VoogaEntity> extends Iterable<T>, VoogaEntity {
	T getByName(String name);

	int size();

	default Stream<? extends T> stream() {
		return getAll().stream();
	}

	default boolean containsName(String name) {
		return Objects.nonNull(getByName(name));
	}

	Collection<? extends T> getAll();

	default Stream<? extends T> parallelStream() {
		return getAll().parallelStream();
	}
}
