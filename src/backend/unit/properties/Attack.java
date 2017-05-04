package backend.unit.properties;

import backend.unit.Unit;
import backend.util.GameplayState;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Timmy
 *
 * @author Created by th174 on 3/27/2017.
 */
public final class Attack implements ActiveAbility.AbilityEffect<Unit>, Serializable {
	private final double damage;
	private final int numHits;
	private final List<InteractionModifier<Double>> damageModifiers;

	public Attack(double damage, int numHits) {
		this(damage, numHits, Collections.emptyList());
	}

	public Attack(double damage, int numHits, Collection<InteractionModifier<Double>> damageModifiers) {
		this.damage = damage;
		this.numHits = numHits;
		this.damageModifiers = new ArrayList<>(damageModifiers);
	}

	public int getNumHits() {
		return numHits;
	}

	public double getDamage(Unit user, Unit target, GameplayState game) {
		return InteractionModifier.modifyAll(damageModifiers, getBaseDamage(), user, target, game);
	}

	public double getBaseDamage() {
		return damage;
	}

	public List<InteractionModifier<Double>> getOffensiveModifiers(){
		return Collections.unmodifiableList(damageModifiers);
	}

	@Override
	public void useAbility(Unit user, Unit target, GameplayState game) {
		Optional<? extends Attack> retaliationAttack = target.getActiveAbilities().stream()
				.filter(e -> e.getTargetCellsFrom(target.getLocation(), game).contains(user.getCurrentCell().getLocation()))
				.map(ActiveAbility::getAbilityEffect)
				.filter(Attack.class::isInstance)
				.map(Attack.class::cast).findAny();
		int numHits = getNumHits();
		if (retaliationAttack.isPresent()) {
			numHits = Math.max(getNumHits(), retaliationAttack.get().getNumHits());
		}
		IntStream.range(0, numHits).forEach(i -> {
			if (i < getNumHits() && !target.getHitPoints().isEmpty() && !user.getHitPoints().isEmpty()) {
				singleHit(user, target, game);
			}
			if (retaliationAttack.isPresent() && i < retaliationAttack.get().getNumHits() && !target.getHitPoints().isEmpty() && !user.getHitPoints().isEmpty()) {
				retaliationAttack.get().singleHit(target, user, game);
			}
		});
	}

	void singleHit(Unit user, Unit target, GameplayState game) {
		double attackDamage = user.applyAllOffensiveModifiers(getDamage(user, target, game), target, game);
		double totalDamage = target.applyAllDefensiveModifiers(attackDamage, user, game);
		target.takeDamage(totalDamage);
	}
}

