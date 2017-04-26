package backend.cell;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.Shape;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Created by th174 on 3/28/2017.
 */
public interface Cell extends Serializable, HasTriggeredAbilities, HasLocation, VoogaEntity {
	Map<Class<? extends VoogaEntity>, BiConsumer<VoogaEntity, Cell>> DISPATCH_MAP = new HashMap<Class<? extends VoogaEntity>, BiConsumer<VoogaEntity, Cell>>() {{
		put(ModifiableTerrain.class, (theTerrain, cell) -> ((ModifiableCell) cell).setTerrain((Terrain) theTerrain));
		put(ModifiableUnit.class, ((unit, cell) -> cell.addOccupants((Unit) unit)));
		put(Ability.class, ((ability, cell) -> cell.getTerrain().addAbility((Ability) ability)));
	}};

	ModifiableCell copy();

	void startTurn(GameplayState gameState);

	void endTurn(GameplayState gameState);

	CoordinateTuple getLocation();

	default int dimension() {
		return getShape().getDimension();
	}

	Shape getShape();

	Map<CoordinateTuple, Cell> getNeighbors(GameBoard grid);

	Terrain getTerrain();

	Collection<? extends TriggeredEffect> getTriggeredAbilities();

	Cell addTriggeredAbilities(ModifiableTriggeredEffect... cellEffect);

	Cell removeTriggeredAbilities(ModifiableTriggeredEffect... cellEffect);

	Collection<? extends Unit> getOccupants();

	Unit getOccupantByName(String name);

	void leave(Unit unit, GameplayState gamestate);

	void arrive(Unit unit, GameplayState gamestate);

	Cell addOccupants(Unit... units);

	Cell removeOccupants(Unit... units);

	Cell removeOccupants(String... unitNames);

	default Cell add(VoogaEntity voogaEntity) {
		DISPATCH_MAP.get(voogaEntity.getClass()).accept(voogaEntity, this);
		return this;
	}
}
