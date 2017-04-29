package frontend.factory.wizard.wizards.strategies.wizard_pages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.unit.properties.ActiveAbility;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.HasShape;
import frontend.View;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.NumericInputRow;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.SelectableInputRow;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.TableInputView;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.VerticalTableInputView;
import javafx.scene.layout.Region;

/**
 * The WizardPage for letting users add abilities
 *
 * @author Andreas
 */
public class AbilitiesAdderPage extends BaseWizardPage {

	private TableInputView table;
	private Map<SelectableInputRow, ActiveAbility<?>> rowToAbility;
	private NumericInputRow hprow;

	public AbilitiesAdderPage(AuthoringGameState gameState, String descriptionKey) {
		super(descriptionKey);
		initialize(gameState);
	}

	@Override
	public Region getNode() {
		return table.getNode();
	}

	private void initialize(AuthoringGameState gameState) {
		table = new VerticalTableInputView();

		hprow = new NumericInputRow(null, getPolyglot().get("HP_Prompt"), getPolyglot().get("HP"));
		table.getChildren().add(hprow);

		rowToAbility = new HashMap<>();
		gameState.getTemplateByCategory(GameplayState.ACTIVE_ABILITY).stream()
				.filter(e -> ((HasShape) e).getShape().equals(gameState.getGrid().getShape())).forEach(ability -> {
					SelectableInputRow row = new SelectableInputRow(View.getImg(ability.getImgPath()),
							ability.getName(), ability.getDescription());
					rowToAbility.put(row, (ActiveAbility<?>) ability);
					table.getChildren().add(row);
				});
		canNextWritable().setValue(true);
	}

	public Collection<ActiveAbility> getSelectedAbilities() {
		return rowToAbility.keySet().stream().filter(SelectableInputRow::getSelected).map(row -> rowToAbility.get(row))
				.collect(Collectors.toList());
	}

	public Integer getHP() {
		return hprow.getValue();
	}

}
