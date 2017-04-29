package frontend.factory.wizard.wizards.strategies;

import backend.cell.ModifiableTerrain;
import backend.cell.Terrain;
import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.UnitMovePointPage;
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

	public TerrainStrategy(AuthoringGameState gameState) {
		initialize(gameState);
	}

	private void initialize(AuthoringGameState gameState) {
		imageNameDescriptionPage = new ImageNameDescriptionPage("TerrainNameDescription");
		unitMovePointPage = new UnitMovePointPage(gameState, "TerrainMovePointDescription");
		getPages().addAll(imageNameDescriptionPage, unitMovePointPage);
	}

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

}
