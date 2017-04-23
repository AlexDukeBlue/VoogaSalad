package frontend.factory.wizard.wizards.strategies.wizard_pages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.unit.properties.ActiveAbility;
import backend.util.AuthoringGameState;
import frontend.View;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.HorizontalTableInputView;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.SelectableInputRow;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.TableInputView;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.VerticalTableInputView;
import javafx.scene.layout.Region;

/**
 * The WizardPage for letting users add abilities
 * @author Andreas
 *
 */
public class AbilitiesAdderPage extends BaseWizardPage {

	private TableInputView table;
	private Map<SelectableInputRow, ActiveAbility<?>> rowToAbility;

	public AbilitiesAdderPage(AuthoringGameState gameState) {
		initialize(gameState);
	}

	@Override
	public Region getObject() {
		return table.getObject();
	}

	private void initialize(AuthoringGameState gameState) {
		table = new VerticalTableInputView();
		rowToAbility = new HashMap<>();
		gameState.getTemplateByCategory(AuthoringGameState.ACTIVE_ABILITY).forEach(ability -> {
			SelectableInputRow row = new SelectableInputRow(View.getImg(ability.getImgPath()), ability.getName(), ability.getDescription());
			rowToAbility.put(row, (ActiveAbility<?>) ability);
			table.getChildren().add(row);
		});
		canNextWritable().setValue(true);
	}

	public Collection<ActiveAbility> getSelectedAbilities() {
		return rowToAbility.keySet().stream().filter(SelectableInputRow::getSelected).map(row -> rowToAbility.get(row))
				.collect(Collectors.toList());
	}

}
