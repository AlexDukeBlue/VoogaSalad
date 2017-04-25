package backend.grid;

import backend.cell.Cell;
import backend.player.Player;
import backend.unit.Unit;
import backend.util.VoogaEntity;
import javafx.util.Pair;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface GameBoard extends Iterable<Entry<CoordinateTuple, Cell>>, VoogaEntity {
	@Override
	ModifiableGameBoard copy();

	BoundsHandler getBoundsHandler();

	default int dimension() {
		return getTemplateCell().dimension();
	}

	Cell getTemplateCell();

	default Collection<Unit> getUnits() {
		return parallelStream().flatMap(e -> e.getOccupants().stream()).collect(Collectors.toList());
	}

	default Stream<Cell> parallelStream() {
		return stream().parallel();
	}

	default Stream<Cell> stream() {
		return getCells().values().stream();
	}

	Map<CoordinateTuple, Cell> getCells();

	default void forEach(BiConsumer<CoordinateTuple, Cell> biConsumer) {
		getCells().forEach(biConsumer);
	}

	default Map<CoordinateTuple, Cell> getNeighbors(CoordinateTuple coordinate) {
		return getNeighbors(get(coordinate));
	}

	default Map<CoordinateTuple, Cell> getNeighbors(Cell cell) {
		return getRelative(cell.getLocation(), cell.getShape().getNeighborPattern());
	}

	Cell get(CoordinateTuple coordinateTuple);

	default Cell get(int... coordinates) {
		return get(new CoordinateTuple(coordinates));
	}

	default Map<CoordinateTuple, Cell> getRelative(CoordinateTuple center, GridPattern gridPattern) {
		return gridPattern.parallelStream().collect(Collectors.toMap(e -> e, e -> get(e.sum(center))));
	}

	GridBounds getBounds();

	GridBounds getRectangularBounds();

	default Collection<Cell> filterCells(Player player, BiPredicate<Player, Cell> visibilityPredicate) {
		return parallelStream().filter(c -> visibilityPredicate.test(player, c)).collect(Collectors.toList());
	}

	@Override
	default Iterator<Entry<CoordinateTuple, Cell>> iterator() {
		return getCells().entrySet().iterator();
	}

	class GridBounds {
		private final List<Pair<Integer, Integer>> bounds;

		@SafeVarargs
		protected GridBounds(Pair<Integer, Integer>... minMax) {
			bounds = Arrays.asList(minMax);
		}

		protected GridBounds(int[]... minMax) {
			bounds = Arrays.stream(minMax).map(e -> new Pair<>(e[0], e[1])).collect(Collectors.toList());
		}

		public int getMin(int i) {
			return bounds.get(i).getKey();
		}

		public int getMax(int i) {
			return bounds.get(i).getValue();
		}
	}

	int getRows();

	int getColumns();
}
