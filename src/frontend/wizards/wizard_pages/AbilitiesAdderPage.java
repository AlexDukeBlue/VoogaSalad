package frontend.wizards.wizard_pages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.unit.properties.ActiveAbility;
import backend.util.AuthoringGameState;
import frontend.wizards.util.SelectableInputRow;
import frontend.wizards.util.VerticalTableInputView;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;

/**
 * The WizardPage for letting users add abilities
 * @author Andreas
 *
 */
public class AbilitiesAdderPage extends WizardPage {

	private VerticalTableInputView table;
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
			Image image;
			try {
				image = new Image(ability.getImgPath());
			} catch (Exception e) {
				image = null;
			}
			SelectableInputRow row = new SelectableInputRow(image, ability.getName(), ability.getDescription());
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
