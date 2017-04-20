package frontend.worldview;

import backend.util.VoogaEntity;
import controller.Controller;
import frontend.util.BaseUIManager;
import frontend.util.ChatLogView;
import frontend.worldview.grid.GridView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
public class WorldView extends BaseUIManager<Region> {

	private StackPane centerStackPane;
	private AnchorPane centerAnchorPane;
	private GridView myGrid;
	private BorderPane borderPane;

	/**
	 * Instantiates a new instance of WorldView. Sets all values to default.
	 *
	 * @param controller the controller whose state will be displayed within the
	 *                   WorldView
	 */
	public WorldView(Controller controller) {
		setController(controller);
		initialize();
	}

	/**
	 * Returns
	 *
	 * @return Region object that shows the user a visual representation of the
	 * grid, which can be interacted with to manipulate the back end
	 */
	@Override
	public Region getObject() {
		return borderPane;
	}

//	/**
//	 * Sets the action that is performed when a cell is clicked.
//	 * 
//	 * @param consumer
//	 *            consumer to execute when the cell is clicked
//	 */
//	public void setOnCellClick(Consumer<CellView> consumer) {
//		myGrid.setOnCellClick(consumer);
//	}

	public void templateClicked(VoogaEntity voogaEntity) {
		myGrid.setTemplateEntityToAdd(voogaEntity);
		
	}

	private void initialize() {
		borderPane = new BorderPane();
		myGrid = new GridView(getController());
		centerAnchorPane = new AnchorPane();
		Rectangle test = new Rectangle(200, 200, Color.TRANSPARENT);
		test.setStroke(Color.RED);
		test.setStrokeWidth(1);
		AnchorPane.setTopAnchor(test, 0.0);
		AnchorPane.setRightAnchor(test, 0.0);
		ChatLogView chatLogView = new ChatLogView(getController());
		borderPane.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				chatLogView.setExpandedState(!chatLogView.isExpanded());
			}
		});
		AnchorPane.setBottomAnchor(chatLogView.getObject(), 10.0);
		AnchorPane.setLeftAnchor(chatLogView.getObject(), 0.0);
		centerAnchorPane.getChildren().addAll(test, chatLogView.getObject());
		centerAnchorPane.setPickOnBounds(false);
		centerStackPane = new StackPane();
		centerStackPane.getChildren().add(myGrid.getObject());
		centerStackPane.getChildren().add(centerAnchorPane);
		borderPane.setCenter(centerStackPane);
	}
}
