package frontend.factory.wizard.strategies;

import java.util.Collections;

import backend.player.Team;
import controller.Controller;
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
	public TeamStrategy(Controller controller) {
		super(controller);
		initialize();
	}

	/**
	 * Returns a fully instantiated Team object.
	 */
	@Override
	public Team finish() {
		return new Team(namePage.getName(), namePage.getDescriptionBoxText(),
				colorPage.getColorString(), namePage.getImagePath(), Collections.emptyList());
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("TeamTitle");
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage(getController(), "TeamNameDescription");
		colorPage = new ColorPage(getController(), "TeamColorDescription");
		getPages().addAll(namePage, colorPage);
	}

}
