package backend.grid;

import backend.util.ModifiableVoogaObject;
import backend.util.VoogaEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class GridPattern extends ModifiableVoogaObject<GridPattern> implements VoogaEntity {
	//TODO ResourceBundlify
	public transient static final GridPattern NONE = new GridPattern("None", "This pattern contains no coordinates", "Empty.png");
	public transient static final GridPattern HEXAGONAL_SINGLE_CELL = new GridPattern("Hexagonal Single Cell", "This pattern contains single hexagonal cell at the origin", "Single_square_cell.png", CoordinateTuple.getOrigin(3));
	public transient static final GridPattern SQUARE_SINGLE_CELL = new GridPattern("Square Single Cell", "This pattern contains a single square cell at the origin", "Single_square_cell.png", CoordinateTuple.getOrigin(2));
	public transient static final GridPattern HEXAGONAL_ADJACENT = new GridPattern("Hexagonal Adjacent", "This pattern of hexagonal coordinates is composed of all hexagonal coordinates adjacent to the origin.", "7Hexagons.png", CoordinateTuple.getOrigin(3).getNeighbors());
	public transient static final GridPattern SQUARE_ADJACENT = new GridPattern("Square Adjacent", "This pattern of square coordinates is composed of all square coordinates adjacent to the origin.", "5Squares.png", CoordinateTuple.getOrigin(2).getNeighbors());
	public transient static final GridPattern HEXAGONAL_RAYS = new GridPattern("Hexagonal Rays", "This pattern of hexagonal coordinates is composed of all hexagonal coordinates on 6 rays centered at the origin.", "6Rook.png", CoordinateTuple.getOrigin(3).getRays(0, 100));
	public transient static final GridPattern SQUARE_RAYS = new GridPattern("Square Rays", "This pattern of square coordinates is composed of all square coordinates on 4 rays centered at the origin", "6Rook.png", CoordinateTuple.getOrigin(3).getRays(0, 100));

	private final Collection<CoordinateTuple> relativeCoordinates;

	GridPattern(CoordinateTuple... coordinates) {
		this("", "", "", coordinates);
	}

	public GridPattern(String name, String description, String imgPath, CoordinateTuple... coordinates) {
		this(name, description, imgPath, Arrays.asList(coordinates));
	}

	public GridPattern(String name, String description, String imgPath, Collection<CoordinateTuple> coordinates) {
		super(name, description, imgPath);
		this.relativeCoordinates = new HashSet<>(coordinates);
	}

	@Deprecated
	public static Collection<GridPattern> getPredefinedGridPatterns() {
		return getPredefined(GridPattern.class);
	}

	public Collection<CoordinateTuple> getCoordinates() {
		return Collections.unmodifiableCollection(relativeCoordinates);
	}

	public Stream<CoordinateTuple> stream() {
		return relativeCoordinates.stream();
	}

	public Stream<CoordinateTuple> parallelStream() {
		return relativeCoordinates.parallelStream();
	}

	@Override
	public GridPattern copy() {
		return new GridPattern(getName(), getDescription(), getImgPath(), getCoordinates());
	}
}
