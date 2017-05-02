package backend.grid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by th174 on 3/28/2017.
 */
public enum Shape {
	SQUARE(
			"Square",
			new CoordinateTuple(0, 1),
			new CoordinateTuple(1, 0),
			new CoordinateTuple(-1, 0),
			new CoordinateTuple(0, -1)),
	HEXAGONAL(
			"Hexagon",
			new CoordinateTuple(0, 1, -1),
			new CoordinateTuple(0, -1, 1),
			new CoordinateTuple(1, 0, -1),
			new CoordinateTuple(-1, 0, 1),
			new CoordinateTuple(1, -1, 0),
			new CoordinateTuple(-1, 1, 0)),
	
	Triangular(
			"Triangle",
			new CoordinateTuple(0,1),
			new CoordinateTuple(-1,1),
			new CoordinateTuple(1,-1));


	private static final long serialVersionUID = 1L;
	private static final Map<Integer, Shape> DIMENSION_SHAPE_MAP = new HashMap<>();

	static {
		Arrays.stream(Shape.values()).forEach(shape -> DIMENSION_SHAPE_MAP.put(shape.getDimension(), shape));
	}

	private final int dimension;
	private final CoordinateTuple[] neighborsPattern;
	private final String name;

	Shape(String name, CoordinateTuple... neighborsRelativeCoordinates) {
		this.name = name;
		this.dimension = neighborsRelativeCoordinates[0].dimension();
		this.neighborsPattern = neighborsRelativeCoordinates;
	}

	public GridPattern getNeighborPattern() {
		return new GridPattern(neighborsPattern);
	}

	public int getDimension() {
		return dimension;
	}

	public static Shape fromDimension(int dimension) {
		return DIMENSION_SHAPE_MAP.get(dimension);
	}

	public String getName() {
		return name;
	}
}