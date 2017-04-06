package backend.unit;

import backend.cell.CellInstance;
import backend.cell.Terrain;
import backend.grid.CoordinateTuple;
import backend.grid.MutableGrid;
import backend.player.Player;
import backend.player.Team;
import backend.unit.properties.*;
import backend.util.*;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Timmy
 *
 * @author Created by th174 on 3/27/2017.
 */
public class UnitInstance extends VoogaInstance<UnitTemplate> {
    private final HitPoints hitPoints;
    private final MovePoints movePoints;
    private final GridPattern movePattern;
    private final Map<String, ActiveAbility<VoogaObject>> activeAbilities;
    private final Map<String, TriggeredEffectInstance> triggeredAbilities;
    private final List<InteractionModifier<Double>> offensiveModifiers;
    private final List<InteractionModifier<Double>> defensiveModifiers;
    private final Map<Terrain, Integer> moveCosts;
    private final Faction faction;

    private Player ownerPlayer;
    private CellInstance currentCell;
    private boolean isVisible;

    protected UnitInstance(String unitName, UnitTemplate unitTemplate, Player ownerPlayer, CellInstance startingCell) {
        super(unitName, unitTemplate);
        this.faction = unitTemplate.getFaction();
        this.hitPoints = unitTemplate.getHitPoints();
        this.movePoints = unitTemplate.getMovePoints();
        this.movePattern = unitTemplate.getMovePattern();
        this.moveCosts = new HashMap<>(unitTemplate.getTerrainMoveCosts());
        this.triggeredAbilities = unitTemplate.getTriggeredAbilities().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().createInstance()));
        this.activeAbilities = new HashMap<>(unitTemplate.getActiveAbilities());
        this.offensiveModifiers = new ArrayList<>(unitTemplate.getOffensiveModifiers());
        this.defensiveModifiers = new ArrayList<>(unitTemplate.getDefensiveModifiers());
        setOwner(ownerPlayer);
        setCurrentCell(startingCell);
        setVisible(true);
    }

    public void moveTo(CellInstance cell, ImmutableGameState gameState) {
        processTriggers(Event.UNIT_PRE_MOVEMENT, gameState);
        currentCell.leave(this, gameState);
        movePoints.useMovePoints(moveCosts.get(cell.getTerrain()));
        currentCell = cell;
        currentCell.arrive(this, gameState);
        processTriggers(Event.UNIT_POST_MOVEMENT, gameState);
    }

    public void startTurn(GameState gameState) {
        processTriggers(Event.TURN_START, gameState);
    }

    public void endTurn(GameState gameState) {
        processTriggers(Event.TURN_END, gameState);
        movePoints.resetValue();
    }

    public void takeDamage(double damage) {
        getHitPoints().takeDamage(damage);
    }

    public void useActiveAbility(String activeAbilityName, VoogaInstance target, ImmutableGameState gameState) {
        useActiveAbility(getActiveAbilityByName(activeAbilityName), target, gameState);
    }

    private ActiveAbility<VoogaObject> getActiveAbilityByName(String activeAbilityName) {
        return activeAbilities.get(activeAbilityName);
    }

    public void useActiveAbility(ActiveAbility<VoogaObject> activeAbility, VoogaInstance target, ImmutableGameState gameState) {
        processTriggers(Event.UNIT_PRE_ABILITY_USE, gameState);
        activeAbility.affect(this, target, gameState);
        processTriggers(Event.UNIT_POST_ABILITY_USE, gameState);
    }

    private void processTriggers(Event event, ImmutableGameState gameState) {
        triggeredAbilities.values().forEach(e -> e.affect(this, event, gameState));
        triggeredAbilities.values().removeIf(TriggeredEffectInstance::isExpired);
    }

    public Collection<CellInstance> getLegalMoves(MutableGrid grid) {
        return movePattern.getCoordinates().parallelStream()
                .map(e -> grid.get(e.sum(this.getLocation())))
                .filter(Objects::nonNull)
                .filter(e -> moveCosts.get(e.getTerrain()) < movePoints.getCurrentValue()).collect(Collectors.toSet());
    }

    public CellInstance getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(CellInstance currentCell) {
        this.currentCell = currentCell;
    }

    public Map<CoordinateTuple, Collection<UnitInstance>> getNeighboringUnits(MutableGrid grid) {
        Map<CoordinateTuple, Collection<UnitInstance>> neighbors = currentCell.getNeighbors(grid).entrySet().parallelStream()
                .map(e -> new Pair<>(e.getKey(), e.getValue().getOccupants()))
                .filter(e -> !e.getValue().isEmpty())
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        neighbors.put(CoordinateTuple.getOrigin(currentCell.dimension()), currentCell.getOccupants().parallelStream().filter(e -> !equals(e)).collect(Collectors.toSet()));
        return neighbors;
    }

    public Collection<UnitInstance> getAllNeighboringUnits(MutableGrid grid) {
        return getNeighboringUnits(grid).values().parallelStream().flatMap(Collection::stream).parallel().collect(Collectors.toSet());
    }

    public Map<CoordinateTuple, CellInstance> getNeighboringCells(MutableGrid grid) {
        return currentCell.getNeighbors(grid);
    }

    public CoordinateTuple getLocation() {
        return currentCell.getCoordinates();
    }

    public Player getOwner() {
        return ownerPlayer;
    }

    public void setOwner(Player p) {
        ownerPlayer = p;
    }

    public Team getTeam() {
        return ownerPlayer.getTeam();
    }

    public List<InteractionModifier<Double>> getOffensiveModifiers() {
        return offensiveModifiers;
    }

    public boolean addOffensiveModifier(InteractionModifier<Double> modifier) {
        return this.offensiveModifiers.add(modifier);
    }

    public double applyAllOffensiveModifiers(Double originalValue, UnitInstance target, ImmutableGameState gameState) {
        return InteractionModifier.modifyAll(getOffensiveModifiers(), originalValue, this, target, gameState);
    }

    public List<InteractionModifier<Double>> getDefensiveModifiers() {
        return defensiveModifiers;
    }

    public boolean addDefensiveModifier(InteractionModifier<Double> modifier) {
        return defensiveModifiers.add(modifier);
    }

    public double applyAllDefensiveModifiers(Double originalValue, UnitInstance agent, ImmutableGameState gameState) {
        return InteractionModifier.modifyAll(getDefensiveModifiers(), originalValue, agent, this, gameState);
    }

    public Map<String, ActiveAbility<VoogaObject>> getActiveAbilities() {
        return activeAbilities;
    }

    public Map<String, TriggeredEffectInstance> getTriggeredAbilities() {
        return triggeredAbilities;
    }

    public void addTriggeredAbility(TriggeredEffectInstance instance) {
        triggeredAbilities.put(instance.getName(),instance);
    }

    public HitPoints getHitPoints() {
        return hitPoints;
    }

    public MovePoints getMovePoints() {
        return movePoints;
    }

    public Faction getFaction() {
        return faction;
    }

    public GridPattern getMovePattern() {
        return movePattern;
    }

    public Map<Terrain, Integer> getTerrainMoveCosts() {
        return moveCosts;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int movePointsTo(CoordinateTuple other) {
        throw new RuntimeException("Not Implemented Yet");
    }

    public Collection<TriggeredEffectInstance> getAllTriggeredAbilities() {
        return getTriggeredAbilities().values();
    }
}
