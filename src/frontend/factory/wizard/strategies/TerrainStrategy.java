package frontend.factory.wizard.strategies;

import backend.cell.ModifiableTerrain;
import backend.cell.Terrain;
import backend.util.AuthoringGameState;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.strategies.wizard_pages.UnitMovePointPage;
import javafx.beans.binding.StringBinding;

/**
 * Strategy that extends BaseStrategy to instantiate a new Terrain object.
 * 
 * @author Dylan Peters
 *
 */
class TerrainStrategy extends BaseStrategy<Terrain> {

	private ImageNameDescriptionPage imageNameDescriptionPage;
	private UnitMovePointPage unitMovePointPage;

	/**
	 * Creates a new instance of TerrainStrategy.
	 * 
	 * @param gameState
	 *            the AuthoringGameState that will be used to populate parts of
	 *            the wizard.
	 */
	public TerrainStrategy(AuthoringGameState gameState) {
		initialize(gameState);
	}

	/**
	 * Returns a fully instantiated Terrain object.
	 */
	@Override
	public Terrain finish() {
		ModifiableTerrain terrain = new ModifiableTerrain(imageNameDescriptionPage.getName());
		terrain.setDescription(imageNameDescriptionPage.getDescriptionLabelBinding().getValue());
		terrain.setImgPath(imageNameDescriptionPage.getImagePath());
		terrain.setDefaultMoveCost(unitMovePointPage.getValue());
		return terrain;
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("TerrainTitle");
	}

	private void initialize(AuthoringGameState gameState) {
		imageNameDescriptionPage = new ImageNameDescriptionPage("TerrainNameDescription");
		unitMovePointPage = new UnitMovePointPage(gameState, "TerrainMovePointDescription");
		getPages().addAll(imageNameDescriptionPage, unitMovePointPage);
	}

}
