package frontend.factory.wizard.wizards.strategies;

import backend.cell.ModifiableTerrain;
import backend.cell.Terrain;
import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.UnitMovePointPage;
import polyglot.PolyglotException;

/**
 * Strategy that extends BaseStrategy to instantiate a new Terrain object.
 * 
 * @author Dylan Peters
 *
 */
class TerrainStrategy extends BaseStrategy<Terrain> {

	private ImageNameDescriptionPage imageNameDescriptionPage;
	// private AbilitiesAdderPage abilitiesAdderPage;
	private UnitMovePointPage unitMovePointPage;

	public TerrainStrategy(AuthoringGameState gameState) {
		initialize(gameState);
	}

	private void initialize(AuthoringGameState gameState) {
		imageNameDescriptionPage = new ImageNameDescriptionPage(getPolyglot().get("CreateNewTerrain"));
		unitMovePointPage = new UnitMovePointPage(gameState);
		getPages().addAll(imageNameDescriptionPage, unitMovePointPage);
		getPolyglot().setOnLanguageChange(event -> {
			try {
				//getPolyglot().setLanguage(getPolyglot().getLanguage());
				imageNameDescriptionPage.getPolyglot().setLanguage(getPolyglot().getLanguage());
				unitMovePointPage.getPolyglot().setLanguage(getPolyglot().getLanguage());
			} catch (PolyglotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	@Override
	public Terrain finish() {
		ModifiableTerrain terrain = new ModifiableTerrain(imageNameDescriptionPage.getName());
		terrain.setDescription(imageNameDescriptionPage.getDescription().getValue());
		terrain.setImgPath(imageNameDescriptionPage.getImagePath());
		terrain.setDefaultMoveCost(unitMovePointPage.getValue());
		return terrain;
	}

}
