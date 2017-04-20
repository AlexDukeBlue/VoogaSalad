package frontend.worldview.grid;


import backend.grid.CoordinateTuple;
import controller.Controller;
import frontend.View;
import frontend.util.BaseUIManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * A Cell object is an immovable object on which Terrains and Units can be
 * placed.
 * <p>
 * The CellView is the UI representation of the backend Cell class. It allows
 * the user to interact with the Cell by dragging and dropping Units, Abilities,
 * or Terrains onto it, as well as selecting the items on it.
 * <p>
 * The CellView extends BaseUIManager so that it can change css stylesheets,
 * languages, and be updated by the controller.
 *
 * @author Dylan Peters
 */
public class CellView extends BaseUIManager<Parent> {
	private static final Paint CELL_OUTLINE = Color.BLACK;
	private static final double CELL_STROKE = 2;
	private static final double UNIT_SCALE = 0.75;

	private final CoordinateTuple cellLocation;
	private Polygon polygon;
	private final Group group;
	private final ContextMenu contextMenu;
	private final UnitViewDelegate delegate;
	private ArrayList<UnitView> unitList;

	/**
	 * Creates a new CellView instance. Sets all values to default.
	 *
	 * @param cellLocation The Cell object that this CellView will visually represent.
	 * @param controller   the controller object that this CellView will send information
	 *                     to when the user interacts with the CellView
	 */
	public CellView(CoordinateTuple cellLocation, Controller controller, UnitViewDelegate delegate) {
		super(controller);
		this.delegate = delegate;
		this.cellLocation = cellLocation;
		polygon = new Polygon();
		group = new Group();
		contextMenu = new ContextMenu();
		polygon.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> contextMenu.show(polygon, event.getScreenX(), event.getScreenY()));
		unitList =  new ArrayList<UnitView>();
		update();
	}

	//	/**
	//	 * Sets the action that is performed when a cell is clicked.
	//	 *
	//	 * @param consumer
	//	 *            consumer to execute when the cell is clicked
	//	 */
	//	public void setOnCellClick(Consumer<CellView> consumer) {
	//		polygon.setOnMouseClicked(event -> consumer.accept(this));
	//	}
	//
	//	/**
	//	 * Adds a copy of the Sprite to the cell and sends the request to the
	//	 * controller.
	//	 *
	//	 * @param sprite
	//	 *            sprite to copy and add to the cell
	//	 */
	//	public void add(VoogaEntity sprite) {
	//		CoordinateTuple location = cellModel.getLocation();
	//		Modifier<? extends GameplayState> toSend = game -> {
	//			game.getGrid().get(location).arrive(sprite.copy(), game);
	//			return game;
	//		};
	//		getController().sendModifier(toSend);
	//	}

	public double getX() {
		return polygon.getLayoutX();
	}

	public void setX(double x) {
		polygon.setLayoutX(x);
	}

	public double getY() {
		return polygon.getLayoutY();
	}

	public void setY(double y) {
		polygon.setLayoutY(y);
	}

	/**
	 * Returns an object that can be displayed to the user to show the Cell
	 */
	@Override
	public Parent getObject() {
		return group;
	}

	/**
	 * returns the polygon that serves as the shape of the cell
	 *
	 * @return polygon
	 */
	Polygon getPolygon() {
		return polygon;
	}

	/**
	 * sets the group to contain a different polygon
	 *
	 * @param polygon Shape of cellview an instance of a cell
	 */
	public void setPolygon(Polygon polygon) {
		group.getChildren().remove(polygon);
		this.polygon = polygon;
		update();
	}

	/**
	 * passes a cell's location to the controller for the backend to use, and
	 * determine its validity
	 */
	public void update() {
		this.getPolygon().setOnMouseEntered(e -> mouseIn());
		this.getPolygon().setOnMouseExited(e -> mouseOut());
		group.getChildren().clear();
		if (getController().getGrid().getImgPath().length() < 1) {
			polygon.setFill(new ImagePattern(View.getImg(getController().getGrid().get(cellLocation).getTerrain().getImgPath())));
		} else {
			polygon.setFill(Color.TRANSPARENT);
		}
		polygon.setStrokeWidth(CELL_STROKE);
		polygon.setStroke(CELL_OUTLINE);
		group.getChildren().add(polygon);
		
		
		
		
		
		//should be messing with the unitview not the polygon!!
		double xCenter = (polygon.getBoundsInParent().getMinX() + polygon.getBoundsInParent().getMaxX()) / 2.0;
		double yCenter = (polygon.getBoundsInParent().getMinY() + polygon.getBoundsInParent().getMaxY()) / 2.0;
		double size = polygon.getBoundsInParent().getHeight() * UNIT_SCALE;
		getController().getCell(cellLocation).getOccupants().forEach(unit -> {
			if (unit != null) {
				UnitView unitView = new UnitView(unit.getName(), unit.getLocation(), unit.getImgPath(), delegate);
				if (!unitList.contains(unitView)){
				unitList.add(unitView);
				}
				toolTip(unitView);
				unitView.getObject().setFitWidth(size);
				unitView.getObject().setFitHeight(size);
				unitView.getObject().setX(xCenter - unitView.getObject().getBoundsInParent().getWidth() / 2.0);
				unitView.getObject().setY(yCenter - unitView.getObject().getBoundsInParent().getHeight() / 2.0);
				group.getChildren().add(unitView.getObject());
				unitView.getObject().toFront();
			}
		});
		contextMenu.getItems().clear();
		getController().getCell(cellLocation).getOccupants().forEach(e -> contextMenu.getItems().add(new MenuItem(e.getName())));
	}

	/**
	 * Returns the coordinateTuple at which the CellView is displayed
	 *
	 * @return DisplayCoordinates at which the CellView is displayed.
	 */
	CoordinateTuple getCoordinateTuple() {
		return cellLocation;
	}

	/*
	 * creates a popup that gives information about the unit
	 */
	private void toolTip(UnitView uv){
		Tooltip tt = new Tooltip();
		tt.setText("Position: (" + polygon.getLayoutX() + "," + polygon.getLayoutY() + ")" 
				+ "\nName: " + uv.getUnitName());
		Tooltip.install(uv.getObject(), tt);
		System.out.println("toolTip");
		
	}
	
	//this isn't working properly
	private void mouseIn() {
		System.out.println(unitList.size());
		System.out.println("mousing over");
		if (unitList.size() > 1) {
			for (int i = 0; i < unitList.size(); i++) {
				System.out.println(i);
				unitList.get(i).getObject().setLayoutY(unitList.get(i).getObject().getLayoutY() - (i - 1) * 60);;
			}
		}
	}

	private void mouseOut() {
		if (unitList.size() > 1) {
			for (int i = 0; i < unitList.size(); i++) {
				unitList.get(i).getObject().setLayoutY(unitList.get(i).getObject().getLayoutY() + (i - 1) * 60);;
			}
		}
	}
	
//	private void mouseInPane(){
////		Rectangle rect = new Rectangle();
////		rect.setWidth(this.getObject().getLayoutBounds().getWidth());
////		rect.setHeight(unitList.size() * 60);
////		rect.setX(this.getX());
////		rect.setY(this.getY() - rect.getHeight() + polygon.getLayoutBounds().getHeight());
//		VBox vbox = new VBox();
//		for (UnitView each : unitList){
//			vbox.getChildren().addAll(each.getObject());
//		}
//		
//		ScrollPane sp = new ScrollPane();
//		
//		for (int i = 0; i < unitList.size(); i++) {
//			unitList.get(i).getObject().setLayoutY(unitList.get(i).getObject().getLayoutY() + (i - 1) * 60);;
//		}
//	}

}
