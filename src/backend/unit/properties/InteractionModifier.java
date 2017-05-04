package backend.unit.properties;

import backend.unit.Unit;
import backend.util.GameplayState;
import backend.util.ImmutableVoogaObject;
import backend.util.PassiveAbility;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Timmy
 *
 * @author Created by th174 on 3/28/2017.
 */
public class InteractionModifier<T> extends ImmutableVoogaObject<InteractionModifier<T>> implements PassiveAbility, Serializable {
	public static final String DEFENSIVE = "defense";
	public static final String OFFENSIVE = "offensive";

	public transient static final InteractionModifier<?> DUMMY = new InteractionModifier<>("Dummy", (originalValue, agent, target, game) -> originalValue, "Dummy modifier that doesn't change anything", "resources/images/blackScreen.png", DEFENSIVE),
			NO_EFFECT = new InteractionModifier<>("No effect", (originalValue, agent, target, game) -> 0.0, "Literally nothing", "resources/images/blackScreen.png", DEFENSIVE);
	//Offensive modifiers, can go on units or attacks
	public transient static final InteractionModifier<Double> CHAOTIC = new InteractionModifier<>("Chaotic", Modifier.CHAOTIC, "Attacks do more damage in nighttime, but less damage in daytime.", "resources/images/chaotic.png", OFFENSIVE),
			LAWFUL = new InteractionModifier<>("Lawful", Modifier.LAWFUL, "Attacks do more damage in daytime, but less damage in nighttime", "resources/images/sherrif.png", OFFENSIVE),
			BLINDED = new InteractionModifier<>("Blinded", Modifier.BLINDED, "Attacks have a high chance to miss", "resources/images/blind.png", OFFENSIVE),
			FIRST_BLOOD = new InteractionModifier<>("First Blood", Modifier.FIRST_BLOOD, "Attacks do extra damage to targets at full HP.", "resources/images/blood.png", OFFENSIVE),
			EXECUTIONER = new InteractionModifier<>("Executioner", Modifier.EXECUTIONER, "Attacks do extra damage to targets at low HP.", "resources/images/guillotine.png", OFFENSIVE),
			CRITICAL_STRIKE = new InteractionModifier<>("Critical Strike", Modifier.CRITICAL_STRIKE, "Attacks have a chance to critical strike, hitting for extra damage.", "resources/images/nuke.png", OFFENSIVE),
			BRAVERY = new InteractionModifier<>("Weakened", Modifier.BRAVERY, "Attacks do extra damage if the defender has more HP than the attacker.", "resources/images/slingshot.png", OFFENSIVE),
			ASSASSIN = new InteractionModifier<>("Assassin", Modifier.ASSASSIN, "Attacks do extra damage to isolated units with no nearby allies", "resources/images/assassin.png", OFFENSIVE);
	//Defensive modifiers, can go on units only
	public transient static final InteractionModifier<Double> INVULNERABILITY = new InteractionModifier<>("Invulnerability", Modifier.INVULNERABILITY, "This unit does not take damage.", "resources/images/god.png", DEFENSIVE),
			FORMATION = new InteractionModifier<>("Formation", Modifier.FORMATION, "This unit takes less damage when near an allied unit of the same type.", "resources/images/phalanx.jpg", DEFENSIVE),
			EVASIVE = new InteractionModifier<>("Evasive", Modifier.EVASIVE, "This unit has a high chance to evade attacks, but it takes extra damage when hit", "resources/images/evade.png", DEFENSIVE),
			STALWART = new InteractionModifier<>("Stalwart", Modifier.STALWART, "This unit takes less damage if it does not move this turn.", "resources/images/stalwart.png", DEFENSIVE),
			HARDENED_SHIELDS = new InteractionModifier<>("Hardened Shields", Modifier.HARDENED_SHIELDS, "Incoming attacks that would deal more than 5 damage have their damage reduced to 5", "resources/images/shield.png", DEFENSIVE),
			FEARFUL = new InteractionModifier<>("Fearful", Modifier.FEARFUL, "This unit take extra damage in night time", "resources/images/crow.png", DEFENSIVE),
			THORNS = new InteractionModifier<>("Thorns", Modifier.THORNS, "This unit reflects half the damage it takes back to the attacker", "resources/images/thorns.png", DEFENSIVE);
	//Also defensive, for use on terrains
	public transient static final InteractionModifier<Double>
			DEFAULT_FLAT_TERRAIN_DEFENSE = new InteractionModifier<>("Default Flat Terrain Defense", (originalValue, agent, target, game) -> game.random() < .7 ? originalValue : 0, "Units have 30% evasion on Flat terrain by default.", "resources/images/kansas.png", InteractionModifier.DEFENSIVE),
			DEFAULT_FOREST_TERRAIN_DEFENSE = new InteractionModifier<>("Default Forest Terrain Defense", (originalValue, agent, target, game) -> game.random() < .4 ? originalValue : 0, "Units have 60% evasion on Forest terrain by default.", "resources/images/forest.png", InteractionModifier.DEFENSIVE),
			DEFAULT_WATER_TERRAIN_DEFENSE = new InteractionModifier<>("Default Water Terrain Defense", (originalValue, agent, target, game) -> game.random() < .8 ? originalValue : 0, "Units have 20% evasion on Water terrain by default.", "resources/images/water.png", InteractionModifier.DEFENSIVE),
			DEFAULT_MOUNTAIN_TERRAIN_DEFENSE = new InteractionModifier<>("Default Mountain Terrain Defense", (originalValue, agent, target, game) -> game.random() < .5 ? originalValue : 0, "Units have 50% evasion on Mountain terrain by default.", "resources/images/mountain.png", InteractionModifier.DEFENSIVE),
			DEFAULT_FORTIFIED_TERRAIN_DEFENSE = new InteractionModifier<>("Default Fortified Terrain Defense", (originalValue, agent, target, game) -> game.random() < .3 ? originalValue : 0, "Units have 70% evasion on Fortified terrain by default.", "resources/images/castle.png", InteractionModifier.DEFENSIVE);

	private final Modifier<T> modifier;
	private final String type;

	public InteractionModifier(String name, Modifier<T> modifier) {
		this(name, modifier, "");
	}

	public InteractionModifier(String name, Modifier<T> modifier, String description) {
		this(name, modifier, description, "", "");
	}

	public InteractionModifier(String name, Modifier<T> modifier, String description, String imgPath, String type) {
		super(name, description, imgPath);
		this.modifier = modifier;
		this.type = type;
	}

	public static <T> T modifyAll(List<? extends InteractionModifier<T>> modifiers, T originalValue, Unit agent, Unit target, GameplayState game) {
		for (InteractionModifier<T> op : modifiers) {
			originalValue = op.modify(originalValue, agent, target, game);
		}
		return originalValue;
	}

	@Deprecated
	public static Collection<InteractionModifier> getPredefinedInteractionModifiers() {
		return getPredefined(InteractionModifier.class);
	}

	@Deprecated
	public static Collection<InteractionModifier> getPredefinedOffensiveModifiers() {
		return Arrays.asList(DUMMY, NO_EFFECT, CHAOTIC, LAWFUL, BLINDED, FIRST_BLOOD, EXECUTIONER, CRITICAL_STRIKE, BRAVERY, ASSASSIN);
	}

	@Deprecated
	public static Collection<InteractionModifier> getPredefinedDefensiveModifiers() {
		return Arrays.asList(DUMMY, NO_EFFECT, INVULNERABILITY, FORMATION, EVASIVE, STALWART, HARDENED_SHIELDS, FEARFUL, THORNS, DEFAULT_FLAT_TERRAIN_DEFENSE, DEFAULT_FOREST_TERRAIN_DEFENSE, DEFAULT_FORTIFIED_TERRAIN_DEFENSE, DEFAULT_MOUNTAIN_TERRAIN_DEFENSE, DEFAULT_WATER_TERRAIN_DEFENSE);
	}

	public T modify(T originalValue, Unit agent, Unit target, GameplayState game) {
		return modifier.modify(originalValue, agent, target, game);
	}

	@Override
	public InteractionModifier<T> copy() {
		return new InteractionModifier<>(getName(), modifier, getDescription(), getImgPath(), getType());
	}

	public String getType() {
		return type;
	}

	@FunctionalInterface
	public interface Modifier<T> extends Serializable {
		//Defensive Modifiers
		Modifier<Double> INVULNERABILITY = (incomingDamage, agent, target, game) -> 0.0;
		Modifier<Double> FORMATION = (incomingDamage, agent, target, game) -> incomingDamage * (target.getNeighboringUnits(game.getGrid()).values().parallelStream().flatMap(Collection::stream).anyMatch(e -> e.getTeam().equals(target.getTeam()) && e.getName().equals(target.getFormattedName())) ? .6 : 1);
		Modifier<Double> EVASIVE = (incomingDamage, agent, target, game) -> incomingDamage * game.random() < .5 ? 0 : 1.5;
		Modifier<Double> STALWART = (incomingDamage, agent, target, game) -> incomingDamage * (target.getMovePoints().isFull() ? .5 : 1);
		Modifier<Double> HARDENED_SHIELDS = (incomingDamage, agent, target, game) -> incomingDamage > 5 ? 5 : incomingDamage;
		Modifier<Double> FEARFUL = (incomingDamage, agent, target, game) -> incomingDamage * (game.getTurnNumber() % 6 == 4 || game.getTurnNumber() % 6 == 5 ? 1.25 : 1);
		Modifier<Double> THORNS = (incomingDamage, agent, target, game) -> {
			agent.takeDamage(incomingDamage / 2);
			return incomingDamage;
		};
		//Offensive Modifiers
		Modifier<Double> CHAOTIC = (outgoingDamage, agent, target, game) -> outgoingDamage * (game.getTurnNumber() % 6 == 1 || game.getTurnNumber() % 6 == 2 ? 0.75 : game.getTurnNumber() % 6 == 4 || game.getTurnNumber() % 6 == 5 ? 1.25 : 1);
		Modifier<Double> LAWFUL = (outgoingDamage, agent, target, game) -> outgoingDamage * (game.getTurnNumber() % 6 == 1 || game.getTurnNumber() % 6 == 2 ? 1.25 : game.getTurnNumber() % 6 == 4 || game.getTurnNumber() % 6 == 5 ? 0.75 : 1);
		Modifier<Double> BLINDED = (outgoingDamage, agent, target, game) -> game.random() < .5 ? outgoingDamage : 0;
		Modifier<Double> FIRST_BLOOD = (outgoingDamage, agent, target, game) -> outgoingDamage * (agent.getHitPoints().isFull() ? 1.5 : 1);
		Modifier<Double> EXECUTIONER = (outgoingDamage, agent, target, game) -> outgoingDamage * (agent.getHitPoints().getCurrentValue() / agent.getHitPoints().getMaxValue() < .25 ? 2 : 1);
		Modifier<Double> BRAVERY = (outgoingDamage, agent, target, game) -> outgoingDamage * (target.getHitPoints().getCurrentValue() > agent.getHitPoints().getCurrentValue() ? 1.5 : 1);
		Modifier<Double> ASSASSIN = (outgoingDamage, agent, target, game) -> outgoingDamage * (target.getNeighboringUnits(game.getGrid()).values().parallelStream().flatMap(Collection::stream).anyMatch(e -> e.getTeam().equals(target.getTeam())) ? 1 : 1.5);
		Modifier<Double> CRITICAL_STRIKE = (outgoingDamage, agent, target, game) -> Math.random() < .25 ? outgoingDamage * 2 : outgoingDamage;
		Modifier<Double> STRONG_ATTACK = (outgoingDamage, agent, target, game) -> outgoingDamage * 1.5;

		T modify(T originalValue, Unit agent, Unit target, GameplayState game);
	}
}