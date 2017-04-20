package frontend.worldview.grid;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.GameplayState;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.View;
import frontend.util.BaseUIManager;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import sun.security.krb5.internal.ktab.KeyTabInputStream;

/**
 * Holds a grid to be displayed in the development and player GUI inside a
 * ScrollPane. It can have Sprites added to a particular cell or have all cells
 * updated after something occurs in the game. Contains a group of CellView
 * objects and a collection of CellView's, when update method is called, it can
 * tell all of the units/cells to update themselves
 *
 * @author Andreas Santos Created 3/29/2017
 */
public class GridView extends BaseUIManager<ScrollPane> implements UnitViewDelegate {
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
		cellViewObjects = new Pane();
		Group zoomGroup = new Group(cellViewObjects);
		myScrollPane.setOnZoom(event -> {
			cellViewObjects.setScaleX(cellViewObjects.getScaleX() * event.getZoomFactor());
			cellViewObjects.setScaleY(cellViewObjects.getScaleY() * event.getZoomFactor());
		});
		
		 myScrollPane.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
		        @Override
		        public void handle(ScrollEvent event) {
		            if (event.isControlDown()) {
		            	cellViewObjects.setScaleX(cellViewObjects.getScaleX() + event.getDeltaY()/500);
						cellViewObjects.setScaleY(cellViewObjects.getScaleY() + event.getDeltaY()/500);
		                event.consume();
		            }
		        }
		    });
		
		myScrollPane.setOnScroll(event -> {
			if(event.isControlDown()){
				
			}
		});
		myLayoutManager = new LayoutManagerFactory();
		populateCellViews();
		myScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		myScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		myScrollPane.setPannable(true);
		myScrollPane.setContent(zoomGroup);
	}

	@Override
	public ScrollPane getObject() {
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
				if (event.getButton().equals(MouseButton.PRIMARY)) cellClicked(coordinate);
			});
		});
	}

	public void setTemplateEntityToAdd(VoogaEntity template) {
		unitClickedName = template.getName();
		unitClickedLocation = null;
		shouldCopy = true;
	}

	private void cellClicked(CoordinateTuple cellClickedLocation) {
		if (unitClickedName != null) {
			CoordinateTuple unitClickedLocation = this.unitClickedLocation;
			String unitClickedName = this.unitClickedName;
			if (shouldCopy) {
				System.out.println("local 93: " + getController().getAuthoringGameState().getTemplateByName(unitClickedName));
				getController().sendModifier((AuthoringGameState gameState) -> {
					VoogaEntity entity = gameState.getTemplateByName(unitClickedName).copy();
					gameState.getGrid().get(cellClickedLocation).addVoogaEntity(entity);
					return gameState;
				});
			} else {
				System.out.println("101: " + getController().getGrid().get(unitClickedLocation).getOccupants());
				getController().sendModifier((GameplayState gameState) -> {
					Unit unitToMove = gameState.getGrid().get(unitClickedLocation).getOccupantByName(unitClickedName);
					System.out.println("104: " + gameState.getGrid().get(unitClickedLocation).getOccupants());
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