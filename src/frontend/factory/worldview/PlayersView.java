package frontend.factory.worldview;

import backend.player.Team;
import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * @author Created by th174 on 4/27/2017.
 */
public class PlayersView extends BaseUIManager<GridPane> {
	//TODO ResourceBundlify
	private final GridPane gridPane;
	private final Label playersHeader;
	private final Label currentTurnLabel;
	private final Label teamsHeader;

	public PlayersView(Controller controller) {
		super(controller);
		gridPane = new GridPane();
		playersHeader = new Label("Player:");
		GridPane.setHalignment(playersHeader, HPos.CENTER);
		teamsHeader = new Label("Team:");
		GridPane.setHalignment(teamsHeader, HPos.CENTER);
		currentTurnLabel = new Label(">");
		GridPane.setHalignment(currentTurnLabel, HPos.RIGHT);
		gridPane.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 0.4), null, null)));
		gridPane.setAlignment(Pos.TOP_RIGHT);
		gridPane.setMouseTransparent(true);
		gridPane.setHgap(10);
	}

	@Override
	public void update() {
		gridPane.getChildren().clear();
		gridPane.add(currentTurnLabel, 0, 1);
		gridPane.add(playersHeader, 1, 0);
		gridPane.add(teamsHeader, 2, 0);
		for (int i = 0; i < getController().getReadOnlyGameState().getOrderedPlayerNames().size(); i++) {
			String e = getController().getReadOnlyGameState().getOrderedPlayerNames().get(i);
			Team team = getController().getPlayer(e).getTeam();
			Label playerLabel = new Label(getController().getMyPlayerName().equals(e) ? "(You) " + e : "" + e);
			Label teamLabel = new Label(String.format("%16s", team.getName()));
			playerLabel.textFillProperty().bind(new SimpleObjectProperty<>(Color.web(team.getColorString())));
			GridPane.setHalignment(playerLabel, HPos.RIGHT);
			playerLabel.setPadding(Insets.EMPTY);
			teamLabel.textFillProperty().bind(new SimpleObjectProperty<>(Color.web(team.getColorString())));
			GridPane.setHalignment(playerLabel, HPos.RIGHT);
			teamLabel.setPadding(Insets.EMPTY);
			gridPane.add(playerLabel, 1, i + 1);
			gridPane.add(teamLabel, 2, i + 1);
		}
	}

	@Override
	public GridPane getObject() {
		return gridPane;
	}
}
