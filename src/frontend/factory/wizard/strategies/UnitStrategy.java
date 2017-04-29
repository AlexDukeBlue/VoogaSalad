package frontend.factory.wizard.strategies;

import java.util.Optional;

import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.unit.properties.ModifiableUnitStat;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.AbilitiesAdderPage;
import frontend.factory.wizard.strategies.wizard_pages.GridPatternPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.strategies.wizard_pages.TerrainMovePointPage;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;

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
	private GridPatternPage gridPatternPageRange;

	/**
	 * Creates a new instance of UnitStrategy. Uses the gameState to gather
	 * information about abilities the unit can be given and terrain that the
	 * unit can cross.
	 * 
	 * @param gameState
	 *            the AuthoringGameState that the UnitStrategy will use to
	 *            instantiate the unit.
	 */
	public UnitStrategy(Controller controller) {
		super(controller);
		initialize();
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
		unit.setRangePattern(gridPatternPageRange.getGridPattern());
		return unit;
	}

	private void initialize() {
		imageNameDescriptionPage = new ImageNameDescriptionPage(getController(), "UnitNameDescription");
		abilitiesAdderPage = new AbilitiesAdderPage(getController(), "UnitAbilitiesAdderDescription");
		terrainMovePointPage = new TerrainMovePointPage(getController(), "UnitTerrainDescription");
		gridPatternPage = new GridPatternPage(getController(), "UnitGridPatternDescription", Color.WHITE, Color.GREEN);
		gridPatternPageRange = new GridPatternPage(getController(), "UnitGridPatternRangeDescription", Color.WHITE, Color.YELLOW);
		getPages().addAll(imageNameDescriptionPage, abilitiesAdderPage, terrainMovePointPage, gridPatternPage);
		if(isARangedUnit()) getPages().add(gridPatternPageRange);
	}
	
	private boolean isARangedUnit()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Ranged");
		alert.setHeaderText("Does this unit have a ranged attack?");
		alert.setContentText("Select yes if this unit can attack units that it is not adjacent to without moving.");

		ButtonType buttonTypeOne = new ButtonType("Yes");
		ButtonType buttonTypeTwo = new ButtonType("No");

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

		Optional<ButtonType> result = alert.showAndWait();
		return (result.get() == buttonTypeOne);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("UnitTitle");
	}

}