package frontend.factory.worldview;

import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

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
		gridPane.setMouseTransparent(true);
		gridPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		update();
	}

	@Override
	public void update() {
		gridPane.getChildren().clear();
		gridPane.addColumn(0, new Label(), subtitle);
		gridPane.addColumn(1, title);
		gridPane.addColumn(1, getController().getReadOnlyGameState().getOrderedPlayerNames().stream().map(e -> String.format("%15s", e)).map(Label::new).peek(e -> e.setTextAlignment(TextAlignment.RIGHT)).toArray(Node[]::new));
	}

	@Override
	public GridPane getObject() {
		return gridPane;
	}
}
