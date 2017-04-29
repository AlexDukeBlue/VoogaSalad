package backend.cell;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.Shape;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.*;

import java.util.*;
import java.util.function.BiConsumer;

import static backend.util.ImmutableVoogaObject.getPredefined;

/**
 * @author Created by th174 on 3/31/2017.
 */
public class ModifiableCell implements Cell {
	private static final long serialVersionUID = 1L;

	private static final Map<Class<? extends VoogaEntity>, BiConsumer<VoogaEntity, Cell>> DISPATCH_MAP = new HashMap<>();

	static {
		DISPATCH_MAP.put(ModifiableTerrain.class, (terrain, cell) -> ((ModifiableCell) cell).setTerrain((Terrain) terrain));
		DISPATCH_MAP.put(ModifiableUnit.class, ((unit, cell) -> cell.addOccupants((Unit) unit)));
		DISPATCH_MAP.put(Ability.class, ((ability, cell) -> cell.getTerrain().addAbility((Ability) ability)));
	}

	private final Map<String, Unit> occupants;
	private Shape shape;
	private Terrain terrain;
	private CoordinateTuple coordinates;

	public ModifiableCell() {
		this(null, null, null);
	}

	public ModifiableCell(CoordinateTuple location, Shape shape, Terrain terrain) {
		this.shape = shape;
		this.terrain = terrain;
		this.coordinates = location;
		occupants = new HashMap<>();
	}

	@Deprecated
	public static Collection<ModifiableCell> getPredefinedCells() {
		return getPredefined(ModifiableCell.class);
	}

	public ModifiableCell addOccupants(Unit... units) {
		Arrays.stream(units).forEach(unit -> {
			occupants.put(unit.getName(), unit);
			((ModifiableUnit) unit).setCurrentCell(this);
		});
		return this;
	}

	public ModifiableCell removeOccupants(Unit... units) {
		Arrays.stream(units).forEach(unit -> {
			occupants.remove(unit.getName());
			((ModifiableUnit) unit).setCurrentCell(null);
		});
		return this;
	}

	public ModifiableCell removeOccupants(String... unitNames) {
		removeOccupants(Arrays.stream(unitNames).map(this::getOccupantByName).toArray(Unit[]::new));
		return this;
	}

	@Override
	public void startTurn(GameplayState gameState) {
		processTriggers(Event.TURN_START, gameState);
		getOccupants().forEach(e -> e.startTurn(gameState));
	}

	@Override
	public void endTurn(GameplayState gameState) {
		processTriggers(Event.TURN_END, gameState);
		getOccupants().forEach(e -> e.endTurn(gameState));
	}

	@Override
	public CoordinateTuple getLocation() {
		return coordinates;
	}

	public ModifiableCell setLocation(CoordinateTuple coordinates) {
		this.coordinates = coordinates;
		return this;
	}

	@Override
	public int dimension() {
		return getShape().getDimension();
	}

	@Override
	public Shape getShape() {
		return shape;
	}

	public ModifiableCell setShape(Shape shape) {
		this.shape = shape;
		return this;
	}

	@Override
	public Map<CoordinateTuple, Cell> getNeighbors(GameBoard grid) {
		return grid.getNeighbors(this);
	}

	@Override
	public Terrain getTerrain() {
		return terrain;
	}

	public ModifiableCell setTerrain(Terrain terrain) {
		this.terrain = terrain.copy();
		return this;
	}

	@Override
	public Collection<? extends TriggeredEffect> getTriggeredAbilities() {
		return getTerrain().getTriggeredAbilities();
	}

	@Override
	public ModifiableCell addTriggeredAbilities(ModifiableTriggeredEffect... triggeredAbilities) {
		getTerrain().addTriggeredAbilities(triggeredAbilities);
		return this;
	}

	@Override
	public ModifiableCell removeTriggeredAbilities(ModifiableTriggeredEffect... triggeredAbilities) {
		getTerrain().removeTriggeredAbilities(triggeredAbilities);
		return this;
	}

	@Override
	public Collection<Unit> getOccupants() {
		return Collections.unmodifiableCollection(occupants.values());
	}

	@Override
	public Unit getOccupantByName(String name) {
		return occupants.get(name);
	}

	@Override
	public void leave(Unit unit, GameplayState gamestate) {
		getTerrain().getTriggeredAbilities().forEach(ability -> ability.affect(unit, Event.UNIT_PRE_MOVEMENT, gamestate));
		this.removeOccupants(unit);
	}

	@Override
	public void arrive(Unit unit, GameplayState gamestate) {
		this.addOccupants(unit);
		getTerrain().getTriggeredAbilities().forEach(ability -> ability.affect(unit, Event.UNIT_POST_MOVEMENT, gamestate));
	}

	@Override
	public ModifiableCell copy() {
		if (Objects.isNull(getShape()) || Objects.isNull(getTerrain())) {
			throw new IncompleteCellException();
		}
		return new ModifiableCell(getLocation(), getShape(), getTerrain());
	}

	@Override
	public String toString() {
		return terrain.toString() + " @ " + getLocation() + "\tOccupants: " + getOccupants().size();
	}

	private void processTriggers(Event event, GameplayState gameState) {
		occupants.values().forEach(unit -> getTerrain().getTriggeredAbilities().forEach(ability -> ability.affect(unit, event, gameState)));
		getTerrain().removeTriggeredAbilitiesIf(TriggeredEffect::isExpired);
	}

	@Override
	public String getName() {
		return getTerrain().getName();
	}

	@Override
	public String getDescription() {
		return getTerrain().getDescription();
	}

	@Override
	public String getImgPath() {
		return getTerrain().getImgPath();
	}

	@Override
	public Cell add(VoogaEntity voogaEntity) {
		DISPATCH_MAP.get(voogaEntity.getClass()).accept(voogaEntity, this);
		return this;
	}

	static class IncompleteCellException extends RuntimeException {
		IncompleteCellException() {
			super("Incomplete Cell");
		}
	}
}
