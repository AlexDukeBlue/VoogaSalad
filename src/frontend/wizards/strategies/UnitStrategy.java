package frontend.wizards.strategies;

import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import frontend.wizards.strategies.wizard_pages.AbilitiesAdderPage;
import frontend.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.wizards.strategies.wizard_pages.TerrainMovePointPage;

/**
 * UnitStrategy implements the SelectionStrategy interface in order to allow the
 * user to instantiate new Units.
 * 
 * @author Dylan Peters
 *
 */
public class UnitStrategy extends BaseStrategy<Unit> {

	private ImageNameDescriptionPage imageNameDescriptionPage;
	private AbilitiesAdderPage abilitiesAdderPage;
	private TerrainMovePointPage terrainMovePointPage;

	public UnitStrategy(AuthoringGameState gameState) {
		initialize(gameState);
	}

	@Override
	public Unit finish() {
		ModifiableUnit unit = new ModifiableUnit(imageNameDescriptionPage.getName());
		unit.setDescription(imageNameDescriptionPage.getDescription());
		unit.setImgPath(imageNameDescriptionPage.getImagePath());
		unit.removeActiveAbilities(unit.getActiveAbilities());
		unit.addActiveAbilities(abilitiesAdderPage.getSelectedAbilities());
		unit.setTerrainMoveCosts(terrainMovePointPage.getTerrainMovePoints());
		return unit;
	}

	private void initialize(AuthoringGameState gameState) {
		System.out.println("instantiating new unit...");
		imageNameDescriptionPage = new ImageNameDescriptionPage("Create New Unit");
		abilitiesAdderPage = new AbilitiesAdderPage(gameState);
		terrainMovePointPage = new TerrainMovePointPage(gameState);
		getPages().addAll(imageNameDescriptionPage, abilitiesAdderPage, terrainMovePointPage);
	}

}
