package backend.util;

import backend.unit.Unit;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * @author Created by th174 on 3/31/2017.
 */
public class ModifiableTriggeredEffect extends ModifiableVoogaObject<ModifiableTriggeredEffect> implements TriggeredEffect {
	private static final long serialVersionUID = 1L;
	//Unit passive abilities
	public transient static final ModifiableTriggeredEffect REGENERATION = new ModifiableTriggeredEffect("Regeneration")
			.setEffect((unit, event, game) -> unit.takeDamage(-6))
			.setDescription("This unit regenerates 6 HP at the run of each turn.")
			.setImgPath("resources/images/redCross.png")
			.addActivationTriggers(Event.TURN_START);
	public transient static final ModifiableTriggeredEffect HEALER = new ModifiableTriggeredEffect("Healer")
			.setEffect((unit, event, game) -> unit.getNeighboringUnits(game.getGrid()).values().forEach(c -> c.forEach(u -> u.takeDamage(-6))))
			.setDescription("This unit heals nearby units for 6 HP at the run of each turn.")
			.setImgPath("resources/images/redCross.png")
			.addActivationTriggers(Event.TURN_START);
	public transient static final ModifiableTriggeredEffect POISONED = new ModifiableTriggeredEffect("Poisoned")
			.setEffect((unit, event, game) -> unit.getHitPoints().setCurrentValue(Math.max(1, unit.getHitPoints().getCurrentValue() - 4)))
			.setDuration(6)
			.setDescription("This unit is poisoned, and will lose 4 damage at the run and end of each turn, to a minimum of 1 HP. Lasts for 6 turns")
			.setImgPath("resources/images/poison.png")
			.addActivationTriggers(Event.TURN_START, Event.TURN_END);
	public transient static final ModifiableTriggeredEffect RESET_MOVE_POINTS = new ModifiableTriggeredEffect("Reset Move Points")
			.setEffect((unit, event, game) -> unit.getMovePoints().resetValue())
			.setDescription("This unit regains full movepoints at the beginning of each turn.")
			.setImgPath("resources/images/timbs.png")
			.addActivationTriggers(Event.TURN_START);
	public transient static final ModifiableTriggeredEffect RESET_ABILITY_POINTS = new ModifiableTriggeredEffect("Reset Ability Points")
			.setEffect((unit, event, game) -> unit.getAbilityPoints().resetValue())
			.setDescription("This unit regains full ability points at the beginning of each turn.")
			.setImgPath("resources/images/ability_points.png")
			.addActivationTriggers(Event.TURN_START);
	public transient static final ModifiableTriggeredEffect REGENERATE_ENERGY_POINTS = new ModifiableTriggeredEffect("Regenerate Energy Points")
			.setEffect((unit, event, game) -> unit.getEnergy().setCurrentValue(Math.max(unit.getEnergy().getMaxValue(), unit.getEnergy().getCurrentValue() + 10)))
			.setDescription("This unit regenerates some energy points at the beginning of each turn.")
			.setImgPath("resources/images/energy.png")
			.addActivationTriggers(Event.TURN_START);
	//Cell passive abilities
	public transient static final ModifiableTriggeredEffect FULL_HEAL = new ModifiableTriggeredEffect("Full Heal")
			.setEffect((occupant, event, game) -> occupant.getHitPoints().resetValue())
			.setDescription("Units occupying this cell have their HP fully restored at the run of each turn.")
			.setImgPath("resources/images/redCross.png")
			.addActivationTriggers(Event.TURN_START);
	public transient static final ModifiableTriggeredEffect POISON = new ModifiableTriggeredEffect("Poison")
			.setEffect((occupant, event, game) -> occupant.addTriggeredAbilities(ModifiableTriggeredEffect.POISONED))
			.setDuration(3)
			.setDescription("For the next three turns, units occupying this cell are poisoned at the end of their turn. Lasts for 3 turns.")
			.setImgPath("resources/images/poison.png")
			.addActivationTriggers(Event.TURN_END);
	public transient static final ModifiableTriggeredEffect ON_FIRE = new ModifiableTriggeredEffect("On Fire")
			.setEffect((occupant, event, game) -> occupant.takeDamage(10))
			.setDuration(2)
			.setDescription("For the next two turns, units that move through this cell at the start of their turn take 8 damage.")
			.setImgPath("resources/images/mixtape.png")
			.addActivationTriggers(Event.UNIT_POST_MOVEMENT);

	private static final int DEFAULT_DURATION = Integer.MAX_VALUE;
	private Collection<Event> activationTriggers;
	private Effect effect;
	private int duration;
	private int turnsRemaining;
	private String soundPath;

	public ModifiableTriggeredEffect(String name) {
		this(name, null, "", "");
	}

	public ModifiableTriggeredEffect(String name, Effect effect, String description, String imgPath, Event... activationTriggers) {
		this(name, effect, DEFAULT_DURATION, description, imgPath, activationTriggers);
	}

	public ModifiableTriggeredEffect(String name, Effect effect, int numTurns, String description, String imgPath, Event... activationTriggers) {
		this(name, effect, numTurns, description, imgPath, Arrays.asList(activationTriggers));
	}

	public ModifiableTriggeredEffect(String name, Effect effect, int numTurns, String description, String imgPath, Collection<Event> activationTriggers) {
		super(name, description, imgPath);
		this.effect = effect;
		this.activationTriggers = new HashSet<>(activationTriggers);
		this.duration = numTurns;
		this.turnsRemaining = duration;
	}

	@Deprecated
	public static Collection<ModifiableTriggeredEffect> getPredefinedTriggeredUnitAbilities() {
		return Arrays.asList(REGENERATION, HEALER, POISONED, RESET_ABILITY_POINTS, RESET_MOVE_POINTS, REGENERATE_ENERGY_POINTS);
	}

	@Deprecated
	public static Collection<ModifiableTriggeredEffect> getPredefinedTriggeredCellAbilities() {
		return Arrays.asList(FULL_HEAL, POISON, ON_FIRE);
	}

	public Effect getEffect() {
		return this.effect;
	}

	public ModifiableTriggeredEffect setEffect(Effect effect) {
		this.effect = effect;
		return this;
	}

	public int getDuration() {
		return duration;
	}

	@Override
	public ModifiableTriggeredEffect setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public ModifiableTriggeredEffect addActivationTriggers(Event... activationTriggers) {
		return addActivationTriggers(Arrays.asList(activationTriggers));
	}

	public ModifiableTriggeredEffect addActivationTriggers(Collection<Event> activationTriggers) {
		this.activationTriggers.addAll(activationTriggers);
		return this;
	}

	@Override
	public ModifiableTriggeredEffect copy() {
		if (getName().length() < 1 || Objects.isNull(getEffect()) || getActivationTriggers().isEmpty()) {
			throw new IncompleteTriggeredEffectException();
		}
		return new ModifiableTriggeredEffect(getName(), getEffect(), getDuration(), getDescription(), getImgPath(), getActivationTriggers()).setSoundPath(getSoundPath());
	}

	@Override
	public int getRemainingTurns() {
		return turnsRemaining;
	}

	@Override
	public final void affect(Unit unit, Event event, GameplayState game) {
		if (getActivationTriggers().contains(event)) {
			effect.affect(unit, event, game);
		}
		if (event.equals(Event.TURN_END)) {
			turnsRemaining--;
		}
	}

	@Override
	public Collection<Event> getActivationTriggers() {
		return activationTriggers;
	}

	@Override
	public boolean isExpired() {
		return getRemainingTurns() <= 0;
	}

	@Override
	public ModifiableTriggeredEffect setSoundPath(String path) {
		this.soundPath = path;
		return this;
	}

	@Override
	public String getSoundPath() {
		return soundPath;
	}

	private static class IncompleteTriggeredEffectException extends RuntimeException {
		IncompleteTriggeredEffectException() {
			super("Incomplete Triggered Effect");
		}
	}
}

