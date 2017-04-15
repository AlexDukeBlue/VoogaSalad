package frontend.worldview.grid;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Holds a grid to be displayed in the development and player GUI inside a ScrollPane. 
 * It can have Sprites added to a particular cell or have all cells updated after something occurs in the game.
 * Contains a group of CellView objects and a collection of CellView's, when update method is called, it can tell
 * all of the units/cells to update themselves
 * 
 * @author Andreas Santos
 * Created 3/29/2017
 */
public class GridView extends BaseUIManager<Region> {
	private static final double MIN = 10, MAX = 100, SCALE = 0.750;

	private ScrollPane myScrollPane;
	private Group cellViewObjects;
	private LayoutManager myLayoutManager;
	private Collection<CellView> cellViews;
	
	public GridView(Controller controller){
		setController(controller);
		initialize();
		update();
	}

	private void initialize() {
		myScrollPane = new ScrollPane();
		cellViewObjects = new Group();
		cellViews = new ArrayList<CellView>();
	}
	
	/**
	 * instructs CellViews to update themselves
	 */
	public void update(){
		cellViewObjects.getChildren().clear();

		getController();
		getController().getGrid();
		getController().getGrid().dimension();
		if (getController().getGrid().dimension() == 2){
			myLayoutManager = new SquareLayout();
		} else {
			myLayoutManager = new HexagonalManager();
		}
		
		Map<CoordinateTuple, Cell> backendCells = getController().getGrid().getCells();
		backendCells.values().forEach(cell -> {
			CellView cl = new CellView(cell,getController());
			myLayoutManager.layoutCell(cl, SCALE, MIN, MAX);
			cellViews.add(cl);
			cellViewObjects.getChildren().add(cl.getObject());
			getController().addToUpdated(cl);
		});
		
		myScrollPane.setContent(cellViewObjects);
	}
	
	/**
	 * set on clicked method for each cell
	 * @param consumer
	 */
	public void setOnCellClick(Consumer<CellView> consumer){
		cellViews.forEach(cellView -> cellView.setOnCellClick(consumer));
	}

	@Override
	public Region getObject() {
		return myScrollPane;
	}
}
