package frontend.factory.worldview.layout;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import javafx.scene.shape.Polygon;

import java.util.HashMap;
import java.util.Map;

/**
 * The GridLayoutDelegateFactory is a combination of the Strategy and Factory
 * design patterns; the factory (this class) has references to each type of
 * layout manager (hexagonal, square, etc.) and selects the proper one based on
 * the game board's dimensions. This allows the GridView to simply create an
 * instance of GridLayoutDelegate, rather than having to manually select the
 * correct Strategy.
 * 
 * @author Dylan Peters
 *
 */
public class GridLayoutDelegateFactory implements GridLayoutDelegate {
	private static final Map<Integer, GridLayoutDelegate> DIMENSION_LAYOUT_MANAGER_MAP = new HashMap<>();

	static {
		DIMENSION_LAYOUT_MANAGER_MAP.put(2, new SquareLayoutDelegate());
		DIMENSION_LAYOUT_MANAGER_MAP.put(3, new HexagonalGridLayoutDelegate());
	}

	@Override
	public Polygon layoutCell(double scaleFactor, double min, double max, CoordinateTuple location,
			GameBoard gameBoard) {
		return DIMENSION_LAYOUT_MANAGER_MAP.get(location.dimension()).layoutCell(scaleFactor, min, max, location,
				gameBoard);
	}
}
