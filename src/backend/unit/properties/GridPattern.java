package backend.unit.properties;

import backend.grid.CoordinateTuple;
import backend.util.VoogaObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class GridPattern extends VoogaObject {
    //TODO ResourceBundlify
    public static final GridPattern NONE = new GridPattern("None", "This pattern contains no coordinates", "Empty.png");
    public static final GridPattern HEXAGONAL_ADJACENT = new GridPattern("Hexagonal Adjacent", "This pattern of hexagonal coordinates is composed of all hexagonal coordinates adjacent to the origin.", "7Hexagons.png", CoordinateTuple.getOrigin(3).getNeighbors());
    public static final GridPattern SQUARE_ADJACENT = new GridPattern("Square Adjacent", "This pattern of square coordinates is composed of all square coordinates adjacent to the origin.", "5Squares.png", CoordinateTuple.getOrigin(2).getNeighbors());
    public static final GridPattern HEXAGONAL_RAYS = new GridPattern("Hexagonal Rays", "This pattern of hexagonal coordinates is composed of all hexagonal coordinates on 6 rays centered at the origin.", "6Rook.png", CoordinateTuple.getOrigin(3).getRays(100));
    public static final GridPattern SQUARE_RAYS = new GridPattern("Square Rays", "This pattern of square coordinates is composed of all square coordinates on 4 rays centered at the origin", "6Rook.png", CoordinateTuple.getOrigin(3).getRays(100));

    private final Collection<CoordinateTuple> relativeCoordinates;

    public GridPattern(String name, String description, String imgPath, CoordinateTuple... coordinates) {
        this(name, description, imgPath, Arrays.asList(coordinates));
    }

    public GridPattern(String name, String description, String imgPath, Collection<CoordinateTuple> coordinates) {
        super(name, description, imgPath);
        this.relativeCoordinates = new HashSet<>(coordinates);
    }

    public static Collection<GridPattern> getPredefinedGridPatterns() {
        return getPredefined(GridPattern.class);
    }

    public Collection<CoordinateTuple> getCoordinates() {
        return Collections.unmodifiableCollection(relativeCoordinates);
    }
}
