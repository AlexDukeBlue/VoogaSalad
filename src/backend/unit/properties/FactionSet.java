package backend.unit.properties;

import backend.util.ModifiableVoogaCollection;

import java.util.Collection;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class FactionSet extends ModifiableVoogaCollection<Faction, FactionSet> {
	public FactionSet(String name, String description, String imgPath, Faction... factions) {
		super(name, description, imgPath, factions);
	}

	public FactionSet(String name, String description, String imgPath, Collection<? extends Faction> factions) {
		super(name, description, imgPath, factions);
	}

	@Override
	public FactionSet copy() {
		return new FactionSet(getName(), getDescription(), getImgPath(), getAll());
	}

	public static Collection<FactionSet> getPredefinedFactionSets() {
		return getPredefined(FactionSet.class);
	}
}
