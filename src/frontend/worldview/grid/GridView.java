/**
 * Holds a grid to be displayed in the development and player GUI. It can have Sprites added to a particular cell or 
 * have all cells updated after something occurs in the game.
 */
package frontend.worldview.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

/**
 * Andreas
 * @author Stone Mathers
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
		backendCells.values().stream().forEach(cell -> {
			CellView cl = new CellView(cell,getController());
			myLayoutManager.layoutCell(cl, SCALE, MIN, MAX);
			cellViews.add(cl);
			cellViewObjects.getChildren().add(cl.getObject());
			getController().addToUpdated(cl);
		});
		
		myScrollPane.setContent(cellViewObjects);
	}
	
	public void setOnCellClick(Consumer<CellView> consumer){
		cellViews.stream().forEach(cellView -> cellView.setOnCellClick(consumer));
	}

	@Override
	public Region getObject() {
		return myScrollPane;
	}
}
