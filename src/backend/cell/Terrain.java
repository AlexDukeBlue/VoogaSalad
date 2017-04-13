package backend.cell;

import backend.unit.properties.InteractionModifier;
import backend.util.TriggeredEffect;
import backend.util.VoogaEntity;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static backend.util.ImmutableVoogaObject.getPredefined;

/**
 * @author Created by th174 on 4/12/2017.
 */
public interface Terrain extends VoogaEntity {
	int IMPASSABLE = Integer.MAX_VALUE;
	Terrain EMPTY = new ModifiableTerrain("Empty")
			.setDefaultMoveCost(IMPASSABLE)
			.setDescription("Literally nothing")
			.setImgPath("resources/images/blackScreen.jpg");
	Terrain FLAT = new ModifiableTerrain("Flat")
			.setDefaultMoveCost(1)
			.addDefensiveModifiers(new InteractionModifier<>("Default Flat Terrain Defense", (originalValue, agent, target, game) -> game.random() < .7 ? originalValue : 0, "Units have 30% evasion on Flat terrain by default."))
			.setDescription("Open, flat, land that offers little defensive cover, but allows for easy movement.")
			.setImgPath("resources/images/kansas.jpg");
	Terrain FOREST = new ModifiableTerrain("Forest")
			.setDefaultMoveCost(2)
			.addDefensiveModifiers(new InteractionModifier<>("Default Forest Terrain Defense", (originalValue, agent, target, game) -> game.random() < .4 ? originalValue : 0, "Units have 60% evasion on Forest terrain by default."))
			.setDescription("Thick forest that offers plenty of cover, but makes navigating difficult.")
			.setImgPath("resources/images/forest.jpg");
	Terrain WATER = new ModifiableTerrain("Water")
			.setDefaultMoveCost(3)
			.addDefensiveModifiers(new InteractionModifier<>("Default Water Terrain Defense", (originalValue, agent, target, game) -> game.random() < .8 ? originalValue : 0, "Units have 20% evasion on Water terrain by default."))
			.setDescription("Water that impedes movement for non-aquatic units")
			.setImgPath("resources/images/water.jpg");
	Terrain MOUNTAIN = new ModifiableTerrain("Mountain")
			.setDefaultMoveCost(2)
			.addDefensiveModifiers(new InteractionModifier<>("Default Mountain Terrain Defense", (originalValue, agent, target, game) -> game.random() < .5 ? originalValue : 0, "Units have 50% evasion on Mountain terrain by default."))
			.setDescription("Rugged mountains that are difficult to navigate through")
			.setImgPath("resources/images/mountain.jpg");
	Terrain FORTIFIED = new ModifiableTerrain("Fortified")
			.setDefaultMoveCost(1)
			.addDefensiveModifiers(new InteractionModifier<>("Default Fortified Terrain Defense", (originalValue, agent, target, game) -> game.random() < .3 ? originalValue : 0, "Units have 70% evasion on Fortified terrain by default."))
			.setDescription("A fortified defensive position")
			.setImgPath("resources/images/castle.jpg");

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

	@Override
	ModifiableTerrain copy();

	@Deprecated
	public static Collection<? extends Terrain> getPredefinedTerrain() {
		return getPredefined(Terrain.class);
	}
}
