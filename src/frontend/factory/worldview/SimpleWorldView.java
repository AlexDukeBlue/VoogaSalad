package frontend.factory.worldview;

import controller.Controller;
import frontend.interfaces.GameObserver;
import frontend.interfaces.worldview.*;
import frontend.util.BaseUIManager;
import frontend.util.ChatLogView;
import frontend.worldview.grid.GridView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Collection;

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
	private Collection<WorldViewObserver> observers;

	/**
	 * Instantiates a new instance of SimpleWorldView. Sets all values to
	 * default.
	 *
	 * @param controller the controller whose state will be displayed within the
	 *                   SimpleWorldView
	 */
	public SimpleWorldView(Controller controller, GameObserver gameDelegate) {
		super(controller);
		initialize(gameDelegate);
	}

	public ScrollPane getGridPane() {
		return myGrid.getObject();
	}

	private void initialize(GameObserver gameDelegate) {
		borderPane = new BorderPane();
		observers = new ArrayList<>();
		myGrid = new GridView(getController());
		myGrid.addGridViewObserver(gameDelegate);
		myGrid.addUnitViewObserver(gameDelegate);
		myGrid.addCellViewObserver(gameDelegate);
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
	public void addGridViewObserver(GridViewObserver observer) {
		myGrid.addGridViewObserver(observer);
	}

	@Override
	public void addAllGridViewObservers(Collection<GridViewObserver> gridViewObservers) {
		myGrid.addAllGridViewObservers(gridViewObservers);
	}

	@Override
	public void removeGridViewObserver(GridViewObserver observer) {
		myGrid.removeGridViewObserver(observer);
	}

	@Override
	public void removeAllGridViewObservers(Collection<GridViewObserver> gridViewObservers) {
		myGrid.removeAllGridViewObservers(gridViewObservers);
	}

	@Override
	public void addCellViewObserver(CellViewObserver observer) {
		myGrid.addCellViewObserver(observer);
	}

	@Override
	public void addAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		myGrid.addAllCellViewObservers(cellViewObservers);
	}

	@Override
	public void removeCellViewObserver(CellViewObserver observer) {
		myGrid.removeCellViewObserver(observer);
	}

	@Override
	public void removeAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		myGrid.removeAllCellViewObservers(cellViewObservers);
	}

	@Override
	public void addUnitViewObserver(UnitViewObserver observer) {
		myGrid.addUnitViewObserver(observer);
	}

	@Override
	public void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		myGrid.addAllUnitViewObservers(unitViewObservers);
	}

	@Override
	public void removeUnitViewObserver(UnitViewObserver observer) {
		myGrid.removeUnitViewObserver(observer);
	}

	@Override
	public void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		myGrid.removeAllUnitViewObservers(unitViewObservers);
	}

	@Override
	public void addWorldViewObserver(WorldViewObserver observer) {
		if (!observers.contains(observer) && observer != null) {
			observers.add(observer);
		}
	}

	@Override
	public void addAllWorldViewObservers(Collection<WorldViewObserver> worldViewObservers) {
		if (worldViewObservers != null) {
			worldViewObservers.forEach(this::addWorldViewObserver);
		}
	}

	@Override
	public void removeWorldViewObserver(WorldViewObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	@Override
	public void removeAllWorldViewObservers(Collection<WorldViewObserver> worldViewObservers) {
		if (worldViewObservers != null) {
			worldViewObservers.forEach(this::removeWorldViewObserver);
		}
	}

	@Override
	public GridViewExternal getGridViewExternal() {
		return myGrid;
	}

	@Override
	public Region getObject() {
		return borderPane;
	}

}
