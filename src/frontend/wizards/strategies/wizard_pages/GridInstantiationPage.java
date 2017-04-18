package frontend.wizards.strategies.wizard_pages;

import java.util.HashMap;
import java.util.Map;

import backend.cell.Cell;
import backend.cell.ModifiableCell;
import backend.cell.Terrain;
import backend.grid.Shape;
import frontend.wizards.strategies.wizard_pages.util.NumericInputRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * The WizardPage for creating and specifying a grid for a game
 * 
 * @author Andreas
 *
 */
public class GridInstantiationPage extends BaseWizardPage {
	private static final String DEFAULT_TITLE = "Set Grid Attributes";
	private static final String DEFAULT_DESCRIPTION = "Choose the default cell type for the grid.";
	private static final int DEFAULT_NUM_ROWS = 10;
	private static final int DEFAULT_NUM_COLS = 10;

	private VBox vbox;
	private NumericInputRow rows, cols;
	private ComboBox<String> cellShapeChooser;
	private ComboBox<String> terrainChooser;
	private Map<String, Shape> shapeMap;
	private Map<String, Terrain> terrainMap;

	public GridInstantiationPage() {
		this(DEFAULT_TITLE);
	}

	public GridInstantiationPage(String title) {
		this(title, DEFAULT_DESCRIPTION);
	}

	public GridInstantiationPage(String title, String description) {
		super(title, description);
		initialize();
	}

	@Override
	public Region getObject() {
		return vbox;
	}

	public int getRows() {
		return rows.getValue();
	}

	public int getCols() {
		return cols.getValue();
	}

	public Cell getTemplateCell() {
		return new ModifiableCell(null, shapeMap.get(cellShapeChooser.getValue()),
				terrainMap.get(terrainChooser.getValue()));
	}

	private void initialize() {
		vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		rows = new NumericInputRow(null, "Number of Grid Rows: ", "");
		rows.setValue(DEFAULT_NUM_ROWS);
		cols = new NumericInputRow(null, "Number of Grid Columns: ", "");
		cols.setValue(DEFAULT_NUM_COLS);

		Shape[] shapes = Shape.values();
		shapeMap = new HashMap<String, Shape>();
		ObservableList<String> shapeNames = FXCollections.observableArrayList();
		for (int i = 0; i < shapes.length; i++) {
			shapeNames.add(shapes[i].getName());
			shapeMap.put(shapes[i].getName(), shapes[i]);
		}
		cellShapeChooser = new ComboBox<String>(shapeNames);
		cellShapeChooser.setValue(shapeNames.get(0));
		ObservableList<String> terrainNames = FXCollections.observableArrayList();
		terrainMap = new HashMap<String, Terrain>();
		Terrain.getPredefinedTerrain().stream().forEach(terrain -> {
			terrainNames.add(terrain.getName());
			terrainMap.put(terrain.getName(), terrain);
		});
		terrainChooser = new ComboBox<String>(terrainNames);
		terrainChooser.setValue(terrainNames.get(0));

		rows.setOnAction(event -> checkCanNext());
		cols.setOnAction(event -> checkCanNext());
		cellShapeChooser.setOnAction(event -> checkCanNext());
		terrainChooser.setOnAction(event -> checkCanNext());

		vbox.getChildren().addAll(rows.getObject(), cols.getObject(), cellShapeChooser, terrainChooser);
		checkCanNext();
	}

	private void checkCanNext() {
		canNextWritable().setValue(cellShapeChooser.getValue() != null && !cellShapeChooser.getValue().isEmpty()
				&& terrainChooser.getValue() != null && !terrainChooser.getValue().isEmpty() && rows.getValue() != 0
				&& cols.getValue() != 0);
	}

}
