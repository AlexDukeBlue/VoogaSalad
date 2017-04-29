package frontend.factory.wizard.strategies;

import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.unit.properties.ModifiableUnitStat;
import backend.util.AuthoringGameState;
import frontend.factory.wizard.strategies.wizard_pages.AbilitiesAdderPage;
import frontend.factory.wizard.strategies.wizard_pages.GridPatternPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.strategies.wizard_pages.TerrainMovePointPage;
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

	/**
	 * Creates a new instance of UnitStrategy. Uses the gameState to gather
	 * information about abilities the unit can be given and terrain that the
	 * unit can cross.
	 * 
	 * @param gameState
	 *            the AuthoringGameState that the UnitStrategy will use to
	 *            instantiate the unit.
	 */
	public UnitStrategy(AuthoringGameState gameState) {
		initialize(gameState);
	}

	/**
	 * Returns a fully instantiated Unit object.
	 */
	@Override
	public Unit finish() {
		ModifiableUnit unit = new ModifiableUnit(imageNameDescriptionPage.getName());
		unit.setDescription(imageNameDescriptionPage.getDescriptionLabelBinding().getValue());
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
		imageNameDescriptionPage = new ImageNameDescriptionPage("UnitNameDescription");
		abilitiesAdderPage = new AbilitiesAdderPage(gameState, "UnitAbilitiesAdderDescription");
		terrainMovePointPage = new TerrainMovePointPage(gameState, "UnitTerrainDescription");
		gridPatternPage = new GridPatternPage(gameState, "UnitGridPatternDescription");

		getPages().addAll(imageNameDescriptionPage, abilitiesAdderPage, terrainMovePointPage, gridPatternPage);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("UnitTitle");
	}

}