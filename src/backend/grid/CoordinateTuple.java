package backend.grid;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Created by th174 on 3/28/2017.
 */
public final class CoordinateTuple implements Iterable<Integer>, Serializable {
	public transient static final CoordinateTuple EMPTY = new CoordinateTuple();
	private static final Map<Integer, Shape> shapeDimensions = new HashMap<>();

	private final List<Integer> coordinates;

	public CoordinateTuple(int... coordinates) {
		this.coordinates = Collections.unmodifiableList(Arrays.stream(coordinates).boxed().collect(Collectors.toList()));
	}

	public CoordinateTuple(List<Integer> coordinates) {
		this.coordinates = Collections.unmodifiableList(coordinates);
	}

	public static CoordinateTuple getOrigin(int dimension) {
		return new CoordinateTuple(new int[dimension]);
	}

	public Collection<CoordinateTuple> getNeighbors() {
		if (shapeDimensions.isEmpty()) {
			Arrays.stream(Shape.values()).forEach(shape -> shapeDimensions.put(shape.getDimension(), shape));
		}
		return shapeDimensions.get(dimension()).getNeighborPattern().getCoordinates().stream().map(this::sum).collect(Collectors.toList());
	}

	public Collection<CoordinateTuple> getRays(int minRadius, int maxRadius) {
		return getNeighbors().stream()
				.flatMap(coordinateTuple -> IntStream.range(minRadius, maxRadius).mapToObj(coordinateTuple::product))
				.map(this::sum)
				.collect(Collectors.toList());
	}

	public int dimension() {
		return coordinates.size();
	}

	public double euclideanDistanceTo(CoordinateTuple other) {
		return Math.sqrt(
				IntStream.range(0, dimension()).parallel()
						.mapToDouble(i -> Math.pow(this.get(i) - other.get(i), 2))
						.sum());
	}

	public int get(int index) {
		return coordinates.get(index);
	}

	public double manhattanDistanceTo(CoordinateTuple other) {
		return ((dimension() == 3) ? 0.5 : 1) *
				IntStream.range(0, dimension())
						.map(i -> this.get(i) - other.get(i))
						.sum();
	}

	public CoordinateTuple replace(int index, int newValue) {
		List<Integer> newCoordinates = new ArrayList<>(coordinates);
		coordinates.set(index, newValue);
		return new CoordinateTuple(newCoordinates);
	}

	public CoordinateTuple sum(CoordinateTuple other) throws DimensionMismatchException {
		if (this.dimension() != other.dimension()) {
			throw new DimensionMismatchException(this.dimension(), other.dimension());
		}
		int[] newCoordinates = new int[dimension()];
		IntStream.range(0, dimension()).forEach(i -> newCoordinates[i] = this.get(i) + other.get(i));
		return new CoordinateTuple(newCoordinates);
	}

	public CoordinateTuple product(double scalar) {
		int[] newCoordinates = new int[dimension()];
		IntStream.range(0, dimension()).forEach(i -> newCoordinates[i] = Math.toIntExact(Math.round(this.get(i) * scalar)));
		return new CoordinateTuple(newCoordinates);
	}

	public CoordinateTuple convertToDimension(int dimension) {
		switch (dimension) {
			case 2:
				return convertToRectangular();
			case 3:
				return convertToHexagonal();
			default:
				throw new RuntimeException("Not Implemented Yet");
		}
	}

	public CoordinateTuple convertToRectangular() {
		if (dimension() == 2) {
			return this;
		}
		int col = get(0);
		int row = get(2) + (get(0) + (get(0) & 1)) / 2;
		return new CoordinateTuple(row, col);
	}

	private CoordinateTuple convertToHexagonal() {
		if (dimension() == 3) {
			return this;
		}
		int x = get(1);
		int z = get(0) - (get(1) + (get(1) & 1)) / 2;
		return new CoordinateTuple(x, -(x + z), z);
	}

	public Stream<Integer> stream() {
		return coordinates.stream();
	}

	public Stream<Integer> parallelStream() {
		return coordinates.parallelStream();
	}

	@Override
	public Iterator<Integer> iterator() {
		return coordinates.iterator();
	}

	@Override
	public int hashCode() {
		return coordinates.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof CoordinateTuple && this.coordinates.equals(((CoordinateTuple) other).coordinates);
	}

	@Override
	public String toString() {
		return coordinates.toString().replace("[", "(").replace("]", ")");
	}

	public static class DimensionMismatchException extends RuntimeException {
		DimensionMismatchException(int dim1, int dim2) {
			super(String.format("Coordinate Tuple Dimension Mismatch: %d,%d", dim1, dim2));
		}
	}
}