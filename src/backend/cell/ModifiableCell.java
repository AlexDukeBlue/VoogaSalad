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
	//TODO ResourceBundlify
	public transient static final ModifiableCell BASIC_HEXAGONAL_EMPTY = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.EMPTY);
	public transient static final ModifiableCell BASIC_HEXAGONAL_FLAT = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.FLAT);
	public transient static final ModifiableCell BASIC_HEXAGONAL_FOREST = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.FOREST);
	public transient static final ModifiableCell BASIC_HEXAGONAL_WATER = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.WATER);
	public transient static final ModifiableCell BASIC_HEXAGONAL_MOUNTAIN = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.MOUNTAIN);
	public transient static final ModifiableCell BASIC_HEXAGONAL_FORTIFIED = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.FORTIFIED);
	public transient static final ModifiableCell HEALING_HEXAGONAL_EMPTY = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.EMPTY)
			.addTriggeredAbility(ModifiableTriggeredEffect.FULL_HEAL);
	public transient static final ModifiableCell HEALING_HEXAGONAL_FLAT = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.FLAT)
			.addTriggeredAbility(ModifiableTriggeredEffect.FULL_HEAL);
	public transient static final ModifiableCell HEALING_HEXAGONAL_FOREST = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.FOREST)
			.addTriggeredAbility(ModifiableTriggeredEffect.FULL_HEAL);
	public transient static final ModifiableCell HEALING_HEXAGONAL_WATER = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.WATER)
			.addTriggeredAbility(ModifiableTriggeredEffect.FULL_HEAL);
	public transient static final ModifiableCell HEALING_HEXAGONAL_MOUNTAIN = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.MOUNTAIN)
			.addTriggeredAbility(ModifiableTriggeredEffect.FULL_HEAL);
	public transient static final ModifiableCell HEALING_HEXAGONAL_FORTIFIED = new ModifiableCell()
			.setShape(Shape.HEXAGONAL)
			.setTerrain(ModifiableTerrain.FORTIFIED)
			.addTriggeredAbility(ModifiableTriggeredEffect.FULL_HEAL);
	public transient static final ModifiableCell BASIC_SQUARE_EMPTY = new ModifiableCell()
			.setShape(Shape.SQUARE)
			.setTerrain(ModifiableTerrain.EMPTY);
	public transient static final ModifiableCell BASIC_SQUARE_FLAT = new ModifiableCell()
			.setShape(Shape.SQUARE)
			.setTerrain(ModifiableTerrain.FLAT);
	public transient static final ModifiableCell BASIC_SQUARE_FOREST = new ModifiableCell()
			.setShape(Shape.SQUARE)
			.setTerrain(ModifiableTerrain.FOREST);
	public transient static final ModifiableCell BASIC_SQUARE_WATER = new ModifiableCell()
			.setShape(Shape.SQUARE)
			.setTerrain(ModifiableTerrain.WATER);
	public transient static final ModifiableCell BASIC_SQUARE_MOUNTAIN = new ModifiableCell()
			.setShape(Shape.SQUARE)
			.setTerrain(ModifiableTerrain.MOUNTAIN);
	public transient static final ModifiableCell BASIC_SQUARE_FORTIFIED = new ModifiableCell()
			.setShape(Shape.SQUARE)
			.setTerrain(ModifiableTerrain.FORTIFIED);

	private static final Map<Class<? extends VoogaEntity>,BiConsumer<VoogaEntity,ModifiableCell>> actionOnClass = new HashMap<>();

	static {
		actionOnClass.put(ModifiableTerrain.class, (theTerrain,cell) -> cell.setTerrain((Terrain)theTerrain));
		actionOnClass.put(ModifiableUnit.class, ((unit,cell) -> cell.addOccupants((Unit)unit)));
		actionOnClass.put(Ability.class, ((ability,cell) -> cell.getTerrain().addAbility((Ability)ability)));
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

	public ModifiableCell addTriggeredAbility(ModifiableTriggeredEffect modifiableTriggeredEffect) {
		getTerrain().addTriggeredAbilities(modifiableTriggeredEffect);
		return this;
	}

	public ModifiableCell removeTriggeredAbility(ModifiableTriggeredEffect modifiableTriggeredEffect) {
		getTerrain().removeTriggeredAbilities(modifiableTriggeredEffect);
		return this;
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
		this.terrain = terrain;
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

	@Deprecated
	public static Collection<ModifiableCell> getPredefinedCells() {
		return getPredefined(ModifiableCell.class);
	}

	@Override
	public void addVoogaEntity(VoogaEntity voogaEntity) {
		System.out.println("Class: " + voogaEntity.getClass().toString());
		System.out.println("Map: " + actionOnClass);
		System.out.println("Action: " + actionOnClass.get(voogaEntity.getClass()));
		actionOnClass.get(voogaEntity.getClass()).accept(voogaEntity, this);
	}
}
