/**
 *
 */
package backend.grid;

import backend.Player;
import backend.grid.boundary.BoundaryConditions;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Dylan Peters
 */
public interface Grid {

    Cell get(CoordinateTuple coordinateTuple);

    Map<CoordinateTuple, Cell> getCells();

    default Collection<Cell> getNeighbors(Cell cell) {
        return getNeighbors(cell.getCoordinates());
    }

    Map<CoordinateTuple, Cell> getNeighbors(CoordinateTuple coordinate);

    GridBounds getRectangularBounds();

    /*
     * this should be either protected or private to prevent dynamic changing of
     * boundary conditions (in the first sprint at least.)
     */
    void setBoundaryConditions(BoundaryConditions boundaryConditions) throws IllegalAccessException;

    Collection<Cell> getVisibleCells(Player player, Predicate<Cell> visibilityPredicate);

    Collection<Cell> getExploredCells(Player player, Predicate<Cell> visibilityPredicate);

    class GridBounds {
        private final List<Pair<Integer, Integer>> bounds;

        protected GridBounds(int[]... minmax) {
            bounds = Arrays.stream(minmax).map(e -> new Pair<>(e[0], e[1])).collect(Collectors.toList());
        }

        public int getMin(int i) {
            return bounds.get(i).getKey();
        }

        public int getMax(int i) {
            return bounds.get(i).getValue();
        }
    }
}
