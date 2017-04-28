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
	//TODO: ResourceBundlify all this shit
	public transient static final InteractionModifier<?> DUMMY = new InteractionModifier<>("Dummy", (originalValue, agent, target, game) -> originalValue, "Dummy modifier that doesn't change anything", "resources/images/blackScreen.png", DEFENSIVE);
	public transient static final InteractionModifier<Double> NO_EFFECT = new InteractionModifier<>("No effect", (originalValue, agent, target, game) -> 0.0, "Literally nothing", "resources/images/blackScreen.png", DEFENSIVE);
	//Offensive modifiers, can go on units or attacks
	public transient static final InteractionModifier<Double> CHAOTIC = new InteractionModifier<>("Chaotic", Modifier.CHAOTIC, "Attacks do more damage in nighttime, but less damage in daytime.", "resources/images/chaotic.png", OFFENSIVE);
	public transient static final InteractionModifier<Double> LAWFUL = new InteractionModifier<>("Lawful", Modifier.LAWFUL, "Attacks do more damage in daytime, but less damage in nighttime", "resources/images/sherrif.png", OFFENSIVE);
	public transient static final InteractionModifier<Double> BLINDED = new InteractionModifier<>("Blinded", Modifier.BLINDED, "Attacks have a high chance to miss", "resources/images/blind.png", OFFENSIVE);
	public transient static final InteractionModifier<Double> FIRST_BLOOD = new InteractionModifier<>("First Blood", Modifier.FIRST_BLOOD, "Attacks do extra damage to targets at full HP.", "resources/images/blood.png", OFFENSIVE);
	public transient static final InteractionModifier<Double> EXECUTIONER = new InteractionModifier<>("Executioner", Modifier.EXECUTIONER, "Attacks do extra damage to targets at low HP.", "resources/images/guillotine.png", OFFENSIVE);
	public transient static final InteractionModifier<Double> CRITICAL_STRIKE = new InteractionModifier<>("Critical Strike", Modifier.CRITICAL_STRIKE, "Attacks have a chance to critical strike, hitting for extra damage.", "resources/images/nuke.png", OFFENSIVE);
	public transient static final InteractionModifier<Double> BRAVERY = new InteractionModifier<>("Weakened", Modifier.BRAVERY, "Attacks do extra damage if the defender has more HP than the attacker.", "resources/images/slingshot.png", OFFENSIVE);
	public transient static final InteractionModifier<Double> ASSASSIN = new InteractionModifier<>("Assassin", Modifier.ASSASSIN, "Attacks do extra damage to isolated units with no nearby allies", "resources/images/assassin.png", OFFENSIVE);
	//Defensive modifiers, can go on units only
	public transient static final InteractionModifier<Double> INVULNERABILITY = new InteractionModifier<>("Invulnerability", Modifier.INVULNERABILITY, "This unit does not take damage.", "resources/images/god.png", DEFENSIVE);
	public transient static final InteractionModifier<Double> FORMATION = new InteractionModifier<>("Formation", Modifier.FORMATION, "This unit takes less damage when near an allied unit of the same type.", "resources/images/phalanx.jpg", DEFENSIVE);
	public transient static final InteractionModifier<Double> EVASIVE = new InteractionModifier<>("Evasive", Modifier.EVASIVE, "This unit has a high chance to evade attacks, but it takes extra damage when hit", "resources/images/evade.png", DEFENSIVE);
	public transient static final InteractionModifier<Double> STALWART = new InteractionModifier<>("Stalwart", Modifier.STALWART, "This unit takes less damage if it does not move this turn.", "resources/images/stalwart.png", DEFENSIVE);
	public transient static final InteractionModifier<Double> HARDENED_SHIELDS = new InteractionModifier<>("Hardened Shields", Modifier.HARDENED_SHIELDS, "Incoming attacks that would deal more than 5 damage have their damage reduced to 5", "resources/images/shield.png", DEFENSIVE);
	public transient static final InteractionModifier<Double> FEARFUL = new InteractionModifier<>("Fearful", Modifier.FEARFUL, "This unit take extra damage in night time", "resources/images/crow.png", DEFENSIVE);
	public transient static final InteractionModifier<Double> THORNS = new InteractionModifier<>("Thorns", Modifier.THORNS, "This unit reflects half the damage it takes back to the attacker", "resources/images/thorns.png", DEFENSIVE);

	private final Modifier<T> modifier;
	private String type;

	public InteractionModifier(String name, Modifier<T> modifier) {
		this(name, modifier, "");
	}

	public InteractionModifier(String name, Modifier<T> modifier, String description) {
		this(name, modifier, description, "", "");
	}

	public InteractionModifier(String name, Modifier<T> modifier, String description, String imgPath, String type) {
		super(name, description, imgPath);
		this.modifier = modifier;
		setType(type);
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
		return Arrays.asList(CHAOTIC, LAWFUL, BLINDED, FIRST_BLOOD, EXECUTIONER, CRITICAL_STRIKE, BRAVERY, ASSASSIN);
	}

	@Deprecated
	public static Collection<InteractionModifier> getPredefinedDefensiveModifiers() {
		return Arrays.asList(INVULNERABILITY, FORMATION, EVASIVE, STALWART, HARDENED_SHIELDS, FEARFUL, THORNS);
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

	public void setType(String type) {
		this.type = type;
	}

	@FunctionalInterface
	public interface Modifier<T> extends Serializable {
		//Defensive Modifiers
		Modifier<Double> INVULNERABILITY = (incomingDamage, agent, target, game) -> 0.0;
		Modifier<Double> FORMATION = (incomingDamage, agent, target, game) -> incomingDamage * (target.getNeighboringUnits(game.getGrid()).values().parallelStream().flatMap(Collection::stream).anyMatch(e -> e.getTeam().equals(target.getTeam()) && e.getName().equals(target.getName())) ? .6 : 1);
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