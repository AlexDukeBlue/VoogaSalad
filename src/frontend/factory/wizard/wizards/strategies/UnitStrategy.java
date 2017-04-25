package frontend.factory.wizard.wizards.strategies;

import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.unit.properties.ModifiableUnitStat;
import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.wizard_pages.AbilitiesAdderPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.GridPatternPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.TerrainMovePointPage;
import javafx.beans.binding.StringBinding;

/**
 * UnitStrategy implements the SelectionStrategy interface in order to allow the
 * user to instantiate new Units.
 *
 * @author Dylan Peters
 */
class UnitStrategy extends BaseStrategy<Unit> {

	private ImageNameDescriptionPage imageNameDescriptionPage;
	private AbilitiesAdderPage abilitiesAdderPage;
	private TerrainMovePointPage terrainMovePointPage;
	private GridPatternPage gridPatternPage;

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
		unit.addUnitStats(
				ModifiableUnitStat.HITPOINTS.setMinValue(0.0).setMaxValue(new Double(abilitiesAdderPage.getHP()))
						.setCurrentValue(new Double(abilitiesAdderPage.getHP())));
		unit.setTerrainMoveCosts(terrainMovePointPage.getTerrainMovePoints());
		unit.addUnitStats(
				ModifiableUnitStat.MOVEPOINTS.setMinValue(0).setMaxValue(terrainMovePointPage.getUnitMovePoints())
						.setCurrentValue(terrainMovePointPage.getUnitMovePoints()));
		unit.setMovePattern(gridPatternPage.getGridPattern());
		return unit;
	}

	private void initialize(AuthoringGameState gameState) {
		imageNameDescriptionPage = new ImageNameDescriptionPage(getPolyglot().get("CreateNewUnit"),
				new StringBinding() {

					@Override
					protected String computeValue() {
						return "";
					}

				});
		abilitiesAdderPage = new AbilitiesAdderPage(gameState);
		terrainMovePointPage = new TerrainMovePointPage(getPolyglot().get("Default_TerrainMovePoint_Title"),
				getPolyglot().get("Default_TerrainMovePoint_Description"), gameState);
		gridPatternPage = new GridPatternPage(getPolyglot().get("Default_MovePattern_Title"),
				getPolyglot().get("Default_MovePattern_Description"), gameState);
		getPages().addAll(imageNameDescriptionPage, abilitiesAdderPage, terrainMovePointPage, gridPatternPage);
	}

}
