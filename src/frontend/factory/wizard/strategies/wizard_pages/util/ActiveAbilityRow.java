package frontend.factory.wizard.strategies.wizard_pages.util;

import backend.unit.properties.ActiveAbility;
import frontend.util.BaseUIManager;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

public class ActiveAbilityRow extends BaseUIManager<Parent> {
	private SelectableInputRow selectable;
//	private IntegerInputRow numeric;
	private HBox hbox;

	public ActiveAbilityRow(ActiveAbility<?> ability) {
		super(null);
		initialize(ability);
	}

	@Override
	public Parent getNode() {
		return hbox;
	}

	public boolean getSelected() {
		return selectable.getSelected();
	}

//	public int getCost() {
//		return numeric.getValue();
//	}

	private void initialize(ActiveAbility<?> ability) {
		selectable = new SelectableInputRow(getImg(ability.getImgPath()), ability.getFormattedName(), ability.getDescription());
//		numeric = new IntegerInputRow(null, getPolyglot().get("AbilityPointCost"), null);
		hbox = new HBox(selectable.getNode()/*, numeric.getNode()*/);
	}

}
