package backend.grid;

import backend.util.ImmutableVoogaObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Created by th174 on 3/28/2017.
 */
public class BoundsHandler extends ImmutableVoogaObject<BoundsHandler> implements Serializable {
	private static final long serialVersionUID = 1L;

	//TODO ResourceBundlify this
	public transient static final BoundsHandler INFINITE_BOUNDS = new BoundsHandler("Infinite Bounds", (input, grid) -> input, "Allows grid to expand to accommodate out of bounds coordinates.");
	public transient static final BoundsHandler FINITE_BOUNDS = new BoundsHandler("Finite Bounds",
			(input, grid) -> fitToBound(input, grid.getBounds()),
			"Converts out of bounds coordinates to the closest inbounds coordinate on the grid.");
	public transient static final BoundsHandler SQUARE_FINITE_BOUNDS = new BoundsHandler("Square Finite Bounds",
			(input, grid) -> fitToBound(input.convertToRectangular(), grid.getRectangularBounds()).convertToDimension(input.dimension()), "Converts out of bounds coordinates to the closest inbounds coordinate on a square grid.");
	public transient static final BoundsHandler TOROIDAL_BOUNDS = new BoundsHandler("Toroidal Bounds",
			(input, grid) -> wrapToBound(input, grid.getBounds()),
			"Wraps out of bounds coordinates to the opposite side of the grid",
			"resources/images/toroid.png");
	public transient static final BoundsHandler SQUARE_TOROIDAL_BOUNDS = new BoundsHandler("Square Toroidal Bounds",
			(input, grid) -> wrapToBound(input.convertToRectangular(), grid.getRectangularBounds()).convertToDimension(input.dimension()),
			"Wraps out of bounds coordinates to the opposite side of a square grid.",
			"resources/images/toroid.png");

	private final CoordinateMapper boundsGetter;

	public BoundsHandler(String name, CoordinateMapper boundsGetter, String description) {
		this(name, boundsGetter, description, "");
	}

	public BoundsHandler(String name, CoordinateMapper boundsGetter, String description, String imgPath) {
		super(name, description, imgPath);
		this.boundsGetter = boundsGetter;
	}

	@Deprecated
	public static Collection<BoundsHandler> getPredefinedBoundsHandlers() {
		return getPredefined(BoundsHandler.class);
	}

	private static CoordinateTuple fitToBound(CoordinateTuple input, GameBoard.GridBounds bounds) {
		return new CoordinateTuple(
				IntStream.range(0, input.dimension()).parallel()
						.mapToObj(i -> Math.max(Math.min(input.get(i), bounds.getMax(i)), bounds.getMin(i)))
						.collect(Collectors.toList()));
	}

	private static CoordinateTuple wrapToBound(CoordinateTuple input, GameBoard.GridBounds bounds) {
		return new CoordinateTuple(
				IntStream.range(0, input.dimension()).parallel()
						.mapToObj(i -> Math.floorMod(input.get(i) - bounds.getMin(i), bounds.getMax(i) - bounds.getMin(i) + 1) + bounds.getMin(i))
						.collect(Collectors.toList()));
	}

	public CoordinateTuple getMappedCoordinate(GameBoard grid, CoordinateTuple input) {
		return boundsGetter.mapCoordinate(input, grid);
	}

	@Override
	public BoundsHandler copy() {
		return new BoundsHandler(getName(), boundsGetter, getDescription(), getImgPath());
	}

	@FunctionalInterface
	public interface CoordinateMapper extends Serializable {
		CoordinateTuple mapCoordinate(CoordinateTuple input, GameBoard grid);
	}
}
