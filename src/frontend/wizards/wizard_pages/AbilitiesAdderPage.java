package frontend.wizards.wizard_pages;

import backend.unit.properties.ActiveAbility;
import backend.util.AuthoringGameState;
import frontend.View;
import frontend.wizards.util.SelectableInputRow;
import frontend.wizards.util.VerticalTableInputView;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The WizardPage for letting users add abilities
 * @author Andreas
 *
 */
public class AbilitiesAdderPage extends BaseWizardPage {

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
				image = View.getImg(ability.getImgPath());
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
