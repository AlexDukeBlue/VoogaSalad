package backend.cell;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.Shape;
import backend.unit.Unit;
import backend.util.GameplayState;
import backend.util.ModifiableTriggeredEffect;
import backend.util.TriggeredEffect;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author Created by th174 on 3/28/2017.
 */
public interface Cell extends Serializable {
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

	public ModifiableCell addOccupants(Unit... units);

	public ModifiableCell removeOccupants(Unit... units);
}
