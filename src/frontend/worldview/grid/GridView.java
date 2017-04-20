package frontend.worldview.grid;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.View;
import frontend.util.BaseUIManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;

/**
 * Holds a grid to be displayed in the development and player GUI inside a
 * ScrollPane. It can have Sprites added to a particular cell or have all cells
 * updated after something occurs in the game. Contains a group of CellView
 * objects and a collection of CellView's, when update method is called, it can
 * tell all of the units/cells to update themselves
 *
 * @author Andreas Santos Created 3/29/2017
 */
public class GridView extends BaseUIManager<Region> implements UnitViewDelegate {
	private static final double MIN = 10, MAX = 100, SCALE = 0.750;
	private ScrollPane myScrollPane;
	private Pane cellViewObjects;
	private LayoutManager myLayoutManager;
	private String unitClickedName;
	private CoordinateTuple unitClickedLocation;
	private boolean shouldCopy = true;

	public GridView(Controller controller) {
		setController(controller);
		initialize();
		update();
	}

	@Override
	public void update() {

	}

	private void initialize() {
		myScrollPane = new ScrollPane();
		myScrollPane.setOnZoom(event -> {
			Node e = myScrollPane.getContent();
			if (e.getScaleX() < 1.5) {
				e.setScaleX(e.getScaleX() * event.getZoomFactor());
				e.setScaleY(e.getScaleY() * event.getZoomFactor());
			} else {
				e.setScaleX(1.1);
				e.setScaleY(1.1);
			}
		});
		cellViewObjects = new Pane();
		Group zoomGroup = new Group(cellViewObjects);
		myScrollPane.setOnZoom(event -> {
			cellViewObjects.setScaleX(cellViewObjects.getScaleX() * event.getZoomFactor());
			cellViewObjects.setScaleY(cellViewObjects.getScaleY() * event.getZoomFactor());
		});
		myLayoutManager = new LayoutManagerFactory();
		populateCellViews();
		myScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		myScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		myScrollPane.setPannable(true);
		myScrollPane.setContent(zoomGroup);
	}

	@Override
	public Region getObject() {
		return myScrollPane;
	}

	private void populateCellViews() {
		cellViewObjects.setBackground(new Background(new BackgroundFill(new ImagePattern(View.getImg(getController().getGrid().getImgPath())), null, null)));
		getController().getGrid().getCells().keySet().forEach(coordinate -> {
			CellView cl = new CellView(coordinate, getController(), this);
			myLayoutManager.layoutCell(cl, SCALE, MIN, MAX);
			cl.update();
			cellViewObjects.getChildren().add(cl.getObject());
			cl.getPolygon().setOnMouseClicked(event -> {
				if (event.getButton().equals(MouseButton.PRIMARY)) cellClicked(cl);
			});
		});
	}

	public void setTemplateEntityToAdd(VoogaEntity template) {
		if (template instanceof Unit) {
			unitClickedName = template.getName();
			unitClickedLocation = null;
			shouldCopy = true;
		}
	}

	private void cellClicked(CellView cell) {
		if (unitClickedName != null) {
			CoordinateTuple cellClickedLocation = cell.getCoordinateTuple();
			CoordinateTuple unitClickedLocation = this.unitClickedLocation;
			String unitClickedName = this.unitClickedName;
			//YOU HAVE TO GET THE SERVER'S UNIT, NOT THE LOCAL UNIT
			//unitToArrive is the client's version
			//unitToMove is the server's version. You have to get it from the gameState
			//If you add unitToArrive to the server's grid, suddenly the server has 2 units
			//unitToMove != unitToArrive
			//you can't move unitToArrive on the server, since that unit doesn't exist on the server
			//however, a unit with the exact same name and location do exist on the server
			//so you can get the server's version using the name and location
			//note that you can't have 2 units with the same name on the same spot (this is hard enforced in the backend with a map)
			//Dylan I figured it out man
			//You can die in peace now
			if (shouldCopy) {
				getController().sendModifier((AuthoringGameState gameState) -> {
					Unit newUnit = gameState.getTemplateByCategory("unit").getByName(unitClickedName).copy();
					gameState.getGrid().get(cellClickedLocation).addOccupants(newUnit);
					return gameState;
				});
			} else {
				getController().sendModifier((GameplayState gameState) -> {
					Unit unitToMove = gameState.getGrid().get(unitClickedLocation).getOccupantByName(unitClickedName);
					unitToMove.moveTo(gameState.getGrid().get(cellClickedLocation), gameState);
					return gameState;
				});
			}
			this.shouldCopy = false;
			this.unitClickedName = null;
			this.unitClickedLocation = null;
		}
	}

	@Override
	public void unitClicked(UnitView unitView) {
		unitClickedName = unitView.getUnitName();
		unitClickedLocation = unitView.getUnitLocation();
	}
}