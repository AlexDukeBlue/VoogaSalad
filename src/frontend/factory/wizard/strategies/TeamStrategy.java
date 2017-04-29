package frontend.factory.wizard.strategies;

import java.util.Collections;

import backend.player.Team;
import frontend.factory.wizard.strategies.wizard_pages.ColorPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import javafx.beans.binding.StringBinding;

/**
 * TeamStrategy is a WizardStrategy that allows the user to create new teams.
 * 
 * @author Dylan Peters
 *
 */
class TeamStrategy extends BaseStrategy<Team> {

	private ImageNameDescriptionPage namePage;
	private ColorPage colorPage;

	/**
	 * Creates a new TeamStrategy object.
	 */
	public TeamStrategy() {
		initialize();
	}

	/**
	 * Returns a fully instantiated Team object.
	 */
	@Override
	public Team finish() {
		return new Team(namePage.getName(), namePage.getDescriptionLabelBinding().getValueSafe(),
				colorPage.getColorString(), namePage.getImagePath(), Collections.emptyList());
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("TeamTitle");
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage("TeamNameDescription");
		colorPage = new ColorPage("TeamColorDescription");
		getPages().addAll(namePage, colorPage);
	}

}
