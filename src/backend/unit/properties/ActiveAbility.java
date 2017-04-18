package backend.unit.properties;

import backend.cell.Terrain;
import backend.grid.GridPattern;
import backend.unit.Unit;
import backend.util.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Timmy
 *
 * @author Created by th174 on 3/29/2017.
 */
public class ActiveAbility<T extends VoogaEntity> extends ImmutableVoogaObject<ActiveAbility<T>> implements Ability,Serializable {
	//All hexagonal
	public transient static final ActiveAbility<Unit> SWORD = new ActiveAbility<>("Sword", new Attack(5, 3), GridPattern.HEXAGONAL_ADJACENT, "The attacker hits 3 times for 5 damage on any neighboring unit", "Sword.png");
	public transient static final ActiveAbility<Unit> BOW = new ActiveAbility<>("Bow", new Attack(7, 2), GridPattern.HEXAGONAL_RAYS, "The attacker hits 2 times for 7 dmage on any unit in a straight line away from the attacker", "Bow.png");
	public transient static final ActiveAbility<Unit> SUICIDE_SQUAD = new ActiveAbility<>("Suicide Squad, Attack!", (user, target, game) -> {
		user.getAllNeighboringUnits(game.getGrid()).parallelStream().filter(e -> e.getTeam() != user.getTeam()).forEach(u -> u.takeDamage(10));
		user.takeDamage(Integer.MAX_VALUE);
	}, GridPattern.HEXAGONAL_ADJACENT, "The attacker sacrifices itself to deal massive damage to all neighboring enemy units.", "Allahu_Akbar.png");
	public transient static final ActiveAbility<Unit> FULL_HEAL = new ActiveAbility<>("Full Heal", (user, target, game) -> target.getHitPoints().resetValue(), GridPattern.HEXAGONAL_ADJACENT, "The attacker fully heals any neighboring unit", "Red_Cross.png");
	public transient static final ActiveAbility<Unit> BLIND = new ActiveAbility<>("Blind", (user, target, game) -> target.addOffensiveModifiers(InteractionModifier.BLINDED), GridPattern.HEXAGONAL_ADJACENT, "The attacker gives any neighboring unit the Blinded modifier", "Helen_Keller.png");
	public transient static final ActiveAbility<Unit> SILENCE = new ActiveAbility<>("Silence", (user, target, game) -> Stream.of(target.getOffensiveModifiers(), target.getDefensiveModifiers(), target.getTriggeredAbilities()).forEach(Collection::clear), GridPattern.HEXAGONAL_ADJACENT, "Removes all offensive, defensive, and passive modifiers from any neighboring unit", "Silencer.png");
	public transient static final ActiveAbility<Terrain> DROP_MIXTAPE = new ActiveAbility<>("Set Fire to Cell", (user, target, game) -> target.addTriggeredAbilities(ModifiableTriggeredEffect.ON_FIRE), GridPattern.HEXAGONAL_SINGLE_CELL, "The attacker sets fire to the cell they are occupying.", "My_mixtape.png");

	private final AbilityEffect<T> effect;
	private final GridPattern range;

	public ActiveAbility(String name, AbilityEffect<T> effect, GridPattern range, String description, String imgPath) {
		super(name, description, imgPath);
		this.range = range;
		this.effect = effect;
	}

	public AbilityEffect<T> getAbilityEffect() {
		return effect;
	}

	public GridPattern getRange() {
		return range;
	}

	public void affect(Unit user, T target, GameplayState game) {
		effect.useAbility(user, target, game);
	}

	@Override
	public ActiveAbility<T> copy() {
		return new ActiveAbility<>(getName(), getAbilityEffect(), getRange(), getDescription(), getImgPath());
	}

	@Deprecated
	public static Collection<ActiveAbility> getPredefinedActiveAbilities() {
		return getPredefined(ActiveAbility.class);
	}

	@FunctionalInterface
	public interface AbilityEffect<T extends VoogaEntity> extends Serializable {
		void useAbility(Unit user, T target, GameplayState game);
	}
}
