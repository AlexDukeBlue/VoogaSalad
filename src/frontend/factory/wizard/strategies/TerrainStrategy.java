package frontend.factory.wizard.strategies;

import java.util.stream.Collectors;

import backend.cell.ModifiableTerrain;
import backend.cell.Terrain;
import backend.unit.properties.InteractionModifier;
import backend.util.GameplayState;
import backend.util.TriggeredEffect;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.EntityMovePointPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.strategies.wizard_pages.PassiveAbilitiesAdderPage;
import javafx.beans.binding.StringBinding;

/**
 * Strategy that extends BaseStrategy to instantiate a new Terrain object.
 * 
 * @author Dylan Peters
 *
 */
class TerrainStrategy extends BaseStrategy<Terrain> {

	private ImageNameDescriptionPage imageNameDescriptionPage;
	private EntityMovePointPage unitMovePointPage;
	private PassiveAbilitiesAdderPage passiveAbilitiesPage;

	/**
	 * Creates a new instance of TerrainStrategy.
	 * 
	 * @param gameState
	 *            the AuthoringGameState that will be used to populate parts of
	 *            the wizard.
	 */
	public TerrainStrategy(Controller controller) {
		super(controller);
		initialize();
	}

	/**
	 * Returns a fully instantiated Terrain object.
	 */
	@Override
	public Terrain finish() {
		ModifiableTerrain terrain = new ModifiableTerrain(imageNameDescriptionPage.getName());
		terrain.setDescription(imageNameDescriptionPage.getDescriptionBoxText());
		terrain.setImgPath(imageNameDescriptionPage.getImagePath());
		terrain.setSoundPath(imageNameDescriptionPage.getSoundPath());
		terrain.setDefaultMoveCost(unitMovePointPage.getDefault());
		passiveAbilitiesPage.getPassiveAbilitiesByCategory(GameplayState.DEFENSIVE_MODIFIER).stream()
				.map(ability -> (InteractionModifier<Double>) ability).collect(Collectors.toList())
				.forEach(modifier -> terrain.addDefensiveModifiers(modifier));
		passiveAbilitiesPage.getPassiveAbilitiesByCategory(GameplayState.OFFENSIVE_MODIFIER).stream()
				.map(ability -> (InteractionModifier<Double>) ability).collect(Collectors.toList())
				.forEach(modifier -> terrain.addOffensiveModifiers(modifier));
		passiveAbilitiesPage.getPassiveAbilitiesByCategory(GameplayState.CELL_TRIGGERED_EFFECT).stream()
				.map(ability -> (TriggeredEffect) ability).collect(Collectors.toList())
				.forEach(modifier -> terrain.addTriggeredAbilities(modifier));
		return terrain;
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("TerrainTitle");
	}

	private void initialize() {
		imageNameDescriptionPage = new ImageNameDescriptionPage(getController(), "TerrainNameDescription", true);
		unitMovePointPage = new EntityMovePointPage(getController(), "TerrainMovePointDescription", GameplayState.UNIT);
		passiveAbilitiesPage = new PassiveAbilitiesAdderPage(getController(), "TerrainPassiveAbilitiesDescription",
				GameplayState.CELL_TRIGGERED_EFFECT, GameplayState.DEFENSIVE_MODIFIER,
				GameplayState.OFFENSIVE_MODIFIER);
		getPages().addAll(imageNameDescriptionPage, unitMovePointPage, passiveAbilitiesPage);
	}

}
