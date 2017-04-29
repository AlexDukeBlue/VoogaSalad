package frontend.factory.worldview;

import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.interfaces.worldview.GridViewExternal;
import frontend.interfaces.worldview.WorldViewExternal;
import frontend.util.ChatLogView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * WorldView sets up and displays a Region object that contains a grid of
 * CellViews that represents the state of the grid received from the Controller
 * that is passed to the WorldView when instantiated.
 * <p>
 * The WorldView extends the BaseUIManager class so that it can change
 * languages, css stylesheets, and can be updated by the controller through the
 * updatable interface.
 *
 * @author Dylan Peters
 */
class WorldView extends ClickableUIComponent<Region> implements WorldViewExternal {

	private GridViewExternal myGrid;
	private BorderPane borderPane;

	/**
	 * Instantiates a new instance of WorldView. Sets all values to default.
	 *
	 * @param controller
	 *            the controller whose state will be displayed within the
	 *            WorldView
	 * @param clickHandler
	 */
	public WorldView(Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		initialize();
	}

	@Override
	public GridViewExternal getGridView() {
		return myGrid;
	}

	@Override
	public Region getNode() {
		return borderPane;
	}

	@Override
	public void setClickHandler(ClickHandler clickHandler) {
		super.setClickHandler(clickHandler);
		myGrid.setClickHandler(clickHandler);
	}

	private void initialize() {
		borderPane = new BorderPane();
		myGrid = new GridView(getController(), getClickHandler());
		AnchorPane centerAnchorPane = new AnchorPane();
		ChatLogView chatLogView = new ChatLogView(getController());
		borderPane.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				chatLogView.setExpandedState(!chatLogView.isExpanded());
			}
		});
		AnchorPane.setBottomAnchor(chatLogView.getNode(), 5.0);
		AnchorPane.setLeftAnchor(chatLogView.getNode(), 30.0);
		AnchorPane.setRightAnchor(chatLogView.getNode(), 30.0);
		PlayersView playersView = new PlayersView(getController());
		AnchorPane.setRightAnchor(playersView.getNode(), 2.0);
		AnchorPane.setTopAnchor(playersView.getNode(), 2.0);
		centerAnchorPane.getChildren().addAll(chatLogView.getNode(), playersView.getNode());
		centerAnchorPane.setPickOnBounds(false);
		StackPane centerStackPane = new StackPane();
		centerStackPane.getChildren().add(myGrid.getNode());
		centerStackPane.getChildren().add(centerAnchorPane);
		borderPane.setCenter(centerStackPane);
	}
}
