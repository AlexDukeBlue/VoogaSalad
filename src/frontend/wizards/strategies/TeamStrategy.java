package frontend.wizards.strategies;

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
		return null;
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage();
		getPages().addAll(namePage);
	}

}
