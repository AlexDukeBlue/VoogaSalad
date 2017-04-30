package frontend.factory.wizard.strategies.wizard_pages;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.util.VoogaEntity;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.util.NumericInputRow;
import frontend.factory.wizard.strategies.wizard_pages.util.TableInputView;
import frontend.factory.wizard.strategies.wizard_pages.util.VerticalTableInputView;
import javafx.beans.binding.StringBinding;
import javafx.scene.layout.Region;

/**
 * The WizardPage for setting the move cost of a unit across different terrain
 * 
 * @author Andreas
 *
 */
public class EntityMovePointPage extends BaseWizardPage {

	private TableInputView table;
	private Map<NumericInputRow, VoogaEntity> rowToEntity;
	private NumericInputRow defaultVal;

	/**
	 * Creates a new instance of EntityMovePointPage
	 * 
	 * @param gameState
	 *            the AuthoringGameState that the page will use to populate the
	 *            different terrains
	 * @param descriptionKey
	 *            a String that can be used as a key to a ResourceBundle to set
	 *            the description of the page
	 */
	public EntityMovePointPage(Controller controller, String descriptionKey, String category) {
		super(controller, descriptionKey);
		initialize(category);
	}

	@Override
	public Region getNode() {
		return table.getNode();
	}

	public Map<VoogaEntity, Integer> getEntityMovePoints() {
		return rowToEntity.keySet().stream()
				.collect(Collectors.toMap(row -> rowToEntity.get(row), NumericInputRow::getValue));
	}

	public Integer getDefault() {
		return defaultVal.getValue();
	}

	private void initialize(String category) {
		table = new VerticalTableInputView();
		rowToEntity = new HashMap<>();
		defaultVal = new NumericInputRow(null, getPolyglot().get("DefaultVal"), new StringBinding() {
			@Override
			protected String computeValue() {
				return "";
			}
		});
		table.getChildren().add(defaultVal);
		defaultVal.setOnAction(
				event -> rowToEntity.keySet().stream().forEach(row -> row.setValue(defaultVal.getValue())));

		getController().getAuthoringGameState().getTemplateByCategory(category).forEach(entity -> {
			NumericInputRow row = new NumericInputRow(getImg(entity.getImgPath()), entity.getName(),
					entity.getDescription());
			rowToEntity.put(row, entity);
			table.getChildren().add(row);
		});
		canNextWritable().setValue(true);
	}

}
