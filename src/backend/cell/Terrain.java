package backend.cell;

import backend.unit.properties.InteractionModifier;
import backend.util.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static backend.util.ImmutableVoogaObject.getPredefined;

/**
 * @author Created by th174 on 4/12/2017.
 */
public interface Terrain extends VoogaEntity, HasTriggeredAbilities, HasSound, HasPassiveModifiers {
	int IMPASSABLE = Integer.MAX_VALUE;
	Terrain EMPTY = new ModifiableTerrain("Empty")
			.setDefaultMoveCost(0)
			.setDescription("Literally nothing")
			.setImgPath("resources/images/blackScreen.png");
	Terrain FLAT = new ModifiableTerrain("Flat")
			.setDefaultMoveCost(1)
			.addDefensiveModifiers(InteractionModifier.DEFAULT_FLAT_TERRAIN_DEFENSE)
			.setDescription("Open, flat, land that offers little defensive cover, but allows for easy movement.")
			.setImgPath("resources/images/kansas.png");
	Terrain FOREST = new ModifiableTerrain("Forest")
			.setDefaultMoveCost(2)
			.addDefensiveModifiers(InteractionModifier.DEFAULT_FOREST_TERRAIN_DEFENSE)
			.setDescription("Thick forest that offers plenty of cover, but makes navigating difficult.")
			.setImgPath("resources/images/forest.png");
	Terrain WATER = new ModifiableTerrain("Water")
			.setDefaultMoveCost(3)
			.addDefensiveModifiers(InteractionModifier.DEFAULT_WATER_TERRAIN_DEFENSE)
			.setDescription("Water that impedes movement for non-aquatic units")
			.setImgPath("resources/images/water.png");
	Terrain MOUNTAIN = new ModifiableTerrain("Mountain")
			.setDefaultMoveCost(2)
			.addDefensiveModifiers(InteractionModifier.DEFAULT_MOUNTAIN_TERRAIN_DEFENSE)
			.setDescription("Rugged mountains that are difficult to navigate through")
			.setImgPath("resources/images/mountain.png");
	Terrain FORTIFIED = new ModifiableTerrain("Fortified")
			.setDefaultMoveCost(1)
			.addDefensiveModifiers(InteractionModifier.DEFAULT_FORTIFIED_TERRAIN_DEFENSE)
			.setDescription("A fortified defensive position")
			.setImgPath("resources/images/castle.png");

	@Deprecated
	static Collection<? extends Terrain> getPredefinedTerrain() {
		return getPredefined(Terrain.class);
	}

	Collection<? extends TriggeredEffect> getTriggeredAbilities();

	Terrain addTriggeredAbilities(TriggeredEffect... triggeredAbilities);

	Terrain removeTriggeredAbilities(TriggeredEffect... triggeredAbilities);

	ModifiableTerrain removeTriggeredAbilitiesIf(Predicate<TriggeredEffect> predicate);

	List<? extends InteractionModifier<Double>> getOffensiveModifiers();

	Terrain addOffensiveModifiers(InteractionModifier<Double>... offensiveModifiers);

	Terrain removeOffensiveModifiers(InteractionModifier<Double>... offensiveModifiers);

	List<? extends InteractionModifier<Double>> getDefensiveModifiers();

	Terrain addDefensiveModifiers(InteractionModifier<Double>... defensiveModifiers);

	Terrain removeDefensiveModifiers(InteractionModifier<Double>... defensiveModifiers);

	int getDefaultMoveCost();

	public void addAbility(Ability ability);

	@Override
	ModifiableTerrain copy();

	@Override
	Terrain setSoundPath(String path);
}
