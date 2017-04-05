package backend.unit;

import backend.cell.CellInstance;
import backend.cell.Terrain;
import backend.player.Player;
import backend.unit.properties.*;
import backend.util.TriggeredEffectInstance;
import backend.util.VoogaObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class UnitTemplate extends VoogaObject implements Unit {
    private final Map<String, ActiveAbility<VoogaObject>> activeAbilities;
    private final Map<String, TriggeredAbilityTemplate> triggeredAbilities;
    private final List<InteractionModifier<Double>> offensiveModifiers;
    private final List<InteractionModifier<Double>> defensiveModifiers;
    private final Map<Terrain, Integer> terrainMoveCosts;
    private double hitPoints;
    private int movePoints;
    private GridPattern movePattern;
    private Faction faction;

    public UnitTemplate(String unitTemplateName, double hitPoints, int movePoints, Faction faction, GridPattern movePattern, String unitTemplateDescription, String imgPath) {
        this(unitTemplateName, hitPoints, movePoints, faction, movePattern, new HashMap<>(), unitTemplateDescription, imgPath);
    }

    public UnitTemplate(String unitTemplateName, double hitPoints, int movePoints, Faction faction, GridPattern movePattern, Map<Terrain, Integer> moveCosts, String unitTemplateDescription, String imgPath) {
        this(unitTemplateName, hitPoints, movePoints, faction, movePattern, moveCosts, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), unitTemplateDescription, imgPath);
    }

    public UnitTemplate(String unitTemplateName, double hitPoints, int movePoints, Faction faction, GridPattern movePattern, Map<Terrain, Integer> moveCosts, Collection<ActiveAbility<VoogaObject>> activeAbilities, Collection<TriggeredAbilityTemplate> triggeredAbilities, Collection<InteractionModifier<Double>> offensiveModifiers, Collection<InteractionModifier<Double>> defensiveModifiers, String unitDescription, String imgPath) {
        super(unitTemplateName, unitDescription, imgPath);
        this.faction = faction;
        this.terrainMoveCosts = new HashMap<>(moveCosts);
        this.hitPoints = hitPoints;
        this.movePoints = movePoints;
        this.movePattern = movePattern;
        this.triggeredAbilities = triggeredAbilities.parallelStream().collect(Collectors.toMap(TriggeredAbilityTemplate::getName, a -> a));
        this.activeAbilities = activeAbilities.parallelStream().collect(Collectors.toMap(ActiveAbility::getName, a -> a));
        this.offensiveModifiers = new ArrayList<>(offensiveModifiers);
        this.defensiveModifiers = new ArrayList<>(defensiveModifiers);
    }

    public static Collection<UnitTemplate> getPredefinedUnitTemplates() {
        return getPredefined(UnitTemplate.class);
    }

    public UnitInstance createInstance(String unitName, Player ownerPlayer, CellInstance startingCell) {
        return new UnitInstance(unitName, this, ownerPlayer, startingCell);
    }

    @Override
    public HitPoints getHitPoints() {
        return new HitPoints(hitPoints);
    }

    public void setHitPoints(double hitPoints) {
        this.hitPoints = hitPoints;
    }

    @Override
    public MovePoints getMovePoints() {
        return new MovePoints(movePoints);
    }

    public void setMovePoints(int movePoints) {
        this.movePoints = movePoints;
    }

    @Override
    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    @Override
    public GridPattern getMovePattern() {
        return movePattern;
    }

    public void setMovePattern(GridPattern movePattern) {
        this.movePattern = movePattern;
    }

    @Override
    public List<InteractionModifier<Double>> getDefensiveModifiers() {
        return defensiveModifiers;
    }

    @Override
    public List<InteractionModifier<Double>> getOffensiveModifiers() {
        return offensiveModifiers;
    }

    @Override
    public Map<String, ActiveAbility<VoogaObject>> getActiveAbilities() {
        return activeAbilities;
    }

    @Override
    public Map<String, TriggeredEffectInstance> getTriggeredAbilities() {
        return triggeredAbilities;
    }

    @Override
    public Map<Terrain, Integer> getTerrainMoveCosts() {
        return terrainMoveCosts;
    }
}
