package frontend.wizards;

import backend.cell.Terrain;
import backend.util.AuthoringGameState;
import frontend.wizards.strategies.TerrainStrategy;
import javafx.stage.Stage;

public class TerrainWizard extends Wizard<Terrain> {
	
	public TerrainWizard(AuthoringGameState gameState) {
		this(new Stage(), gameState);
	}

	TerrainWizard(Stage stage, AuthoringGameState gameState) {
		super(stage, new TerrainStrategy(gameState));
	}
}
