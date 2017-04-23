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
	private static final Map<Class<? extends VoogaEntity>, BiConsumer<VoogaEntity, ModifiableCell>> actionOnClass = new HashMap<>();

	static {
		actionOnClass.put(ModifiableTerrain.class, (theTerrain, cell) -> cell.setTerrain((Terrain) theTerrain));
		actionOnClass.put(ModifiableUnit.class, ((unit, cell) -> cell.addOccupants((Unit) unit)));
		actionOnClass.put(Ability.class, ((ability, cell) -> cell.getTerrain().addAbility((Ability) ability)));
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

	@Override
	public void startTurn(GameplayState gameState) {
		processTriggers(Event.TURN_START, gameState);
	}

	@Override
	public void endTurn(GameplayState gameState) {
		processTriggers(Event.TURN_END, gameState);
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
		return "\n" + terrain.toString() + " @ " + getLocation() + "\tOccupants: " + getOccupants().size();
	}

	private void processTriggers(Event event, GameplayState gameState) {
		occupants.values().forEach(unit -> getTerrain().getTriggeredAbilities().forEach(ability -> ability.affect(unit, event, gameState)));
		getTerrain().removeTriggeredAbilitiesIf(TriggeredEffect::isExpired);
	}

	@Override
	public void addVoogaEntity(VoogaEntity voogaEntity) {
		actionOnClass.get(voogaEntity.getClass()).accept(voogaEntity, this);
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

	static class IncompleteCellException extends RuntimeException {
		IncompleteCellException() {
			super("Incomplete Cell");
		}
	}
}
