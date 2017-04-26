package frontend.factory.wizard.wizards.strategies;

import java.util.ArrayList;

import backend.player.Team;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ColorPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;

class TeamStrategy extends BaseStrategy<Team> {

	private ImageNameDescriptionPage namePage;
	private ColorPage colorPage;

	public TeamStrategy() {
		initialize();
	}

	@Override
	public Team finish() {
		return new Team(namePage.getName(), namePage.getDescription(), colorPage.getColorString(),
				namePage.getImagePath(), new ArrayList<>());
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage();
		colorPage = new ColorPage();
		getPages().addAll(namePage, colorPage);
	}

}
