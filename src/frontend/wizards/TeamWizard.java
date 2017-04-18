package frontend.wizards;

import backend.player.Team;
import frontend.wizards.strategies.TeamStrategy;
import javafx.stage.Stage;

public class TeamWizard extends Wizard<Team> {

	public TeamWizard() {
		this(new Stage());
	}

	TeamWizard(Stage stage) {
		super(stage, new TeamStrategy());
	}

}
