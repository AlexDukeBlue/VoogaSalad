package frontend.factory.wizard.wizards.strategies;

import java.util.Collections;

import backend.player.Team;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ColorPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import polyglot.PolyglotException;

class TeamStrategy extends BaseStrategy<Team> {

	private ImageNameDescriptionPage namePage;
	private ColorPage colorPage;

	public TeamStrategy() {
		initialize();
	}

	@Override
	public Team finish() {
		return new Team(namePage.getName(), namePage.getDescription().getValueSafe(), colorPage.getColorString(), namePage.getImagePath(), Collections.emptyList());
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage(getPolyglot().get("NewTeamPrompt"),getPolyglot().get("NewTeamDescription"));
		getPolyglot().setOnLanguageChange(event -> {
			try {
				namePage.getPolyglot().setLanguage(getPolyglot().getLanguage());
			} catch (PolyglotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		colorPage = new ColorPage(getPolyglot().get("NewTeamPrompt"),getPolyglot().get("NewTeamColorDescription"));
		getPages().addAll(namePage, colorPage);
	}

}
