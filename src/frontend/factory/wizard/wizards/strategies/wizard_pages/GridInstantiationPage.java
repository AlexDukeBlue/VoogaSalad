package frontend.factory.wizard.wizards.strategies.wizard_pages;

import java.util.HashMap;
import java.util.Map;

import backend.cell.Cell;
import backend.cell.ModifiableCell;
import backend.cell.Terrain;
import backend.grid.Shape;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.NumericInputRow;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * The WizardPage for creating and specifying a grid for a game
 * 
 * @author Andreas
 *
 */
public class GridInstantiationPage extends BaseWizardPage {

	private VBox vbox;
	private NumericInputRow rows, cols;
	private ComboBox<String> cellShapeChooser;
	private ComboBox<String> terrainChooser;
	private Map<String, Shape> shapeMap;
	private Map<String, Terrain> terrainMap = new HashMap<>();
	public GridInstantiationPage(String descriptionKey) {
		super(descriptionKey);
		initialize();
	}

	@Override
	public Region getNode() {
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
		rows = new NumericInputRow(null, getPolyglot().get("Num_Grid_Rows"), new StringBinding() {
			@Override
			protected String computeValue() {
				return "";
			}
		});
		rows.setValue(Integer.parseInt((getResourceBundle().getString("DEFAULT_NUM_ROWS"))));
		cols = new NumericInputRow(null, getPolyglot().get("Num_Grid_Cols"), new StringBinding() {
			@Override
			protected String computeValue() {
				return "";
			}
		});
		cols.setValue(Integer.parseInt((getResourceBundle().getString("DEFAULT_NUM_COLS"))));

		Shape[] shapes = Shape.values();
		shapeMap = new HashMap<>();
		ObservableList<String> shapeNames = FXCollections.observableArrayList();
		for (int i = 0; i < shapes.length; i++) {
			shapeNames.add(shapes[i].getName());
			shapeMap.put(shapes[i].getName(), shapes[i]);
		}
		cellShapeChooser = new ComboBox<>(shapeNames);
		cellShapeChooser.setValue(shapeNames.get(0));
		ObservableList<String> terrainNames = FXCollections.observableArrayList();
		Terrain.getPredefinedTerrain().forEach(terrain -> {
			terrainNames.add(terrain.getName());
			terrainMap.put(terrain.getName(), terrain);
		});
		terrainChooser = new ComboBox<>(terrainNames);
		terrainChooser.setValue(terrainNames.get(0));

		rows.setOnAction(event -> checkCanNext());
		cols.setOnAction(event -> checkCanNext());
		cellShapeChooser.setOnAction(event -> checkCanNext());
		terrainChooser.setOnAction(event -> checkCanNext());

		Label cellShape = new Label();
		cellShape.textProperty().bind(getPolyglot().get("Default_Cell_Shape"));
		HBox cellShapeBox = new HBox(cellShape, cellShapeChooser);
		Label terrain = new Label();
		terrain.textProperty().bind(getPolyglot().get("Default_Terrain"));
		HBox terrainBox = new HBox(terrain, terrainChooser);

		vbox.getChildren().addAll(rows.getNode(), cols.getNode(), cellShapeBox, terrainBox);
		checkCanNext();
	}

	private void checkCanNext() {
		canNextWritable().setValue(cellShapeChooser.getValue() != null && !cellShapeChooser.getValue().isEmpty()
				&& terrainChooser.getValue() != null && !terrainChooser.getValue().isEmpty() && rows.getValue() != 0
				&& cols.getValue() != 0);
	}

}
