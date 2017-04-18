package frontend.wizards.strategies;

import backend.player.Player;
import backend.player.Team;
import frontend.wizards.strategies.wizard_pages.ImageNameDescriptionPage;

public class ItemStrategy extends BaseStrategy<Team> {

	private ImageNameDescriptionPage namePage;

	public ItemStrategy() {
		initialize();
	}

	@Override
	public Team finish() {
		return new Team(namePage.getName(), namePage.getDescription(), namePage.getImagePath(), new Player[0]);
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage();
		getPages().addAll(namePage);
	}

}
