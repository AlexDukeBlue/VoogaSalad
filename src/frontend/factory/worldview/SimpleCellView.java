package frontend.factory.worldview;

import backend.grid.CoordinateTuple;
import controller.Controller;
import frontend.View;
import frontend.factory.worldview.layout.CellViewLayoutInterface;
import frontend.interfaces.worldview.CellViewExternal;
import frontend.interfaces.worldview.CellViewObserver;
import frontend.interfaces.worldview.UnitViewObserver;
import frontend.util.BaseUIManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Collection;

class SimpleCellView extends BaseUIManager<Node> implements CellViewLayoutInterface, CellViewExternal {

	private Collection<CellViewObserver> observers;
	private Collection<UnitViewObserver> unitViewObservers;

	private static final Paint CELL_OUTLINE = Color.BLACK;
	private static final double CELL_STROKE = 2;
	private static final double UNIT_SCALE = 0.75;

	private CoordinateTuple cellLocation;
	private Polygon polygon;
	private Group group;
	private ContextMenu contextMenu;
	private ArrayList<SimpleUnitView> unitList = new ArrayList<>();

	/**
	 * Creates a new CellView instance. Sets all values to default.
	 *
	 * @param cellLocation
	 *            The Cell object that this CellView will visually represent.
	 * @param controller
	 *            the controller object that this CellView will send information
	 *            to when the user interacts with the CellView
	 */
	public SimpleCellView(CoordinateTuple cellLocation, Controller controller) {
		super(controller);
		initialize(cellLocation);
	}

	@Override
	public double getX() {
		return polygon.getLayoutX();
	}

	@Override
	public void setX(double x) {
		polygon.setLayoutX(x);
	}

	@Override
	public double getY() {
		return polygon.getLayoutY();
	}

	@Override
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
	@Override
	public Polygon getPolygon() {
		return polygon;
	}

	/**
	 * sets the group to contain a different polygon
	 *
	 * @param polygon
	 *            Shape of cellview an instance of a cell
	 */
	@Override
	public void setPolygon(Polygon polygon) {
		group.getChildren().remove(polygon);
		this.polygon = polygon;
		update();
	}

	/**
	 * passes a cell's location to the controller for the backend to use, and
	 * determine its validity
	 */
	@Override
	public void update() {
		this.getPolygon().setOnMouseEntered(e -> mouseOver());
		group.getChildren().clear();
		if (getController().getGrid().getImgPath().length() < 1) {
			polygon.setFill(new ImagePattern(
					View.getImg(getController().getGrid().get(cellLocation).getTerrain().getImgPath())));
		} else {
			polygon.setFill(Color.TRANSPARENT);
		}

		polygon.setOnMouseClicked(event -> {
			if (event.getButton().equals(MouseButton.PRIMARY))
				observers.stream().forEach(observer -> observer.didClickCellViewExternalInterface(this));

		});

		polygon.setStrokeWidth(CELL_STROKE);
		polygon.setStroke(CELL_OUTLINE);
		group.getChildren().add(polygon);
		double xCenter = (polygon.getBoundsInParent().getMinX() + polygon.getBoundsInParent().getMaxX()) / 2.0;
		double yCenter = (polygon.getBoundsInParent().getMinY() + polygon.getBoundsInParent().getMaxY()) / 2.0;
		double size = polygon.getBoundsInParent().getHeight() * UNIT_SCALE;
		getController().getCell(cellLocation).getOccupants().forEach(unit -> {
			if (unit != null) {
				SimpleUnitView unitView = new SimpleUnitView(unit.getName(), unit.getLocation(), unit.getImgPath());
				unitList.add(unitView);
				unitView.setFitWidth(size);
				unitView.setFitHeight(size);
				unitView.setX(xCenter - unitView.getObject().getBoundsInParent().getWidth() / 2.0);
				unitView.setY(yCenter - unitView.getObject().getBoundsInParent().getHeight() / 2.0);
				group.getChildren().add(unitView.getObject());
				unitView.getObject().toFront();
				unitView.addAllUnitViewObservers(unitViewObservers);
			}
		});
		contextMenu.getItems().clear();
		getController().getCell(cellLocation).getOccupants()
				.forEach(e -> contextMenu.getItems().add(new MenuItem(e.getName())));
	}


	private void mouseOver() {
		// System.out.println(unitList.size());
		// System.out.println("mousing over");
		if (unitList.size() != 0) {
			for (int i = 0; i < unitList.size(); i++) {
				// unitList.get(i).getObject().setLayoutY(unitList.get(i).getObject().getLayoutY()
				// - i * 30);;
			}
		}
	}

	private void mouseOut() {
		if (unitList.size() != 0) {
			for (int i = 0; i < unitList.size(); i++) {
				unitList.get(i).getObject().setTranslateY(i * 10);
			}
		}
	}

	private void pushUp() {

	}

	@Override
	public void addCellViewObserver(CellViewObserver observer) {
		if (!observers.contains(observer) && observer != null) {
			observers.add(observer);
		}
	}

	@Override
	public void removeCellViewObserver(CellViewObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	private void initialize(CoordinateTuple cellLocation) {
		observers = new ArrayList<>();
		unitViewObservers = new ArrayList<>();
		this.cellLocation = cellLocation;
		polygon = new Polygon();
		group = new Group();
		contextMenu = new ContextMenu();
		polygon.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
				event -> contextMenu.show(polygon, event.getScreenX(), event.getScreenY()));
		update();
	}

	@Override
	public void addUnitViewObserver(UnitViewObserver observer) {
		if (!unitViewObservers.contains(observer) && observer != null) {
			unitViewObservers.add(observer);
		}
	}

	@Override
	public void removeUnitViewObserver(UnitViewObserver observer) {
		if (unitViewObservers.contains(observer)) {
			unitViewObservers.remove(observer);
		}
	}

	@Override
	public void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		if (unitViewObservers != null) {
			unitViewObservers.stream().forEach(observer -> addUnitViewObserver(observer));
		}
	}

	@Override
	public void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		if (unitViewObservers != null) {
			unitViewObservers.stream().forEach(observer -> removeUnitViewObserver(observer));
		}
	}

	@Override
	public void addAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		if (cellViewObservers != null) {
			cellViewObservers.stream().forEach(observer -> addCellViewObserver(observer));
		}
	}

	@Override
	public void removeAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		if (cellViewObservers != null) {
			cellViewObservers.stream().forEach(observer -> removeCellViewObserver(observer));
		}
	}

	@Override
	public CoordinateTuple getLocation() {
		return cellLocation;
	}

}
