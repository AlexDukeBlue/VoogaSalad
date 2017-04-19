package frontend.worldview.grid;

import java.util.ArrayList;
import java.util.Collection;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
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
	private boolean shouldCopy = true;

	public GridView(Controller controller) {
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

	@Override
	public Region getObject() {
		return myScrollPane;
	}

	private void populateCellViews() {
		cellViewObjects.setBackground(new Background(
				new BackgroundFill(new ImagePattern(View.getImg(getController().getGrid().getImgPath())), null, null)));
		getController().getGrid().getCells().values().forEach(cell -> {
			CellView cl = new CellView(cell, getController(), this);
			myLayoutManager.layoutCell(cl, SCALE, MIN, MAX);
			cl.update();
			cellViews.add(cl);
			cellViewObjects.getChildren().add(cl.getObject());

			cl.getPolygon().setOnMouseClicked(event -> cellClicked(cl));
		});
	}

	public void setTemplateEntityToAdd(VoogaEntity template) {
		if (template instanceof Unit) {
			unitToArrive = (Unit)template;
			shouldCopy = true;
		}
	}

	private void cellClicked(CellView cell) {
		if (unitToArrive != null) {
			CoordinateTuple tuple = cell.getCoordinateTuple();
			Unit unitToArrive;
			if (shouldCopy){
				unitToArrive = this.unitToArrive.copy();
			} else {
				unitToArrive = this.unitToArrive;
			}
			boolean shouldCopy = this.shouldCopy;
			Modifier<? extends AuthoringGameState> modifier = gameState -> {
				if (shouldCopy){
					gameState.getGrid().get(tuple).addOccupants(unitToArrive);
					((ModifiableUnit)unitToArrive).setCurrentCell(gameState.getGrid().get(tuple));
				} else {
					Cell previous = unitToArrive.getCurrentCell();
					unitToArrive.moveTo(gameState.getGrid().get(tuple), gameState);
					Cell current = unitToArrive.getCurrentCell();
					System.out.println("Previous: " + previous + "\n" + previous.getOccupants().size());
					System.out.println("Current: " + current + "\n" + current.getOccupants().size());
				}
				return gameState;
			};
			getController().sendModifier(modifier);
			System.out.println(getController().getGrid());
		}
	}

	@Override
	public void unitClicked(UnitView unitView) {
		unitToArrive = unitView.getUnit();
		shouldCopy = false;
	}
	
}
