package frontend.wizards.wizard_2_0.selection_strategies;

import java.util.Arrays;

import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.GameState;
import frontend.wizards.wizard_2_0.wizard_pages.AbilitiesAdderPage;
import frontend.wizards.wizard_2_0.wizard_pages.ImageNameDescriptionPage;
import frontend.wizards.wizard_2_0.wizard_pages.TerrainMovePointPage;

public class NewUnitSelectionStrategy extends BaseSelectionStrategy<Unit> {

	private ImageNameDescriptionPage imageNameDescriptionPage;
	private AbilitiesAdderPage abilitiesAdderPage;
	private TerrainMovePointPage terrainMovePointPage;

	public NewUnitSelectionStrategy(GameState gameState) {
		initialize(gameState);
	}

	@Override
	public Unit finish() {
		return new ModifiableUnit(imageNameDescriptionPage.getName());
		// can change ^ to use a constructor with more options, using
		// information retreived from the user by the pages. For example:
		// "return new ModifiableUnit(imageNamePairView.getName(),
		// imageNamePairView.getImage(), abilitiesAdder.getAbilities());"
	}

	private void initialize(GameState gameState) {
		imageNameDescriptionPage = new ImageNameDescriptionPage();
		abilitiesAdderPage = new AbilitiesAdderPage();
		terrainMovePointPage = new TerrainMovePointPage();
		getPages().addAll(Arrays.asList(imageNameDescriptionPage, abilitiesAdderPage, terrainMovePointPage));
	}

}
