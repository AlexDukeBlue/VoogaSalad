package frontend.factory.worldview;

import controller.Controller;
import frontend.ComponentClickHandler;
import frontend.interfaces.worldview.GridViewExternal;
import frontend.interfaces.worldview.WorldViewExternal;
import frontend.util.BaseUIManager;
import frontend.util.ChatLogView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * SimpleWorldView sets up and displays a Region object that contains a grid of
 * CellViews that represents the state of the grid received from the Controller
 * that is passed to the SimpleWorldView when instantiated.
 * <p>
 * The SimpleWorldView extends the BaseUIManager class so that it can change
 * languages, css stylesheets, and can be updated by the controller through the
 * updatable interface.
 *
 * @author Dylan Peters
 */
class SimpleWorldView extends BaseUIManager<Region> implements WorldViewExternal {

	private GridViewExternal myGrid;
	private BorderPane borderPane;

	/**
	 * Instantiates a new instance of SimpleWorldView. Sets all values to
	 * default.
	 *
	 * @param controller   the controller whose state will be displayed within the
	 *                     SimpleWorldView
	 * @param clickHandler
	 */
	public SimpleWorldView(Controller controller, ComponentClickHandler clickHandler) {
		super(controller, clickHandler);
		initialize();
	}

	public ScrollPane getGridPane() {
		return myGrid.getObject();
	}

	private void initialize() {
		borderPane = new BorderPane();
		myGrid = new SimpleGridView(getController(), getClickHandler());
		AnchorPane centerAnchorPane = new AnchorPane();
		ChatLogView chatLogView = new ChatLogView(getController());
		borderPane.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				chatLogView.setExpandedState(!chatLogView.isExpanded());
			}
		});
		AnchorPane.setBottomAnchor(chatLogView.getObject(), 5.0);
		AnchorPane.setLeftAnchor(chatLogView.getObject(), 30.0);
		AnchorPane.setRightAnchor(chatLogView.getObject(), 30.0);
		centerAnchorPane.getChildren().addAll(chatLogView.getObject());
		centerAnchorPane.setPickOnBounds(false);
		StackPane centerStackPane = new StackPane();
		centerStackPane.getChildren().add(myGrid.getObject());
		centerStackPane.getChildren().add(centerAnchorPane);
		borderPane.setCenter(centerStackPane);
	}

	@Override
	public GridViewExternal getGridView() {
		return myGrid;
	}

	@Override
	public Region getObject() {
		return borderPane;
	}

}
