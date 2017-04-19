package frontend.worldview.grid;

import java.util.ArrayList;
import java.util.Collection;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.View;
import frontend.util.BaseUIManager;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import util.net.Modifier;

public class GridView extends BaseUIManager<Region> implements UnitViewDelegate {
	private static final double MIN = 10, MAX = 100, SCALE = 0.750;

	private ScrollPane myScrollPane;
	private Pane cellViewObjects;
	private LayoutManager myLayoutManager;
	private Collection<CellView> cellViews;
	private Unit unitToArrive;
	
	public GridView(Controller controller){
		setController(controller);
		initialize();
		update();
	}

	private void initialize() {
		myScrollPane = new ScrollPane();
		cellViewObjects = new Pane();
		cellViews = new ArrayList<>();
		myLayoutManager = new LayoutManagerFactory();
		populateCellViews();
		myScrollPane.setContent(cellViewObjects);
	}
	
//	/**
//	 * set on clicked method for each cell
//	 * @param consumer
//	 */
//	public void setOnCellClick(Consumer<CellView> consumer){
//		cellViews.forEach(cellView -> cellView.setOnCellClick(consumer));
//	}

	@Override
	public Region getObject() {
		return myScrollPane;
	}

	private void populateCellViews() {
		cellViewObjects.setBackground(new Background(new BackgroundFill(new ImagePattern(View.getImg(getController().getGrid().getImgPath())), null, null)));
		getController().getGrid().getCells().values().forEach(cell -> {
			CellView cl = new CellView(cell, getController());
			myLayoutManager.layoutCell(cl, SCALE, MIN, MAX);
			cl.update();
			cellViews.add(cl);
			cellViewObjects.getChildren().add(cl.getObject());
			
			cl.getPolygon().setOnMouseClicked(event -> cellClicked(cl));
		});
	}
	
	public void setTemplateEntityToAdd(VoogaEntity template){
		if (template instanceof Unit){
			unitToArrive = (Unit)template.copy();
		}
	}
	
	public void setUnitToMove(Unit unit){
		unitToArrive = unit;
	}
	
	
	private void cellClicked(CellView cell){
		if (unitToArrive != null){
			CoordinateTuple tuple = cell.getCoordinateTuple();
			Unit unitToArrive = this.unitToArrive;
			Modifier<? extends AuthoringGameState> modifier = gameState -> {
				gameState.getGrid().get(tuple).arrive(unitToArrive, gameState);
				return gameState;
			};
			getController().sendModifier(modifier);
		}
	}

	@Override
	public void dragBegan(UnitView unitView) {
		// TODO Auto-generated method stub
	}
}
