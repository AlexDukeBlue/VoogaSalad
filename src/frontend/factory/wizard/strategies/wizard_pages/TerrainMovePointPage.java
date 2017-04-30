package frontend.factory.wizard.strategies.wizard_pages;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.cell.Terrain;
import backend.util.GameplayState;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.util.IntegerInputRow;
import frontend.factory.wizard.strategies.wizard_pages.util.TableInputView;
import frontend.factory.wizard.strategies.wizard_pages.util.VerticalTableInputView;
import javafx.scene.layout.Region;

/**
 * The WizardPage for setting the move cost of a unit across different terrain
 * 
 * @author Andreas
 *
 */
public class TerrainMovePointPage extends BaseWizardPage {

	private TableInputView table;
	private Map<IntegerInputRow, Terrain> rowToTerrain;

	/**
	 * Creates a new instance of TerrainMovePointPage
	 * 
	 * @param gameState
	 *            the AuthoringGameState that the page will use to populate the
	 *            different terrains
	 * @param descriptionKey
	 *            a String that can be used as a key to a ResourceBundle to set
	 *            the description of the page
	 */
	public TerrainMovePointPage(Controller controller, String descriptionKey) {
		super(controller, descriptionKey);
		initialize();
	}

	@Override
	public Region getNode() {
		return table.getNode();
	}

	public Map<Terrain, Integer> getTerrainMovePoints() {
		return rowToTerrain.keySet().stream()
				.collect(Collectors.toMap(row -> rowToTerrain.get(row), IntegerInputRow::getValue));
	}

	private void initialize() {
		table = new VerticalTableInputView();
		rowToTerrain = new HashMap<>();
		getController().getAuthoringGameState().getTemplateByCategory(GameplayState.TERRAIN).forEach(terrain -> {
			IntegerInputRow row = new IntegerInputRow(getImg(terrain.getImgPath()), terrain.getName(),
					terrain.getDescription());
			rowToTerrain.put(row, (Terrain) terrain);
			table.getChildren().add(row);
		});
		canNextWritable().setValue(true);
	}

}
