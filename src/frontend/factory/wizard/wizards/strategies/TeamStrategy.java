package frontend.factory.wizard.wizards.strategies;

import java.util.Collections;

import backend.player.Team;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ColorPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import javafx.beans.binding.StringBinding;

class TeamStrategy extends BaseStrategy<Team> {

	private ImageNameDescriptionPage namePage;
	private ColorPage colorPage;

	public TeamStrategy() {
		initialize();
	}

	@Override
	public Team finish() {
		return new Team(namePage.getName(), namePage.getDescription().getValueSafe(), colorPage.getColorString(),
				namePage.getImagePath(), Collections.emptyList());
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage("TeamNameDescription");
		colorPage = new ColorPage("TeamColorDescription");
		getPages().addAll(namePage, colorPage);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("TeamTitle");
	}

}
