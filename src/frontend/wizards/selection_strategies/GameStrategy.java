package frontend.wizards.selection_strategies;

import backend.grid.BoundsHandler;
import backend.grid.ModifiableGameBoard;
import backend.util.AuthoringGameState;
import frontend.wizards.wizard_pages.GridInstantiationPage;
import frontend.wizards.wizard_pages.ImageNameDescriptionPage;

/**
 * GameStrategy implements the SelectionStrategy interface in order to allow the
 * user to instantiate new AuthoringGameStates.
 * 
 * @author Dylan Peters
 *
 */
public class GameStrategy extends BaseStrategy<AuthoringGameState> implements WizardStrategy<AuthoringGameState> {

	private ImageNameDescriptionPage gameNamePage;
	private ImageNameDescriptionPage boardNamePage;
	private GridInstantiationPage gridInstantiationPage;

	public GameStrategy() {
		initialize();
	}

	@Override
	public AuthoringGameState finish() {
		ModifiableGameBoard boardBuilder = new ModifiableGameBoard(boardNamePage.getName());
		boardBuilder.setDescription(boardNamePage.getDescription());
		boardBuilder.setImgPath(boardNamePage.getImagePath());
		boardBuilder.setRows(gridInstantiationPage.getRows());
		boardBuilder.setColumns(gridInstantiationPage.getCols());
		boardBuilder.setTemplateCell(gridInstantiationPage.getTemplateCell());
		boardBuilder.setBoundsHandler(BoundsHandler.FINITE_BOUNDS);
		AuthoringGameState gameState = new AuthoringGameState(gameNamePage.getName());
		gameState.setDescription(gameNamePage.getDescription());
		gameState.setImgPath(gameNamePage.getImagePath());
		gameState.setGrid(boardBuilder.build());
		return gameState;
	}

	private void initialize() {
		gameNamePage = new ImageNameDescriptionPage(getString("CreateNewGame"), getString("CreateNewGameDesc"));
		boardNamePage = new ImageNameDescriptionPage(getString("CreateNewBoard"), getString("CreateNewBoardDesc"));
		gridInstantiationPage = new GridInstantiationPage();
		getPages().addAll(gameNamePage, boardNamePage, gridInstantiationPage);
	}
}