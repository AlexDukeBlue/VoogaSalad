package backend.grid;

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
			new CoordinateTuple(-1, 1, 0));

	private final int dimension;
	private final GridPattern neighborsPattern;
	private final String name;

	Shape(String name, CoordinateTuple... neighborsRelativeCoordinates) {
		this.name = name;
		this.dimension = neighborsRelativeCoordinates[0].dimension();
		this.neighborsPattern = new GridPattern(neighborsRelativeCoordinates);
	}

	public GridPattern getNeighborPattern() {
		return neighborsPattern;
	}

	public int getDimension() {
		return dimension;
	}

	public String getName() {
		return name;
	}
}