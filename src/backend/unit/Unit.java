package backend.unit;

import backend.cell.Cell;
import backend.cell.Terrain;
import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.GridPattern;
import backend.grid.ModifiableGameBoard;
import backend.player.Player;
import backend.player.Team;
import backend.unit.properties.ActiveAbility;
import backend.unit.properties.Faction;
import backend.unit.properties.InteractionModifier;
import backend.unit.properties.UnitStat;
import backend.util.GameplayState;
import backend.util.TriggeredEffect;
import backend.util.VoogaEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Timmy
 *
 * @author Created by th174 on 3/27/2017.
 */
public interface Unit extends VoogaEntity {
	@Override
	Unit copy();

	void moveTo(Cell destinationCell, GameplayState gameState);

	void startTurn(GameplayState gameState);

	void endTurn(GameplayState gameState);

	void takeDamage(double damage);

	UnitStat getUnitStat(String name);

	Collection<? extends UnitStat> getUnitStats();

	default UnitStat<Integer> getMovePoints() {
		return getUnitStat("Movepoints");
	}

	default UnitStat<Double> getHitPoints() {
		return getUnitStat("Hitpoints");
	}

	default void useActiveAbility(String activeAbilityName, VoogaEntity target, GameplayState gameState) {
		useActiveAbility(getActiveAbilityByName(activeAbilityName), target, gameState);
	}

	void useActiveAbility(ActiveAbility activeAbility, VoogaEntity target, GameplayState gameState);

	ActiveAbility getActiveAbilityByName(String name);

	default Collection<Cell> getLegalMoves(ModifiableGameBoard grid) {
		return getMovePattern().getCoordinates().parallelStream()
				.map(e -> grid.get(e.sum(this.getLocation())))
				.filter(Objects::nonNull)
				.filter(e -> getTerrainMoveCosts().get(e.getTerrain()) < getMovePoints().getCurrentValue()).collect(Collectors.toSet());
	}

	GridPattern getMovePattern();

	default CoordinateTuple getLocation() {
		return getCurrentCell().getLocation();
	}

	Cell getCurrentCell();

	Map<Terrain, Integer> getTerrainMoveCosts();

	default Collection<Unit> getAllNeighboringUnits(GameBoard grid) {
		return getNeighboringUnits(grid).values().parallelStream().flatMap(Collection::stream).parallel().collect(Collectors.toSet());
	}

	default Map<CoordinateTuple, Collection<? extends Unit>> getNeighboringUnits(GameBoard grid) {
		Map<CoordinateTuple, Collection<? extends Unit>> neighbors = getCurrentCell().getNeighbors(grid).entrySet().parallelStream()
				.filter(e -> !e.getValue().getOccupants().isEmpty())
				.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getOccupants()));
		neighbors.put(CoordinateTuple.getOrigin(getCurrentCell().dimension()), getCurrentCell().getOccupants().parallelStream().filter(e -> !equals(e)).collect(Collectors.toSet()));
		return neighbors;
	}

	default Map<CoordinateTuple, Cell> getNeighboringCells(ModifiableGameBoard grid) {
		return getCurrentCell().getNeighbors(grid);
	}

	default Unit addOffensiveModifiers(InteractionModifier<Double>... modifiers) {
		return addOffensiveModifiers(Arrays.asList(modifiers));
	}

	Unit addOffensiveModifiers(Collection<InteractionModifier<Double>> modifiers);

	default Unit removeOffensiveModifiers(InteractionModifier<Double>... modifiers) {
		return removeOffensiveModifiers(Arrays.asList(modifiers));
	}

	Unit removeOffensiveModifiers(Collection<InteractionModifier<Double>> modifiers);

	default double applyAllOffensiveModifiers(Double originalValue, Unit target, GameplayState gameState) {
		Double temp = InteractionModifier.modifyAll(getOffensiveModifiers(), originalValue, this, target, gameState);
		return InteractionModifier.modifyAll(getCurrentCell().getTerrain().getOffensiveModifiers(), temp, this, target, gameState);
	}

	List<InteractionModifier<Double>> getOffensiveModifiers();

	default Unit addDefensiveModifiers(InteractionModifier<Double>... modifiers) {
		return addDefensiveModifiers(Arrays.asList(modifiers));
	}

	Unit addDefensiveModifiers(Collection<InteractionModifier<Double>> modifiers);

	default Unit removeDefensiveModifiers(InteractionModifier<Double>... modifiers) {
		return removeDefensiveModifiers(Arrays.asList(modifiers));
	}

	Unit removeDefensiveModifiers(Collection<InteractionModifier<Double>> modifiers);

	default double applyAllDefensiveModifiers(Double originalValue, Unit agent, GameplayState gameState) {
		Double temp = InteractionModifier.modifyAll(getDefensiveModifiers(), originalValue, agent, this, gameState);
		return InteractionModifier.modifyAll(getCurrentCell().getTerrain().getDefensiveModifiers(), temp, agent, this, gameState);
	}

	List<InteractionModifier<Double>> getDefensiveModifiers();

	Collection<? extends ActiveAbility> getActiveAbilities();

	default Unit addActiveAbilities(ActiveAbility... abilities) {
		return addActiveAbilities(Arrays.asList(abilities));
	}

	Unit addActiveAbilities(Collection<ActiveAbility> abilities);

	default Unit removeActiveAbilities(ActiveAbility... abilities) {
		return removeActiveAbilities(Arrays.asList(abilities));
	}

	Unit removeActiveAbilities(Collection<ActiveAbility> abilities);

	Collection<? extends TriggeredEffect> getTriggeredAbilities();

	default Unit addTriggeredAbilities(TriggeredEffect... abilities) {
		return addTriggeredAbilities(Arrays.asList(abilities));
	}

	Unit addTriggeredAbilities(Collection<TriggeredEffect> abilities);

	default Unit removeTriggeredAbilities(TriggeredEffect... abilities) {
		return removeTriggeredAbilities(Arrays.asList(abilities));
	}

	Unit removeTriggeredAbilities(Collection<TriggeredEffect> abilities);

	Faction getFaction();

	boolean isVisible();

	void setVisible(boolean isVisible);

	default int movePointsTo(CoordinateTuple other) {
		throw new RuntimeException("Not Implemented Yet");
	}

	default Team getTeam() {
		return getOwner().getTeam();
	}

	Player getOwner();
}