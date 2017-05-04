package backend.unit.properties;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
import backend.grid.GridPattern;
import backend.grid.Shape;
import backend.unit.Unit;
import backend.util.*;
import javafx.application.Platform;
import util.AlertFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Timmy
 *
 * @author Created by th174 on 3/29/2017.
 */
public class ActiveAbility<T extends VoogaEntity> extends ImmutableVoogaObject<ActiveAbility<T>> implements Ability, Serializable, HasShape, HasSound, HasPassiveModifiers {
	public transient static final ActiveAbility<Unit> CONSUME = new ActiveAbility<>("Consume", (user, target, game) -> {
		Cell targetCell = target.getCurrentCell();
		double damage = target.applyAllDefensiveModifiers(user.applyAllOffensiveModifiers(10.0, user, game), user, game);
		target.takeDamage(damage);
		if (target.getHitPoints().isEmpty()) {
			user.getHitPoints().setCurrentValue(Math.min(user.getHitPoints().getMaxValue(), user.getHitPoints().getCurrentValue() + damage));
			user.getCurrentCell().removeOccupants(user);
			targetCell.addOccupants(user);
		}
	}, GridPattern.SQUARE_ADJACENT, "The attacker deals damage to the defender. If the defender dies, the user heals for the amount of damage dealt, and the user takes the defender's place. (Does not count as movement)", "resources/images/consume.png");
	public transient static final ActiveAbility<Unit> SWORD = new ActiveAbility<>("Sword", new Attack(5, 3), GridPattern.SQUARE_ADJACENT, "The attacker hits 3 times for 5 damage on any neighboring unit", "resources/images/sword.png");
	public transient static final ActiveAbility<Unit> BOW = new ActiveAbility<>("Bow", new Attack(7, 2), GridPattern.SQUARE_RAYS, "The attacker hits 2 times for 7 dmage on any unit in a straight line away from the attacker", "resources/images/bow.png");
	public transient static final ActiveAbility<Unit> SUICIDE_SQUAD = new ActiveAbility<>("Suicide Squad, Attack!", (user, target, game) -> {
		user.getAllNeighboringUnits(game.getGrid()).parallelStream().filter(e -> e.getTeam() != user.getTeam()).forEach(u -> u.takeDamage(30));
		user.takeDamage(Integer.MAX_VALUE);
	}, GridPattern.SQUARE_ADJACENT, "The attacker sacrifices itself to deal massive damage to all neighboring enemy units.", "resources/images/explosion.png");
	public transient static final ActiveAbility<Unit> FULL_HEAL = new ActiveAbility<>("Full Heal", (user, target, game) -> target.getHitPoints().resetValue(), GridPattern.SQUARE_ADJACENT, "The attacker fully heals any neighboring unit", "resources/images/redCross.png");
	public transient static final ActiveAbility<Unit> BLIND = new ActiveAbility<>("Blind", (user, target, game) -> target.addOffensiveModifiers(InteractionModifier.BLINDED), GridPattern.SQUARE_ADJACENT, "The attacker gives any neighboring unit the Blinded modifier", "resources/images/blind.png");
	public transient static final ActiveAbility<Unit> SILENCE = new ActiveAbility<>("Silence", (user, target, game) -> target.getTriggeredAbilities().forEach(e -> e.setDuration(e.getRemainingTurns() - 1000)), GridPattern.SQUARE_ADJACENT, "Removes all temporary abilities from any neighboring unit", "resources/images/silence.png");
	public transient static final ActiveAbility<HasLocation> DROP_MIXTAPE = new ActiveAbility<>("Set Fire to Cell", (user, target, game) -> game.getGrid().get(target.getLocation()).addTriggeredAbilities(ModifiableTriggeredEffect.ON_FIRE), GridPattern.SQUARE_SINGLE_CELL, "The attacker sets fire to the cell they are occupying.", "resources/images/mixtape.png");

	public transient static final double DEFAULT_ABILITY_COST = 1;
	private final AbilityEffect<T> effect;
	private final GridPattern range;
	private final double cost;
	private String soundPath;

	public ActiveAbility(String name, AbilityEffect<T> effect, GridPattern range, String description, String imgPath) {
		this(name, effect, DEFAULT_ABILITY_COST, range, description, imgPath);
	}

	public ActiveAbility(String name, AbilityEffect<T> effect, double cost, GridPattern range, String description, String imgPath) {
		super(name, description, imgPath);
		this.range = range;
		this.effect = effect;
		this.cost = cost;
	}

	@Deprecated
	public static Collection<ActiveAbility> getPredefinedActiveAbilities() {
		return getPredefined(ActiveAbility.class);
	}

	public AbilityEffect<T> getAbilityEffect() {
		return effect;
	}

	public GridPattern getRange() {
		return range;
	}

	public Collection<CoordinateTuple> getLegalTargetCells(Unit user, ReadonlyGameplayState readonlyGameplayState) {
		return user.getAbilityPoints().getCurrentValue() >= getCost() ? getTargetCellsFrom(user.getLocation(), readonlyGameplayState) : Collections.emptyList();
	}

	public Collection<CoordinateTuple> getTargetCellsFrom(CoordinateTuple userLocation, ReadonlyGameplayState readonlyGameplayState) {
		return getRange().parallelStream()
				.filter(e -> Objects.nonNull(userLocation))
				.map(e -> readonlyGameplayState.getGrid().get(e.sum(userLocation)))
				.filter(Objects::nonNull)
				.map(Cell::getLocation)
				.collect(Collectors.toList());
	}

	public void affect(Unit user, T target, GameplayState game) {
		try {
			effect.useAbility(user, target, game);
			user.getAbilityPoints().setCurrentValue(user.getAbilityPoints().getCurrentValue() - cost);
		} catch (Exception e) {
			Platform.runLater(() -> AlertFactory.warningAlert("Illegal Move", "Attempted illegal move was ignored.", "The user attempted to make an illegal move, so it was ignored.").showAndWait());
		}
	}

	@Override
	public ActiveAbility<T> copy() {
		return new ActiveAbility<>(getName(), getAbilityEffect(), getRange(), getDescription(), getImgPath()).setSoundPath(getSoundPath());
	}

	@Override
	public Shape getShape() {
		return getRange().getShape();
	}

	@Override
	public String getSoundPath() {
		return soundPath;
	}

	@Override
	public ActiveAbility<T> setSoundPath(String path) {
		this.soundPath = path;
		return this;
	}

	public double getCost() {
		return cost;
	}

	@Override
	public List<? extends InteractionModifier> getOffensiveModifiers() {
		return effect instanceof Attack ? ((Attack) effect).getOffensiveModifiers() : Collections.emptyList();
	}

	@Override
	public List<? extends InteractionModifier> getDefensiveModifiers() {
		return Collections.emptyList();
	}

	@FunctionalInterface
	public interface AbilityEffect<T extends VoogaEntity> extends Serializable {
		void useAbility(Unit user, T target, GameplayState game);
	}
}
