package backend.cell;

import backend.util.ModifiableVoogaCollection;

import java.util.Collection;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class Region extends ModifiableVoogaCollection<Terrain, Region> {
	private static final long serialVersionUID = 1L;

	//TODO ResourceBundlify this
	public transient static final Region DEFAULT_REGION = new Region("Default Region")
			.setDescription("The default region contains all the pre-defined terrains")
			.setImgPath("resources/images/blackScreen.png")
			.addAll(Terrain.getPredefinedTerrain());

	public Region(String name) {
		super(name, "", "");
	}

	public Region(String name, String description, String imgPath, Terrain... gameObjects) {
		super(name, description, imgPath, gameObjects);
	}

	public Region(String name, String description, String imgPath, Collection<? extends Terrain> gameObjects) {
		super(name, description, imgPath, gameObjects);
	}

	@Deprecated
	public static Collection<Region> getPredefinedRegions() {
		return getPredefined(Region.class);
	}

	@Override
	public Region copy() {
		return new Region(getName(), getDescription(), getImgPath(), getAll());
	}
}
