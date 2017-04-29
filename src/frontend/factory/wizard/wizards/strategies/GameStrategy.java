package frontend.factory.wizard.wizards.strategies;

import java.util.Collections;

import backend.grid.BoundsHandler;
import backend.grid.ModifiableGameBoard;
import backend.player.Team;
import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.wizard_pages.AdditionalWizardsPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.GridInstantiationPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import javafx.beans.binding.StringBinding;

/**
 * GameStrategy implements the SelectionStrategy interface in order to allow the
 * user to instantiate new AuthoringGameStates.
 *
 * @author Dylan Peters, heavily edited by ncp14 4/18/17
 */
class GameStrategy extends BaseStrategy<AuthoringGameState> implements WizardStrategy<AuthoringGameState> {

	private ImageNameDescriptionPage gameNamePage;
	private ImageNameDescriptionPage boardNamePage;
	private GridInstantiationPage gridInstantiationPage;
	private AdditionalWizardsPage<Team> additionalTeamWizardsPage;
	private AuthoringGameState gameState;

	public GameStrategy() {
		initialize();
	}

	@Override
	public AuthoringGameState finish() {
		ModifiableGameBoard boardBuilder = new ModifiableGameBoard("");
		boardBuilder.setName(boardNamePage.getName());
		boardBuilder.setDescription(boardNamePage.getDescriptionLabelBinding().getValue());
		boardBuilder.setImgPath(boardNamePage.getImagePath());
		boardBuilder.setRows(gridInstantiationPage.getRows());
		boardBuilder.setColumns(gridInstantiationPage.getCols());
		boardBuilder.setTemplateCell(gridInstantiationPage.getTemplateCell());
		boardBuilder.setBoundsHandler(BoundsHandler.INFINITE_BOUNDS);
		//gameState = new AuthoringGameState(gameNamePage.getName());
		gameState.setDescription(gameNamePage.getDescriptionLabelBinding().getValue());
		gameState.setImgPath(gameNamePage.getImagePath());
		gameState.setGrid(boardBuilder.build());
		additionalTeamWizardsPage.getObjects().stream().forEach(e -> gameState.addTeam(e));;
		//gameState.addTeam(new Team("Default", "Default Team", Team.WHITE, null, Collections.emptyList()));
		// gameState.setTeams(additionalTeamWizardsPage.getObjects());
		return gameState;
	}

	private void initialize() {
		gameNamePage = new ImageNameDescriptionPage("GameStrategyGameNameDescription");
		gameState = new AuthoringGameState(gameNamePage.getName());
		boardNamePage = new ImageNameDescriptionPage("GameStrategyBoardNameDescription");
		gridInstantiationPage = new GridInstantiationPage("GameStrategyGridInstantiationDescription");
		
		additionalTeamWizardsPage = new AdditionalWizardsPage<Team>("GameStrategyAdditionalTeamWizardsDescription", "team", gameState);

		getPages().addAll(gameNamePage, boardNamePage, gridInstantiationPage, additionalTeamWizardsPage);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("GameStrategyTitle");
	}
}