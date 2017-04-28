package backend.util;

/**
 * @author Created by th174 on 4/5/2017.
 */
public abstract class ModifiableVoogaObject<T extends ModifiableVoogaObject<T>> extends ImmutableVoogaObject<T> {
	private static final long serialVersionUID = 1L;

	public ModifiableVoogaObject() {
		this("");
	}

	public ModifiableVoogaObject(String name) {
		this(name, "");
	}

	public ModifiableVoogaObject(String name, String description) {
		this(name, description, "");
	}

	public ModifiableVoogaObject(String name, String description, String imgPath) {
		super(name, description, imgPath);
	}

	@Override
	public final T setName(String name) {
		return super.setName(name);
	}

	@Override
	public final T setDescription(String description) {
		return super.setDescription(description);
	}

	@Override
	public final T setImgPath(String imgPath) {
		return super.setImgPath(imgPath);
	}
}
