package frontend.factory.wizard.strategies;

import java.util.Optional;
import java.util.stream.Collectors;

import backend.cell.Terrain;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.unit.properties.InteractionModifier;
import backend.util.GameplayState;
import backend.util.TriggeredEffect;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.ActiveAbilitiesAdderPage;
import frontend.factory.wizard.strategies.wizard_pages.EntityMovePointPage;
import frontend.factory.wizard.strategies.wizard_pages.GridPatternPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.strategies.wizard_pages.PassiveAbilitiesAdderPage;
import frontend.factory.wizard.strategies.wizard_pages.UnitStatsPage;
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
	private PassiveAbilitiesAdderPage passiveAbilitiesAdderPage;
	private ActiveAbilitiesAdderPage activeAbilitiesAdderPage;
	private EntityMovePointPage terrainMovePointPage;
	private UnitStatsPage statsPage;
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
	public UnitStrategy(Controller controller) {
		super(controller);
		initialize();
	}

	/**
	 * Returns a fully instantiated Unit object.
	 */
	@Override
	public Unit finish() {
		ModifiableUnit unit = new ModifiableUnit(imageNameDescriptionPage.getName())
				.setDescription(imageNameDescriptionPage.getDescriptionLabelBinding().getValue())
				.setImgPath(imageNameDescriptionPage.getImagePath());
		unit.removeActiveAbilities(unit.getActiveAbilities());
		unit.addActiveAbilities(activeAbilitiesAdderPage.getSelectedAbilities())
				.addDefensiveModifiers(passiveAbilitiesAdderPage
						.getPassiveAbilitiesByCategory(GameplayState.DEFENSIVE_MODIFIER).stream()
						.map(ability -> (InteractionModifier<Double>) ability).collect(Collectors.toList()))
				.addOffensiveModifiers(passiveAbilitiesAdderPage
						.getPassiveAbilitiesByCategory(GameplayState.OFFENSIVE_MODIFIER).stream()
						.map(ability -> (InteractionModifier<Double>) ability).collect(Collectors.toList()))
				.addTriggeredAbilities(
						passiveAbilitiesAdderPage.getPassiveAbilitiesByCategory(GameplayState.UNIT_TRIGGERED_EFFECT)
								.stream().map(ability -> (TriggeredEffect) ability).collect(Collectors.toList()))
				.addUnitStats(statsPage.getStats())
				.setTerrainMoveCosts(
						terrainMovePointPage.getEntityMovePoints().keySet().stream().map(entity -> (Terrain) entity)
								.collect(Collectors.toMap(entity -> entity,
										entity -> terrainMovePointPage.getEntityMovePoints().get(entity))))
				.setMovePattern(gridPatternPage.getGridPattern());
		return unit;
	}

	private void initialize() {
		imageNameDescriptionPage = new ImageNameDescriptionPage(getController(), "UnitNameDescription");
		passiveAbilitiesAdderPage = new PassiveAbilitiesAdderPage(getController(),
				"UnitPassiveAbilitiesAdderDescription", GameplayState.DEFENSIVE_MODIFIER,
				GameplayState.OFFENSIVE_MODIFIER, GameplayState.UNIT_TRIGGERED_EFFECT);
		activeAbilitiesAdderPage = new ActiveAbilitiesAdderPage(getController(), "UnitActiveAbilitiesAdderDescription");
		terrainMovePointPage = new EntityMovePointPage(getController(), "UnitTerrainDescription",
				GameplayState.TERRAIN);
		statsPage = new UnitStatsPage(getController(), "UnitStatsPageDescription");
		gridPatternPage = new GridPatternPage(getController(), "UnitGridPatternDescription", Color.WHITE, Color.GREEN);
		getPages().addAll(imageNameDescriptionPage, activeAbilitiesAdderPage, passiveAbilitiesAdderPage,
				terrainMovePointPage, statsPage, gridPatternPage);
	}

	private boolean isARangedUnit() {
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