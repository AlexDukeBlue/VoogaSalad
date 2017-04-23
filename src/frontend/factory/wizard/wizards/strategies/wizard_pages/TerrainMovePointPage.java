package frontend.factory.wizard.wizards.strategies.wizard_pages;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.cell.Terrain;
import backend.util.AuthoringGameState;
import frontend.View;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.NumericInputRow;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.TableInputView;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.VerticalTableInputView;
import javafx.scene.layout.Region;

/**
 * The WizardPage for setting the move cost of a unit across different terrain
 * @author Andreas
 *
 */
public class TerrainMovePointPage extends BaseWizardPage {
	private static final String DEFAULT_TITLE = "Set Movement Points";
	private static final String DEFAULT_DESCRIPTION = "Enter the number of movement points it takes for the unit to cross each terrain type.";

	private TableInputView table;
	private Map<NumericInputRow, Terrain> rowToTerrain;
	
	public TerrainMovePointPage(AuthoringGameState gameState){
		this(DEFAULT_TITLE,gameState);
	}

	public TerrainMovePointPage(String title, AuthoringGameState gameState){
		this(title,DEFAULT_DESCRIPTION, gameState);
	}
	
	public TerrainMovePointPage(String title, String description, AuthoringGameState gameState) {
		super(title, description);
		initialize(gameState);
	}

	@Override
	public Region getObject() {
		return table.getObject();
	}

	private void initialize(AuthoringGameState gameState) {
		table = new VerticalTableInputView();
		rowToTerrain = new HashMap<>();
		gameState.getTemplateByCategory(AuthoringGameState.TERRAIN).forEach(terrain -> {
			NumericInputRow row = new NumericInputRow(View.getImg(terrain.getImgPath()), terrain.getName(), terrain.getDescription());
			rowToTerrain.put(row, (Terrain)terrain);
			table.getChildren().add(row);
		});
		canNextWritable().setValue(true);
	}

	public Map<Terrain, Integer> getTerrainMovePoints() {
		return rowToTerrain.keySet().stream()
				.collect(Collectors.toMap(row -> rowToTerrain.get(row), NumericInputRow::getValue));
	}

}
