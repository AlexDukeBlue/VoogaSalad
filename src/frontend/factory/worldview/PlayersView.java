package frontend.factory.worldview;

import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * @author Created by th174 on 4/27/2017.
 */
public class PlayersView extends BaseUIManager<GridPane> {
	
	private final SimpleObjectProperty<Color> DEFAULT_COLOR = new SimpleObjectProperty<>(Color.BLACK);
	private final GridPane gridPane;
	private final Label playersHeader;
	private final Label teamsHeader;

	public PlayersView(Controller controller) {
		super(controller);
		gridPane = new GridPane();
		playersHeader = new Label("Player:");
		GridPane.setHalignment(playersHeader, HPos.CENTER);
		playersHeader.textFillProperty().bind(DEFAULT_COLOR);
		teamsHeader = new Label("Team:");
		teamsHeader.textFillProperty().bind(DEFAULT_COLOR);
		GridPane.setHalignment(teamsHeader, HPos.CENTER);
		gridPane.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 0.5), new CornerRadii(10), null)));
		gridPane.setAlignment(Pos.TOP_RIGHT);
		gridPane.setMouseTransparent(true);
		gridPane.setPadding(new Insets(4, 4, 4, 4));
		gridPane.setHgap(10);
	}

	@Override
	public void update() {
		gridPane.getChildren().clear();
		gridPane.add(playersHeader, 1, 0);
		if (!getController().isAuthoringMode()) {
			gridPane.add(teamsHeader, 2, 0);
		}
		for (int i = 0; i < getController().getReadOnlyGameState().getOrderedPlayerNames().size(); i++) {
			String playerName = getController().getReadOnlyGameState().getOrderedPlayerNames().get(i);
			Label playerLabel = new Label(getController().getMyPlayerName().equals(playerName) ? "(You) " + playerName : "" + playerName);
			playerLabel.setPadding(Insets.EMPTY);
			GridPane.setHalignment(playerLabel, HPos.RIGHT);
			playerLabel.textFillProperty().bind(DEFAULT_COLOR);
			gridPane.add(playerLabel, 1, i + 1);
			if (!getController().isAuthoringMode() && Objects.nonNull(getController().getActiveTeam()) && getController().getActiveTeam().equals(getController().getPlayer(playerName).getTeam().orElse(null))) {
				Label currentTurnLabel = new Label(">");
				currentTurnLabel.textFillProperty().bind(DEFAULT_COLOR);
				GridPane.setHalignment(currentTurnLabel, HPos.RIGHT);
				gridPane.add(currentTurnLabel, 0, i + 1);
			}
			int row = i;
			getController().getPlayer(playerName).getTeam().ifPresent(team -> {
				playerLabel.textFillProperty().bind(new SimpleObjectProperty<>(Color.web(team.getColorString())));
				if (!getController().isAuthoringMode()) {
					Label teamLabel = new Label(team.getName());
					teamLabel.textFillProperty().bind(new SimpleObjectProperty<>(Color.web(team.getColorString())));
					GridPane.setHalignment(teamLabel, HPos.RIGHT);
					teamLabel.setPadding(Insets.EMPTY);
					teamLabel.textFillProperty().bind(DEFAULT_COLOR);
					gridPane.add(teamLabel, 2, row + 1);
				}
			});
		}
	}

	@Override
	public GridPane getNode() {
		return gridPane;
	}
}
