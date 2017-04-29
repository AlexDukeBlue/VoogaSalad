package backend.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 4/8/2017.
 */
public abstract class ImmutableVoogaObject<T extends ImmutableVoogaObject<T>> implements VoogaEntity {
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private String imgPath;

	public ImmutableVoogaObject(String name, String description, String imgPath) {
		this.name = name;
		this.description = description;
		this.imgPath = imgPath;
	}

	public static <T> Collection<T> getPredefined(Class<T> clazz) {
		return Arrays.stream(clazz.getFields()).map(e -> {
			try {
				return e.get(null);
			} catch (IllegalAccessException e1) {
				throw new RuntimeException("This will never happen");
			}
		}).filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getName() {
		return name;
	}

	T setName(String name) {
		this.name = name;
		return (T) this;
	}

	@Override
	public String getDescription() {
		return description;
	}

	T setDescription(String description) {
		this.description = description;
		return (T) this;
	}

	@Override
	public String getImgPath() {
		return imgPath;
	}

	T setImgPath(String imgPath) {
		this.imgPath = imgPath;
		return (T) this;
	}
}
