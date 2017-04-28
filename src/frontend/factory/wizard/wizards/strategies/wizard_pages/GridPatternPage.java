package frontend.factory.wizard.wizards.strategies.wizard_pages;

import backend.grid.GridPattern;
import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.WizardGrid;
import javafx.beans.binding.StringBinding;
import javafx.scene.layout.Region;

public class GridPatternPage extends BaseWizardPage {
	private WizardGrid grid;

	public GridPatternPage(AuthoringGameState gameState) {
		this(new StringBinding() {
			@Override
			protected String computeValue() {
				return "";
			}
		}, gameState);
	}

	public GridPatternPage(StringBinding title, AuthoringGameState gameState) {
		this(title, new StringBinding() {
			@Override
			protected String computeValue() {
				return "";
			}
		}, gameState);
	}

	public GridPatternPage(StringBinding title, StringBinding description, AuthoringGameState gameState) {
		super();
		this.setTitle(title);
		this.setDescription(description);
		initialize(gameState);
	}

	private void initialize(AuthoringGameState gameState) {
		grid = new WizardGrid(gameState);
		canNextWritable().setValue(true);
	}

	@Override
	public Region getNode() {
		return grid.getNode();
	}

	public GridPattern getGridPattern() {
		return grid.getGridPattern();
	}

}
