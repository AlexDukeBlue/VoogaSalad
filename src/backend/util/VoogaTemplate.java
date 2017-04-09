package backend.util;

/**
 * @author Created by th174 on 4/5/2017.
 */
public abstract class VoogaTemplate<T extends VoogaTemplate<T>> extends ImmutableVoogaObject<T> {

	public VoogaTemplate() {
		this("");
	}

	public VoogaTemplate(String name) {
		this(name, "");
	}

	public VoogaTemplate(String name, String description) {
		this(name, description, "");
	}

	public VoogaTemplate(String name, String description, String imgPath) {
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
