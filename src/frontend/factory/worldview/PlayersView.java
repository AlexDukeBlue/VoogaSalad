package frontend.factory.worldview;

import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

/**
 * @author Created by th174 on 4/27/2017.
 */
public class PlayersView extends BaseUIManager<GridPane> {
	//TODO ResourceBundlify
	private final GridPane gridPane;
	private final Label title;
	private final Label subtitle;

	public PlayersView(Controller controller) {
		super(controller);
		gridPane = new GridPane();
		title = new Label("---Players---");
		title.setTextAlignment(TextAlignment.RIGHT);
		subtitle = new Label("Current Turn: ");
		subtitle.setTextAlignment(TextAlignment.RIGHT);
		gridPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
	}

	@Override
	public void update() {
		gridPane.getChildren().clear();
		gridPane.add(subtitle, 0, 1);
		gridPane.addColumn(1, title);
		gridPane.addColumn(1,
				getController().getReadOnlyGameState().getOrderedPlayerNames().stream()
						.map(e -> new Pair<>(getController().getPlayer(e).getTeam(), new Label(String.format("%15s", e))))
						.peek(e -> e.getValue().textFillProperty().bind(new SimpleObjectProperty<>(Color.web(e.getKey().getColorString()))))
						.peek(e -> e.getValue().setTooltip(new Tooltip(String.format("Team: %s", e.getKey().getName()))))
						.map(Pair::getValue)
						.peek(e -> e.setTextAlignment(TextAlignment.RIGHT))
						.toArray(Node[]::new));
	}

	@Override
	public GridPane getObject() {
		return gridPane;
	}
}
