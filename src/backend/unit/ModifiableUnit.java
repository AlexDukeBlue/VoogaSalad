package backend.unit;

import backend.cell.Cell;
import backend.cell.Terrain;
import backend.grid.GridPattern;
import backend.player.Player;
import backend.unit.properties.*;
import backend.util.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class ModifiableUnit extends ModifiableVoogaObject<ModifiableUnit> implements Unit {
	//TODO ResourceBundlify
	public static final Unit SKELETON_WARRIOR = new ModifiableUnit("X")
			.addUnitStats(ModifiableUnitStat.HITPOINTS.setMaxValue(39.0), ModifiableUnitStat.MOVEPOINTS.setMaxValue(5))
			.setDescription("Once a noble knight in service of his kingdom, the skeleton warrior once again takes up the blade for the lich king.")
			.setImgPath("resources/images/x.png")
			.setMovePattern(GridPattern.HEXAGONAL_ADJACENT)
			.addActiveAbilities(ActiveAbility.SWORD)
			.addOffensiveModifiers(InteractionModifier.CHAOTIC);
	public static final Unit SKELETON_ARCHER = new ModifiableUnit("O")
			.addUnitStats(ModifiableUnitStat.HITPOINTS.setMaxValue(34.0))
			.addUnitStats(ModifiableUnitStat.MOVEPOINTS.setMaxValue(6))
			.setMovePattern(GridPattern.HEXAGONAL_ADJACENT)
			.setImgPath("resources/images/o.png")
			.setDescription("The skeletal corpse of an impoverished serf left to starve, reanimated by necromancy. Now, bow and arrow in hand, he pursues his revenge on the living.")
			.addOffensiveModifiers(InteractionModifier.CHAOTIC)
			.addActiveAbilities(ActiveAbility.BOW);
	
	

	private final ActiveAbilitySet activeAbilities;
	private final TriggeredAbilitySet triggeredAbilities;
	private final OffensiveModifierSet offensiveModifiers;
	private final DefensiveModifierSet defensiveModifiers;
	private final Map<Terrain, Integer> terrainMoveCosts;
	private final UnitStats stats;
	private GridPattern movePattern;
	private Faction faction;
	private Player ownerPlayer;
	private Cell currentCell;
	private boolean isVisible;

	public ModifiableUnit(String unitTemplateName) {
		this(unitTemplateName, Collections.emptySet(), null, null, Collections.emptyMap(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), "", "");
	}

	public ModifiableUnit(String unitTemplateName, Collection<? extends UnitStat> unitStats, Faction faction, GridPattern movePattern, Map<? extends Terrain, Integer> moveCosts, Collection<? extends ActiveAbility> activeAbilities, Collection<? extends TriggeredEffect> triggeredAbilities, Collection<? extends InteractionModifier<Double>> offensiveModifiers, Collection<? extends InteractionModifier<Double>> defensiveModifiers, String unitDescription, String imgPath) {
		super(unitTemplateName, unitDescription, imgPath);
		this.faction = faction;
		this.terrainMoveCosts = new HashMap<>(moveCosts);
		this.stats = new UnitStats(unitStats.parallelStream().map(UnitStat::copy).collect(Collectors.toList()));
		this.movePattern = movePattern;
		this.triggeredAbilities = new TriggeredAbilitySet(triggeredAbilities);
		this.activeAbilities = new ActiveAbilitySet(activeAbilities);
		this.offensiveModifiers = new OffensiveModifierSet(offensiveModifiers);
		this.defensiveModifiers = new DefensiveModifierSet(defensiveModifiers);
	}

	private void processTriggers(Event event, GameplayState gameState) {
		triggeredAbilities.forEach(e -> e.affect(this, event, gameState));
		triggeredAbilities.removeIf(TriggeredEffect::isExpired);
	}

	@Override
	public ModifiableUnit copy() {
		return new ModifiableUnit(getName(), getUnitStats(), getFaction(), getMovePattern(), getTerrainMoveCosts(), getActiveAbilities(), getTriggeredAbilities(), getOffensiveModifiers(), getDefensiveModifiers(), getDescription(), getImgPath());
	}

	@Override
	public void moveTo(Cell destinationCell, GameplayState gameState) {
		processTriggers(Event.UNIT_PRE_MOVEMENT, gameState);
		currentCell.leave(this, gameState);
		getMovePoints().setCurrentValue(getMovePoints().getCurrentValue() - getTerrainMoveCosts().get(destinationCell.getTerrain()));
		currentCell = destinationCell;
		currentCell.arrive(this, gameState);
		processTriggers(Event.UNIT_POST_MOVEMENT, gameState);
	}

	@Override
	public void startTurn(GameplayState gameState) {
		processTriggers(Event.TURN_START, gameState);
	}

	@Override
	public void endTurn(GameplayState gameState) {
		processTriggers(Event.TURN_END, gameState);
		getMovePoints().resetValue();
	}

	@Override
	public void takeDamage(double damage) {
		getHitPoints().setCurrentValue(getHitPoints().getCurrentValue() - damage);
	}

	@Override
	public void useActiveAbility(ActiveAbility activeAbility, VoogaEntity target, GameplayState gameState) {
		processTriggers(Event.UNIT_PRE_ABILITY_USE, gameState);
		activeAbility.affect(this, target, gameState);
		processTriggers(Event.UNIT_POST_ABILITY_USE, gameState);
	}

	@Override
	public final ActiveAbility getActiveAbilityByName(String name) {
		return activeAbilities.getByName(name);
	}

	@Override
	public final GridPattern getMovePattern() {
		return movePattern;
	}

	public final ModifiableUnit setMovePattern(GridPattern movePattern) {
		this.movePattern = movePattern;
		return this;
	}

	@Override
	public final Cell getCurrentCell() {
		return currentCell;
	}

	@Override
	public final Map<Terrain, Integer> getTerrainMoveCosts() {
		return Collections.unmodifiableMap(terrainMoveCosts);
	}
	
	public final ModifiableUnit setTerrainMoveCosts(Map<Terrain, Integer> terrainMoveCosts) {
		this.terrainMoveCosts.clear();
		terrainMoveCosts.keySet().stream()
				.forEach(terrain -> this.terrainMoveCosts.put(terrain, terrainMoveCosts.get(terrain)));
		return this;
	}

	@Override
	@SafeVarargs
	public final ModifiableUnit addOffensiveModifiers(InteractionModifier<Double>... modifiers) {
		return (ModifiableUnit) Unit.super.addOffensiveModifiers(modifiers);
	}

	@Override
	public final ModifiableUnit addOffensiveModifiers(Collection<InteractionModifier<Double>> modifiers) {
		offensiveModifiers.addAll(modifiers);
		return this;
	}

	@SafeVarargs
	@Override
	public final ModifiableUnit removeOffensiveModifiers(InteractionModifier<Double>... modifiers) {
		return (ModifiableUnit) Unit.super.removeOffensiveModifiers(modifiers);
	}

	@Override
	public final ModifiableUnit removeOffensiveModifiers(Collection<InteractionModifier<Double>> modifiers) {
		defensiveModifiers.removeAll(modifiers);
		return this;
	}

	@Override
	public final List<InteractionModifier<Double>> getOffensiveModifiers() {
		return Collections.unmodifiableList(offensiveModifiers.getAll());
	}

	@SafeVarargs
	@Override
	public final ModifiableUnit addDefensiveModifiers(InteractionModifier<Double>... modifiers) {
		return (ModifiableUnit) Unit.super.removeDefensiveModifiers(modifiers);
	}

	@Override
	public final ModifiableUnit addDefensiveModifiers(Collection<InteractionModifier<Double>> modifiers) {
		defensiveModifiers.addAll(modifiers);
		return this;
	}

	@SafeVarargs
	@Override
	public final ModifiableUnit removeDefensiveModifiers(InteractionModifier<Double>... modifiers) {
		return (ModifiableUnit) Unit.super.removeDefensiveModifiers(modifiers);
	}

	@Override
	public final ModifiableUnit removeDefensiveModifiers(Collection<InteractionModifier<Double>> modifiers) {
		defensiveModifiers.removeAll(modifiers);
		return this;
	}

	@Override
	public final List<InteractionModifier<Double>> getDefensiveModifiers() {
		return Collections.unmodifiableList(defensiveModifiers.getAll());
	}

	@Override
	public final Collection<ActiveAbility> getActiveAbilities() {
		return Collections.unmodifiableCollection(activeAbilities.getAll());
	}

	@Override
	public ModifiableUnit addActiveAbilities(ActiveAbility... abilities) {
		return (ModifiableUnit) Unit.super.addActiveAbilities(abilities);
	}

	@Override
	public final ModifiableUnit addActiveAbilities(Collection<ActiveAbility> abilities) {
		activeAbilities.addAll(abilities);
		return this;
	}

	@Override
	public ModifiableUnit removeActiveAbilities(ActiveAbility... abilities) {
		activeAbilities.removeAll(abilities);
		return this;
	}

	@Override
	public final ModifiableUnit removeActiveAbilities(Collection<ActiveAbility> abilities) {
		activeAbilities.removeAll(abilities);
		return this;
	}

	@Override
	public final Collection<TriggeredEffect> getTriggeredAbilities() {
		return Collections.unmodifiableCollection(triggeredAbilities.getAll());
	}

	@Override
	public ModifiableUnit addTriggeredAbilities(TriggeredEffect... abilities) {
		return (ModifiableUnit) Unit.super.addTriggeredAbilities(abilities);
	}

	@Override
	public final ModifiableUnit addTriggeredAbilities(Collection<TriggeredEffect> abilities) {
		triggeredAbilities.addAll(abilities);
		return this;
	}

	@Override
	public ModifiableUnit removeTriggeredAbilities(TriggeredEffect... abilities) {
		return (ModifiableUnit) Unit.super.removeTriggeredAbilities(abilities);
	}

	@Override
	public final ModifiableUnit removeTriggeredAbilities(Collection<TriggeredEffect> abilities) {
		triggeredAbilities.removeAll(abilities);
		return this;
	}

	@Override
	public UnitStat getUnitStat(String name) {
		return stats.getByName(name);
	}

	@Override
	public final Faction getFaction() {
		return faction;
	}

	public final ModifiableUnit setFaction(Faction faction) {
		this.faction = faction;
		return this;
	}

	@Override
	public final boolean isVisible() {
		return isVisible;
	}

	@Override
	public final void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	@Override
	public Player getOwner() {
		return ownerPlayer;
	}

	public void setOwner(Player p) {
		ownerPlayer = p;
	}

	public final void setCurrentCell(Cell currentCell) {
		this.currentCell = currentCell;
	}

	@Override
	public final Collection<? extends UnitStat> getUnitStats() {
		return stats.getAll();
	}

	public final ModifiableUnit addUnitStats(Collection<UnitStat> unitStats) {
		stats.addAll(unitStats);
		return this;
	}

	public final ModifiableUnit addUnitStats(UnitStat... unitStats) {
		stats.addAll(unitStats);
		return this;
	}

	public final ModifiableUnit removeUnitStats(Collection<UnitStat> unitStat) {
		stats.removeAll(unitStat);
		return this;
	}

	@Deprecated
	public static Collection<ModifiableUnit> getPredefinedUnits() {
		return getPredefined(ModifiableUnit.class);
	}
}