package frontend.factory.wizard.strategies;

import backend.cell.ModifiableTerrain;
import backend.cell.Terrain;
import backend.util.GameplayState;
import backend.util.PassiveAbility;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.EntityMovePointPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
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
		terrain.setDescription(imageNameDescriptionPage.getDescriptionLabelBinding().getValue());
		terrain.setImgPath(imageNameDescriptionPage.getImagePath());
		terrain.setSoundPath(imageNameDescriptionPage.getSoundPath());
		terrain.setDefaultMoveCost(unitMovePointPage.getDefault());
		return terrain;
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("TerrainTitle");
	}

	private void initialize() {
		imageNameDescriptionPage = new ImageNameDescriptionPage(getController(), "TerrainNameDescription", true);
		unitMovePointPage = new EntityMovePointPage(getController(), "TerrainMovePointDescription", GameplayState.UNIT);
		getPages().addAll(imageNameDescriptionPage, unitMovePointPage);
	}

}
