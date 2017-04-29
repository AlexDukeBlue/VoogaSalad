package frontend.factory.wizard.wizards.strategies.wizard_pages;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.util.AuthoringGameState;
import backend.util.Event;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.NumericInputRow;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.SelectableInputRow;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.TableInputView;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.VerticalTableInputView;
import javafx.scene.layout.Region;

public class ActivationTriggersPage extends BaseWizardPage {

	private TableInputView table;
	private Map<SelectableInputRow, Event> rowToTrigger;
	private NumericInputRow hprow;

	public ActivationTriggersPage(AuthoringGameState gameState, String descriptionKey) {
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

		rowToTrigger = new HashMap<>();
		Arrays.asList(Event.values()).stream().forEach(trigger -> {
			SelectableInputRow row = new SelectableInputRow(null, trigger.name(), "");
			rowToTrigger.put(row, (Event) trigger);
			table.getChildren().add(row);
		});
		canNextWritable().setValue(true);
	}

	public Collection<Event> getSelectedActivationTriggers() {
		return rowToTrigger.keySet().stream().filter(SelectableInputRow::getSelected).map(row -> rowToTrigger.get(row))
				.collect(Collectors.toList());
	}

}
