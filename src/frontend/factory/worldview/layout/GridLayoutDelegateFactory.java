package frontend.factory.worldview.layout;

import backend.grid.CoordinateTuple;
import controller.Controller;
import javafx.scene.shape.Polygon;

import java.util.HashMap;
import java.util.Map;

public class GridLayoutDelegateFactory implements GridLayoutDelegate {
	private static final Map<Integer, GridLayoutDelegate> DIMENSION_LAYOUT_MANAGER_MAP = new HashMap<>();

	static {
		DIMENSION_LAYOUT_MANAGER_MAP.put(2, new SquareLayoutDelegate());
		DIMENSION_LAYOUT_MANAGER_MAP.put(3, new HexagonalGridLayoutDelegate());
	}

	@Override
	public Polygon layoutCell(double scaleFactor, double min, double max, CoordinateTuple location, Controller controller) {
		return DIMENSION_LAYOUT_MANAGER_MAP.get(location.dimension()).layoutCell(scaleFactor, min, max, location, controller);
	}
}
