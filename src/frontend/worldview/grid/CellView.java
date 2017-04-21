package frontend.worldview.grid;


import backend.grid.CoordinateTuple;
import controller.Controller;
import frontend.View;
import frontend.factory.worldview.SimpleUnitView;
import frontend.factory.worldview.layout.CellViewLayoutInterface;
import frontend.interfaces.worldview.CellViewExternal;
import frontend.interfaces.worldview.CellViewObserver;
import frontend.interfaces.worldview.UnitViewExternal;
import frontend.interfaces.worldview.UnitViewObserver;
import frontend.util.BaseUIManager;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.IntStream;

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
public class CellView extends BaseUIManager<Parent> implements CellViewLayoutInterface, CellViewExternal {
	private Collection<CellViewObserver> observers;
	private Collection<UnitViewObserver> unitViewObservers;

	private static final Paint CELL_OUTLINE = Color.BLACK;
	private static final double CELL_STROKE = 2;
	private static final double UNIT_SCALE = 0.75;

	private final CoordinateTuple cellLocation;
	private Polygon polygon;
	private String polygonFillImgPath;
	private final Group group;
	private final ContextMenu contextMenu;
	private ArrayList<UnitViewExternal> unitList;

	/**
	 * Creates a new CellView instance. Sets all values to default.
	 *
	 * @param cellLocation The Cell object that this CellView will visually represent.
	 * @param controller   the controller object that this CellView will send information
	 *                     to when the user interacts with the CellView
	 */
	public CellView(CoordinateTuple cellLocation, Controller controller) {
		super(controller);
		this.cellLocation = cellLocation;
		unitList = new ArrayList<>();
		unitViewObservers = new ArrayList<>();
		observers = new ArrayList<>();
		contextMenu = new ContextMenu();
		setPolygon(new Polygon());
		group = new Group(polygon);
		update();
	}

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

	@Override
	public Polygon getPolygon() {
		return polygon;
	}

	@Override
	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
		polygon.setStrokeWidth(CELL_STROKE);
		polygon.setStroke(CELL_OUTLINE);
		polygon.setOnMouseEntered(e -> mouseIn());
		polygon.setOnMouseExited(e -> mouseOut(e.getX()));
		polygon.setOnMouseClicked(event -> {
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				observers.stream().filter(Objects::nonNull).forEach(observer -> observer.didClickCellViewExternalInterface(this));
			}
		});
		polygon.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> contextMenu.show(polygon, event.getScreenX(), event.getScreenY()));
	}

	/**
	 * Returns an object that can be displayed to the user to show the Cell
	 */
	@Override
	public Parent getObject() {
		return group;
	}

	/**
	 * passes a cell's location to the controller for the backend to use, and
	 * determine its validity
	 */
	public void update() {
		String imgPath = getController().getGrid().getImgPath();
		if (imgPath.length() < 1) {
			if (!imgPath.equals(polygonFillImgPath)) {
				polygon.setFill(new ImagePattern(View.getImg(getController().getGrid().get(cellLocation).getTerrain().getImgPath())));
				polygonFillImgPath = imgPath;
			}
		} else {
			polygon.setFill(Color.TRANSPARENT);
		}
		//should be messing with the unitview not the polygon!!
		double xCenter = (polygon.getBoundsInParent().getMinX() + polygon.getBoundsInParent().getMaxX()) / 2.0;
		double yCenter = (polygon.getBoundsInParent().getMinY() + polygon.getBoundsInParent().getMaxY()) / 2.0;
		double size = polygon.getBoundsInParent().getHeight() * UNIT_SCALE;
		unitList.clear();
		group.getChildren().clear();
		group.getChildren().addAll(polygon);
		getController().getCell(cellLocation).getOccupants().forEach(unit -> {
			if (unit != null) {
				UnitViewExternal unitView = new SimpleUnitView(unit.getName(), unit.getLocation(), unit.getImgPath());
				unitList.add(unitView);
				toolTip(unitView);
				unitView.getObject().setFitWidth(size);
				unitView.getObject().setFitHeight(size);
				unitView.getObject().setX(xCenter - unitView.getObject().getBoundsInParent().getWidth() / 2.0);
				unitView.getObject().setY(yCenter - unitView.getObject().getBoundsInParent().getHeight() / 2.0);
				group.getChildren().add(unitView.getObject());
				unitView.getObject().toFront();
				unitView.addAllUnitViewObservers(unitViewObservers);
			}
		});
		contextMenu.getItems().clear();
		getController().getCell(cellLocation).getOccupants().forEach(e -> contextMenu.getItems().add(new MenuItem(e.getName())));
	}

	/*
	 * creates a popup that gives information about the unit
	 */
	private void toolTip(UnitViewExternal uv) {
		Tooltip tt = new Tooltip();
		tt.setText("Position: (" + polygon.getLayoutX() + "," + polygon.getLayoutY() + ")"
				+ "\nName: " + uv.getUnitName());
		Tooltip.install(uv.getObject(), tt);
//		System.out.println("toolTip");
	}

	//this isn't working properly
	private void mouseIn() {
		if (unitList.size() > 1) {
			for (int i = 0; i < unitList.size(); i++) {
				unitList.get(i).getObject().setLayoutY(unitList.get(i).getObject().getLayoutY() - i * 60);
			}
		}
	}

	private void mouseOut(double d) {
		IntStream.range(0, unitList.size()).parallel().forEach(i -> unitList.get(i).getObject().setLayoutY(unitList.get(i).getObject().getLayoutY() + i * 60));
	}

	@Override
	public void addUnitViewObserver(UnitViewObserver observer) {
		unitViewObservers.add(observer);
	}

	@Override
	public void removeUnitViewObserver(UnitViewObserver observer) {
		unitViewObservers.remove(observer);
	}

	@Override
	public void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		this.unitViewObservers.addAll(unitViewObservers);
	}

	@Override
	public void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		this.unitViewObservers.removeAll(unitViewObservers);
	}

	@Override
	public void addCellViewObserver(CellViewObserver observer) {
		observers.add(observer);
	}

	@Override
	public void addAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		observers.addAll(cellViewObservers);
	}

	@Override
	public void removeCellViewObserver(CellViewObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void removeAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		observers.removeAll(cellViewObservers);
	}

	@Override
	public CoordinateTuple getLocation() {
		return cellLocation;
	}
}
