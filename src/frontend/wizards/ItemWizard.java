package frontend.wizards;

import backend.player.Team;
import frontend.wizards.strategies.ItemStrategy;
import javafx.stage.Stage;

public class ItemWizard extends Wizard<Team> {

	public ItemWizard() {
		this(new Stage());
	}

	ItemWizard(Stage stage) {
		super(stage, new ItemStrategy());
	}

}
