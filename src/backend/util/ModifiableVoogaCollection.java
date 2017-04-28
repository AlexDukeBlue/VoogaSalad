package backend.util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class ModifiableVoogaCollection<T extends VoogaEntity, U extends ModifiableVoogaCollection> extends ModifiableVoogaObject<ModifiableVoogaCollection<T, U>> implements ImmutableVoogaCollection<T> {
	private static final long serialVersionUID = 1L;

	private final Map<String, T> gameObjects;

	@SafeVarargs
	public ModifiableVoogaCollection(String name, String description, String imgPath, T... gameObjects) {
		this(name, description, imgPath, Arrays.asList(gameObjects));
	}

	public ModifiableVoogaCollection(String name, String description, String imgPath, Collection<? extends T> gameObjects) {
		super(name, description, imgPath);
		this.gameObjects = new LinkedHashMap<>();
		addAll(gameObjects);
	}

	public U addAll(Collection<? extends T> elements) {
		gameObjects.putAll(elements.parallelStream().collect(Collectors.toMap(T::getName, e -> e)));
		return (U) this;
	}

	public U removeAll(Collection<? extends T> elements) {
		gameObjects.values().removeAll(elements);
		return (U) this;
	}

	@Override
	public ModifiableVoogaCollection<T, U> copy() {
		return new ModifiableVoogaCollection<>(getName(), getDescription(), getImgPath(), stream().map(T::copy).map(e -> (T) e).collect(Collectors.toList()));
	}

	@Override
	public T getByName(String name) {
		return gameObjects.get(name);
	}

	@Override
	public final int size() {
		return gameObjects.size();
	}

	@Override
	public Collection<? extends T> getAll() {
		return Collections.unmodifiableCollection(gameObjects.values());
	}

	@Override
	public Iterator<T> iterator() {
		return gameObjects.values().iterator();
	}

	@SafeVarargs
	public final U addAll(T... elements) {
		return addAll(Arrays.asList(elements));
	}

	@SafeVarargs
	public final U removeAll(T... element) {
		return removeAll(Arrays.asList(element));
	}

	public final U removeAll(String... s) {
		return removeAll(Arrays.stream(s).map(this::getByName).collect(Collectors.toList()));
	}

	public final U removeIf(Predicate<T> removalCondition) {
		gameObjects.values().removeIf(removalCondition);
		return (U) this;
	}
}
