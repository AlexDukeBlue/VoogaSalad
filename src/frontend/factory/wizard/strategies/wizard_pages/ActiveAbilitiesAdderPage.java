package frontend.factory.wizard.strategies.wizard_pages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.unit.properties.ActiveAbility;
import backend.util.GameplayState;
import backend.util.HasShape;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.util.ActiveAbilityRow;
import frontend.factory.wizard.strategies.wizard_pages.util.TableInputView;
import frontend.factory.wizard.strategies.wizard_pages.util.VerticalTableInputView;
import javafx.scene.layout.Region;

public class ActiveAbilitiesAdderPage extends BaseWizardPage {

	private TableInputView table;
	private Map<ActiveAbilityRow, ActiveAbility<?>> rowToAbility;

	public ActiveAbilitiesAdderPage(Controller controller, String descriptionKey) {
		super(controller, descriptionKey);
		initialize();
	}

	@Override
	public Region getNode() {
		return table.getNode();
	}

	/**
	 * Returns a collection of ActiveAbility objects that the use has selected.
	 * 
	 * @return a collection of ActiveAbility objects that the use has selected.
	 */
	public Collection<ActiveAbility> getSelectedAbilities() {
		return rowToAbility.keySet().stream().filter(ActiveAbilityRow::getSelected).map(row -> rowToAbility.get(row))
				.collect(Collectors.toList());
	}

	private void initialize() {
		table = new VerticalTableInputView();

		rowToAbility = new HashMap<>();
		getController().getTemplatesByCategory(GameplayState.ACTIVE_ABILITY).stream().filter(
				e -> ((HasShape) e).getShape().equals(getController().getAuthoringGameState().getGrid().getShape()))
				.forEach(ability -> {
					ActiveAbilityRow row = new ActiveAbilityRow((ActiveAbility<?>) ability);
					rowToAbility.put(row, (ActiveAbility<?>) ability);
					table.getChildren().add(row);
				});

		canNextWritable().setValue(true);
	}

}
