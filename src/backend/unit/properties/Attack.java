package backend.unit.properties;

import backend.game_engine.GameState;
import backend.unit.UnitInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Timmy
 *
 * @author Created by th174 on 3/27/2017.
 */
public final class Attack implements ActiveAbility.AbilityEffect<UnitInstance> {
    private final double damage;
    private final int numHits;
    private final List<InteractionModifier<Double>> damageModifiers;

    public Attack(double damage, int numHits) {
        this(damage, numHits, new ArrayList<>());
    }

    public Attack(double damage, int numHits, List<InteractionModifier<Double>> damageModifiers) {
        this.damage = damage;
        this.numHits = numHits;
        this.damageModifiers = damageModifiers;
    }

    public int getNumHits() {
        return numHits;
    }

    public double getBaseDamage() {
        return damage;
    }

    public double getDamage(UnitInstance user, UnitInstance target, GameState game) {
        return InteractionModifier.modifyAll(damageModifiers, getBaseDamage(), user, target, game);
    }

    @Override
    public void useAbility(UnitInstance user, UnitInstance target, GameState game) {
        for (int i = 0; i < getNumHits(); i++) {
            double attackDamage = InteractionModifier.modifyAll(user.getOffensiveModifiers(), getDamage(user, target, game), user, target, game);
            double totalDamage = InteractionModifier.modifyAll(target.getDefensiveModifiers(), attackDamage, user, target, game);
            target.getHitPoints().takeDamage(totalDamage);
        }
    }
}

