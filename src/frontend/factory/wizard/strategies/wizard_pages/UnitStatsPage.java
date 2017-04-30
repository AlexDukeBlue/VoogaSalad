package frontend.factory.wizard.strategies.wizard_pages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.unit.properties.ModifiableUnitStat;
import backend.unit.properties.UnitStat;
import backend.util.GameplayState;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.util.IntegerInputRow;
import frontend.factory.wizard.strategies.wizard_pages.util.TableInputView;
import frontend.factory.wizard.strategies.wizard_pages.util.VerticalTableInputView;
import javafx.beans.binding.StringBinding;
import javafx.scene.layout.Region;

public class UnitStatsPage extends BaseWizardPage {

	private TableInputView table;
	private Map<IntegerInputRow, UnitStat> rowToStat;

	public UnitStatsPage(Controller controller, String descriptionKey) {
		super(controller, descriptionKey);
		initialize();
	}

	@Override
	public Region getNode() {
		return table.getNode();
	}

	private void initialize() {
		table = new VerticalTableInputView();
		rowToStat = new HashMap<>();
		getController().getTemplatesByCategory(GameplayState.UNIT_STAT).stream().forEach(unitStat -> {
			IntegerInputRow statRow = new IntegerInputRow(getImg(unitStat.getImgPath()),
					getPolyglot().get("UnitStatRowPrompt"), new StringBinding() {
						@Override
						protected String computeValue() {
							return unitStat.getFormattedName();
						}
					});
			statRow.setValue(((UnitStat) unitStat).getMaxValue());
			table.getChildren().add(statRow);
			rowToStat.put(statRow, (UnitStat) unitStat);
		});
		canNextWritable().setValue(true);
	}

	public Collection<UnitStat> getStats() {
		return rowToStat.keySet().stream().map(unitRow -> {
			ModifiableUnitStat stat = (ModifiableUnitStat) rowToStat.get(unitRow);
			stat.setMinValue(unitRow.getNumberValue()).setMaxValue(unitRow.getNumberValue())
					.setCurrentValue(unitRow.getNumberValue());
			return stat;
		}).collect(Collectors.toList());
	}

}
