package backend.util;

import backend.unit.Unit;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Created by th174 on 3/31/2017.
 */
public class ModifiableTriggeredEffect extends ModifiableVoogaObject<ModifiableTriggeredEffect> implements TriggeredEffect {
	//Unit passive abilities
	public transient static final ModifiableTriggeredEffect REGENERATION = new ModifiableTriggeredEffect("Regeneration")
			.setEffect((unit, event, game) -> unit.takeDamage(-6))
			.setDescription("This unit regenerates 6 HP at the run of each turn.")
			.setImgPath("Regenerate.png")
			.addActivationTriggers(Event.TURN_START);
	public transient static final ModifiableTriggeredEffect HEALER = new ModifiableTriggeredEffect("Healer")
			.setEffect((unit, event, game) -> unit.getNeighboringUnits(game.getGrid()).values().forEach(c -> c.forEach(u -> u.takeDamage(-6))))
			.setDescription("This unit heals nearby units for 6 HP at the run of each turn.")
			.setImgPath("Red_Cross.png")
			.addActivationTriggers(Event.TURN_START);
	public transient static final ModifiableTriggeredEffect SHADOWSTALKER = new ModifiableTriggeredEffect("Shadowstalker")
			.setEffect((unit, event, game) -> unit.setVisible(event.equals(Event.UNIT_POST_ABILITY_USE) || game.getTurnNumber() % 6 != 4 || game.getTurnNumber() % 6 != 5))
			.setDescription("This unit hides in the shadows, rendering it invisible in nighttime")
			.setImgPath("Ninja.png")
			.addActivationTriggers(Event.values());
	public transient static final ModifiableTriggeredEffect POISONED = new ModifiableTriggeredEffect("Poisoned")
			.setEffect((unit, event, game) -> unit.getHitPoints().setCurrentValue(Math.min(1, unit.getHitPoints().getCurrentValue() - 4)))
			.setDuration(6)
			.setDescription("This unit is poisoned, and will lose 4 damage at the run and end of each turn, to a minimum of 1 HP. Lasts for 6 turns")
			.setImgPath("Drink_bleach.png")
			.addActivationTriggers(Event.TURN_START, Event.TURN_END);
	//Cell passive abilities
	public transient static final ModifiableTriggeredEffect FULL_HEAL = new ModifiableTriggeredEffect("Full Heal")
			.setEffect((occupant, event, game) -> occupant.getHitPoints().resetValue())
			.setDescription("Units occupying this cell have their HP fully restored at the run of each turn.")
			.setImgPath("Red_Cross.png")
			.addActivationTriggers(Event.TURN_START);
	public transient static final ModifiableTriggeredEffect POISON = new ModifiableTriggeredEffect("Poison")
			.setEffect((occupant, event, game) -> occupant.addTriggeredAbilities(ModifiableTriggeredEffect.POISONED))
			.setDuration(3)
			.setDescription("For the next three turns, units occupying this cell are poisoned at the end of their turn. Lasts for 3 turns.")
			.setImgPath("Drink_bleach.png")
			.addActivationTriggers(Event.TURN_END);
	public transient static final ModifiableTriggeredEffect ON_FIRE = new ModifiableTriggeredEffect("On Fire")
			.setEffect((occupant, event, game) -> occupant.takeDamage(10))
			.setDuration(2)
			.setDescription("For the next two turns, units that move through this cell at the start of their turn take 8 damage.")
			.setImgPath("My_mixtape.png")
			.addActivationTriggers(Event.UNIT_POST_MOVEMENT);

	private static final int DEFAULT_DURATION = Integer.MAX_VALUE;
	private Collection<Event> activationTriggers;
	private Effect effect;
	private int duration;
	private int turnsRemaining;

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
		return Arrays.asList(REGENERATION, HEALER, SHADOWSTALKER, POISONED);
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
		return new ModifiableTriggeredEffect(getName(), getEffect(), getDuration(), getDescription(), getImgPath(), getActivationTriggers());
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
}

