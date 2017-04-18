package frontend.wizards.strategies;

import backend.player.Player;
import backend.player.Team;
import frontend.wizards.wizard_pages.ImageNameDescriptionPage;

public class TeamStrategy extends BaseStrategy<Team> {

	private ImageNameDescriptionPage namePage;

	public TeamStrategy() {
		initialize();
	}

	@Override
	public Team finish() {
		// TODO Auto-generated method stub
		return new Team("asdf", "asdf", "asdf", new Player[0]);
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage();
		getPages().addAll(namePage);
	}

}
