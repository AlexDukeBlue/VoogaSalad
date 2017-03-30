package backend.unit;

import backend.GameObject;
import backend.GameObjectImpl;
import backend.cell.Cell;
import backend.cell.Terrain;
import backend.game_engine.GameState;
import backend.game_engine.Player;
import backend.grid.CoordinateTuple;
import backend.unit.properties.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Timmy
 *
 * @author Created by th174 on 3/27/2017.
 */
public class Unit extends GameObjectImpl {
    private final HitPoints hitPoints;
    private final MovePoints movePoints;
    private final MovementPattern movePattern;
    private final Map<String, ActiveAbility<GameObject>> activeAbilities;
    private final Map<String, PassiveAbility> passiveAbilties;
    private final Map<Terrain, Integer> moveCosts;
    private final Faction faction;

    private Player ownedBy;
    private Cell currentCell;

    public Unit(String unitName, double hitPoints, int movePoints, Faction faction, MovementPattern movePattern, Map<Terrain, Integer> moveCosts, Collection<ActiveAbility> activeAbilities, Collection<PassiveAbility> passiveAbilties, String unitDescription, String imgPath, GameState game) {
        super(unitName, unitDescription, imgPath, game);
        this.faction = faction;
        this.moveCosts = moveCosts;
        this.hitPoints = new HitPoints(hitPoints, game);
        this.movePoints = new MovePoints(movePoints, game);
        this.movePattern = movePattern;
        this.passiveAbilties = passiveAbilties.stream().collect(Collectors.toMap(PassiveAbility::getName, a -> a));
        this.activeAbilities = activeAbilities.stream().collect(Collectors.toMap(ActiveAbility::getName, a -> a));
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public CoordinateTuple getLocation() {
        return currentCell.getCoordinates();
    }

    public void applyPassives() {
        movePoints.resetValue();
        passiveAbilties.values().forEach(e -> e.activate(this, getGame()));
    }

    public Faction getFaction() {
        return faction;
    }

    public Player getOwner() {
        return ownedBy;
    }

    public void setOwner(Player p) {
        ownedBy = p;
    }

    public ActiveAbility getAbilityByName(String s) {
        return activeAbilities.get(s);
    }

    public void useActiveAbility(String activeAbilityName, GameObject target) {
        useActiveAbility(activeAbilities.get(activeAbilityName), target);
    }

    public void useActiveAbility(ActiveAbility<GameObject> activeAbility, GameObject target) {
        activeAbility.affect(this, target, getGame());
    }

    public Collection<ActiveAbility<GameObject>> getActives() {
        return activeAbilities.values();
    }

    public Collection<PassiveAbility> getPassives() {
        return passiveAbilties.values();
    }

    public List<InteractionModifier<Double>> getAttackModifier() {
        //TODO
        return Collections.singletonList(InteractionModifier.NO_CHANGE);
    }

    public List<InteractionModifier<Double>> getDefenseModifier() {
        //TODO
        return Collections.singletonList(InteractionModifier.NO_CHANGE);
    }

    public void moveTo(Cell cell) {
        movePoints.useMovePoints(moveCosts.get(cell.getTerrain()));
        currentCell = cell;
    }

    public int getMoveCost(Terrain terrain) {
        return moveCosts.get(terrain);
    }

    public Collection<Cell> getMoveOptions() {
        return movePattern.getLegalMoves().stream()
                .map(e -> getGame().getGrid().get(e.sum(this.getLocation())))
                .filter(Objects::nonNull)
                .filter(e -> getMoveCost(e.getTerrain()) < movePoints.getCurrentValue()).collect(Collectors.toSet());
    }

    public HitPoints getHitPoints() {
        return hitPoints;
    }

    public MovePoints getMovePoints() {
        return movePoints;
    }

    public int movePointsTo(CoordinateTuple other) {
        throw new RuntimeException("Not Implemented Yet");
    }

    public MovementPattern getMovementPattern() {
        return movePattern;
    }
}
