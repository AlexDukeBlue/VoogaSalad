package backend.cell;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.Shape;
import backend.unit.Unit;
import backend.unit.properties.InteractionModifier;
import backend.util.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Created by th174 on 3/28/2017.
 */
public interface Cell extends Serializable, HasTriggeredAbilities, HasLocation, VoogaEntity, HasPassiveModifiers {

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

	Cell add(VoogaEntity voogaEntity);

	Cell setMaxOccupants(int maxOccupants);

	@Override
	default List<? extends InteractionModifier> getOffensiveModifiers() {
		return getTerrain().getOffensiveModifiers();
	}

	@Override
	default List<? extends InteractionModifier> getDefensiveModifiers() {
		return getTerrain().getDefensiveModifiers();
	}

	int getMaxOccupants();
}
