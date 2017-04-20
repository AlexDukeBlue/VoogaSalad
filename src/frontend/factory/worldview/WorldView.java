package frontend.factory.worldview;

import java.util.Arrays;
import java.util.Collection;

import controller.Controller;
import frontend.interfaces.GameObserver;
import frontend.interfaces.worldview.CellViewObserver;
import frontend.interfaces.worldview.GridViewExternal;
import frontend.interfaces.worldview.GridViewObserver;
import frontend.interfaces.worldview.UnitViewObserver;
import frontend.interfaces.worldview.WorldViewExternal;
import frontend.interfaces.worldview.WorldViewObserver;
import frontend.util.BaseUIManager;
import frontend.util.ChatLogView;
import javafx.scene.control.ScrollPane;
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
class WorldView extends BaseUIManager<Region> implements WorldViewExternal {

	private GridViewExternal myGrid;
	private BorderPane borderPane;

	/**
	 * Instantiates a new instance of WorldView. Sets all values to default.
	 *
	 * @param controller
	 *            the controller whose state will be displayed within the
	 *            WorldView
	 */
	public WorldView(Controller controller, GameObserver gameDelegate) {
		setController(controller);
		initialize(gameDelegate);
	}

	public GridViewExternal getGridPane() {
		return myGrid;
	}

	private void initialize(GameObserver gameDelegate) {
		borderPane = new BorderPane();
		myGrid = new SimpleGridView(getController(), Arrays.asList(gameDelegate), Arrays.asList(gameDelegate),
				Arrays.asList(gameDelegate));
		AnchorPane centerAnchorPane = new AnchorPane();
		ChatLogView chatLogView = new ChatLogView(getController());
		borderPane.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				chatLogView.setExpandedState(!chatLogView.isExpanded());
			}
		});
		AnchorPane.setBottomAnchor(chatLogView.getObject(), 10.0);
		AnchorPane.setLeftAnchor(chatLogView.getObject(), 0.0);
		centerAnchorPane.getChildren().addAll(chatLogView.getObject());
		centerAnchorPane.setPickOnBounds(false);
		StackPane centerStackPane = new StackPane();
		centerStackPane.getChildren().add(myGrid.getObject());
		centerStackPane.getChildren().add(centerAnchorPane);
		borderPane.setCenter(centerStackPane);
	}

	@Override
	public void addGridViewObserver(GridViewObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAllGridViewObservers(Collection<GridViewObserver> gridViewObservers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGridViewObserver(GridViewObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAllGridViewObservers(Collection<GridViewObserver> gridViewObservers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ScrollPane getObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCellViewObserver(CellViewObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCellViewObserver(CellViewObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUnitViewObserver(UnitViewObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUnitViewObserver(UnitViewObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWorldViewObserver(WorldViewObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAllWorldViewObservers(Collection<WorldViewObserver> worldViewObservers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeWorldViewObserver(WorldViewObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAllWorldViewObservers(Collection<WorldViewObserver> worldViewObservers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GridViewExternal getGridViewExternal() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
