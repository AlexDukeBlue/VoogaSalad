package frontend.worldview;

import java.util.function.Consumer;

import backend.grid.GameBoard;
import controller.Controller;
import frontend.util.BaseUIManager;
import frontend.worldview.grid.CellView;
import frontend.worldview.grid.GridView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public class WorldView extends BaseUIManager<Region> {

	private GridView myGrid;
	private BorderPane borderPane;

	public WorldView(Controller controller) {
		setController(controller);
		initialize();
	}

	@Override
	public Region getObject() {
		return borderPane;
	}

	public void setOnCellClick(Consumer<CellView> consumer) {
		myGrid.setOnCellClick(consumer);
	}

	private void initialize() {
		borderPane = new BorderPane();
		myGrid = new GridView(getController());
		borderPane.setCenter(myGrid.getObject());
		// borderPane.setOnDragDetected(event -> System.out.println("WorldView
		// drag drop" + event));
		// borderPane.setOnMouseClicked(event -> System.out.println("WorldView
		// click" + event));
	}
}
