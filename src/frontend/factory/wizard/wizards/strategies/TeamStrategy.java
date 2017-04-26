package frontend.factory.wizard.wizards.strategies;

import backend.player.Player;
import backend.player.Team;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import polyglot.PolyglotException;

class TeamStrategy extends BaseStrategy<Team> {

	private ImageNameDescriptionPage namePage;

	public TeamStrategy() {
		initialize();
	}

	@Override
	public Team finish() {
		return new Team(namePage.getName(), namePage.getDescription().getValue(), namePage.getImagePath(), new Player[0]);
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage();
		
		getPolyglot().setOnLanguageChange(event -> {
			try {
				namePage.getPolyglot().setLanguage(getPolyglot().getLanguage());
			} catch (PolyglotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		getPages().addAll(namePage);
	}

}
