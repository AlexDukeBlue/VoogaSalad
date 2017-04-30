package frontend.factory.wizard.strategies.wizard_pages;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.unit.properties.InteractionModifier;
import backend.util.Ability;
import backend.util.GameplayState;
import backend.util.TriggeredEffect;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.util.SelectableInputRow;
import frontend.factory.wizard.strategies.wizard_pages.util.TableInputView;
import frontend.factory.wizard.strategies.wizard_pages.util.VerticalTableInputView;
import javafx.scene.layout.Region;

/**
 * The WizardPage for letting users add abilities
 *
 * @author Andreas
 */
public class PassiveAbilitiesAdderPage extends BaseWizardPage {

	private TableInputView table;
	private Map<SelectableInputRow, Ability> rowToAbility;

	/**
	 * Creates a new instance of PassiveAbilitiesAdderPage. Sets all values to
	 * default.
	 * 
	 * @param gameState
	 *            the AuthoringGameState that the wizard will use to populate
	 *            its fields.
	 * @param descriptionKey
	 *            a String that can be used as a key to a ResourceBundle to set
	 *            the description of the page
	 */
	public PassiveAbilitiesAdderPage(Controller controller, String descriptionKey, String... passiveAbilityTypes) {
		super(controller, descriptionKey);
		initialize(passiveAbilityTypes);
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
	private Collection<Ability> getSelectedAbilities() {
		return rowToAbility.keySet().stream().filter(SelectableInputRow::getSelected).map(row -> rowToAbility.get(row))
				.collect(Collectors.toList());
	}

	private void initialize(String... passiveAbilityTypes) {
		table = new VerticalTableInputView();

		rowToAbility = new HashMap<>();
		Arrays.asList(passiveAbilityTypes).stream()
				.forEach(category -> getController().getTemplatesByCategory(category).stream().forEach(ability -> {
					SelectableInputRow row = new SelectableInputRow(getImg(ability.getImgPath()), ability.getName(),
							ability.getDescription());
					rowToAbility.put(row, (Ability) ability);
					table.getChildren().add(row);
				}));

		canNextWritable().setValue(true);
	}
	
	public Collection<Ability> getPassiveAbilitiesByCategory(String category){
		return getSelectedAbilities().stream().filter(ability -> getController().getTemplatesByCategory(category).contains(ability)).collect(Collectors.toList());
	}

}
