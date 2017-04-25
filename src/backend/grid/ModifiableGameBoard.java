package backend.grid;

import backend.cell.Cell;
import backend.util.ModifiableVoogaObject;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Created by th174 on 3/28/2017.
 */
public class ModifiableGameBoard extends ModifiableVoogaObject<ModifiableGameBoard> implements GameBoard, GameBoardBuilder {
	private volatile Map<CoordinateTuple, Cell> gameBoard;
	private Cell templateCell;
	private BoundsHandler boundsHandler;
	private int rows;
	private int columns;

	//Constructor for building
	public ModifiableGameBoard(String name) {
		this(name, null, Collections.emptyMap(), null, "", "");
	}

	public ModifiableGameBoard(String name, Cell templateCell, int rows, int columns, BoundsHandler boundsHandler, String description, String imgPath) {
		this(name, templateCell, generateGameBoard(templateCell, rows, columns), boundsHandler, description, imgPath);
		this.rows = rows;
		this.columns = columns;
	}

	private ModifiableGameBoard(String name, Cell templateCell, Map<CoordinateTuple, Cell> gameBoard, BoundsHandler boundsHandler, String description, String imgPath) {
		super(name, description, imgPath);
		this.boundsHandler = boundsHandler;
		this.templateCell = templateCell;
		this.gameBoard = gameBoard;
	}

	private static Map<CoordinateTuple, Cell> generateGameBoard(Cell templateCell, int rows, int columns) {
		return IntStream.range(-rows/2, rows/2).boxed()
				.flatMap(i -> IntStream.range(-columns/2, columns/2).mapToObj(j -> new CoordinateTuple(i, j)))
				.parallel()
				.map(e -> e.convertToDimension(templateCell.dimension()))
				.collect(Collectors.toMap(e -> e, e -> templateCell.copy().setLocation(e)));
	}

	@Deprecated
	public static Collection<? extends GameBoard> getPredefinedGameBoards() {
		return getPredefined(ModifiableGameBoard.class);
	}

	@Override
	public ModifiableGameBoard copy() {
		return new ModifiableGameBoard(getName(), getTemplateCell(), gameBoard, getBoundsHandler(), getDescription(), getImgPath());
	}

	@Override
	public BoundsHandler getBoundsHandler() {
		return boundsHandler;
	}

	@Override
	public ModifiableGameBoard setBoundsHandler(BoundsHandler boundsHandler) {
		this.boundsHandler = boundsHandler;
		return this;
	}

	@Override
	public ModifiableGameBoard setCell(CoordinateTuple coordinateTuple, Cell cell) {
		gameBoard.put(coordinateTuple, cell.copy().setLocation(coordinateTuple));
		return this;
	}

	@Override
	public GameBoard build() {
		this.gameBoard = generateGameBoard(getTemplateCell(), rows, columns);
		return this;
	}

	@Override
	public Cell getTemplateCell() {
		return templateCell;
	}

	@Override
	public ModifiableGameBoard setTemplateCell(Cell cell) {
		this.templateCell = cell;
		return this;
	}

	@Override
	public ModifiableGameBoard setRows(int rows) {
		this.rows = rows;
		return this;
	}

	@Override
	public ModifiableGameBoard setColumns(int columns) {
		this.columns = columns;
		return this;
	}

	@Override
	public GridBounds getBounds() {
		return new GridBounds(findMinMax(gameBoard.keySet()));
	}

	@Override
	public GridBounds getRectangularBounds() {
		return new GridBounds(findMinMax(gameBoard.keySet().stream().map(CoordinateTuple::convertToRectangular).collect(Collectors.toList())));
	}

	private int[][] findMinMax(Collection<CoordinateTuple> tuples) {
		int[][] minMax = new int[dimension()][2];
		tuples.forEach(e -> {
			for (int[] ints : minMax) {
				ints[0] = Math.min(e.get(0), ints[0]);
				ints[1] = Math.max(e.get(0), ints[1]);
			}
		});
		return minMax;
	}

	@Override
	public Map<CoordinateTuple, Cell> getCells() {
		return Collections.unmodifiableMap(gameBoard);
	}

	@Override
	public Cell get(CoordinateTuple coordinateTuple) {
		return gameBoard.get(boundsHandler.getMappedCoordinate(this, coordinateTuple));
	}

	@Override
	public Iterator<Map.Entry<CoordinateTuple, Cell>> iterator() {
		return gameBoard.entrySet().iterator();
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				s += String.format("%-20s", get(new CoordinateTuple(i, j).convertToRectangular()).toString());
			}
			s += "\n";
		}
		return s;
	}

	public static class InvalidGridException extends RuntimeException {
		InvalidGridException() {
			super("Incorrect Grid parameters! Could not generate grid!");
		}
	}
}
