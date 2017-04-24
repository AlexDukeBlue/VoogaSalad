package frontend.factory.worldview;

import backend.cell.Cell;
import backend.cell.Terrain;
import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.unit.properties.UnitStat;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.View;
import frontend.factory.worldview.layout.CellViewLayoutInterface;
import frontend.interfaces.worldview.CellViewExternal;
import frontend.interfaces.worldview.UnitViewExternal;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class SimpleCellView extends ClickableUIComponent<Group>
		implements CellViewLayoutInterface, CellViewExternal, PolyglotDelegate {

	private static final Paint CELL_OUTLINE = Color.BLACK;
	private static final double CELL_STROKE = 2;
	private static final double UNIT_SCALE = 0.75;
	private CoordinateTuple cellLocation;
	private Polygon polygon;
	private Group group;
	private ContextMenu contextMenu;
	private ArrayList<SimpleUnitView> unitViews = new ArrayList<>();

	private Terrain terrainCache;
	private Set<Unit> unitCache;
	private PolyglotDelegate delegate;

	/**
	 * Creates a new CellView instance. Sets all values to default.
	 *
	 * @param cellLocation
	 *            The Cell object that this CellView will visually represent.
	 * @param controller
	 *            the controller object that this CellView will send information
	 * @param clickHandler
	 */
	public SimpleCellView(CoordinateTuple cellLocation, Controller controller, ClickHandler clickHandler,
			PolyglotDelegate delegate) {
		super(controller, clickHandler);
		initialize(cellLocation);
		this.delegate = delegate;
		getPolyglot().addLanguageChangeHandler(change -> {
			setContextMenu();
			installToolTips();
		});
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
	public Group getObject() {
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
		polygon.setOnMouseClicked(event -> handleClick(null));
		update();
	}

	/**
	 * passes a cell's location to the controller for the backend to use, and
	 * determine its validity
	 */
	@Override
	public void update() {
		this.getPolygon().setOnMouseEntered(e -> mouseOver());
		if (getController().getGrid().getImgPath().length() < 1) {
			if (terrainCache == null || !terrainCache.getImgPath().equals(getCell().getTerrain().getImgPath())) {
				polygon.setFill(new ImagePattern(View.getImg(getCell().getTerrain().getImgPath())));
			}
		} else {
			if (!polygon.getFill().equals(Color.TRANSPARENT)) {
				polygon.setFill(Color.TRANSPARENT);
			}
		}
		if (polygon.getStrokeWidth() != CELL_STROKE) {
			polygon.setStrokeWidth(CELL_STROKE);
		}
		if (polygon.getStroke() == null || !polygon.getStroke().equals(CELL_OUTLINE)) {
			polygon.setStroke(CELL_OUTLINE);
		}
		if (!group.getChildren().contains(polygon)) {
			group.getChildren().add(polygon);
		}
		if (!(getCell().getOccupants().containsAll(unitCache) && unitCache.containsAll(getCell().getOccupants()))) {
			unitCache.clear();
			unitCache.addAll(getCell().getOccupants());
			unitViews.forEach(e -> getController().removeFromUpdated(e));
			unitViews.clear();
			double xCenter = (polygon.getBoundsInParent().getMinX() + polygon.getBoundsInParent().getMaxX()) / 2.0;
			double yCenter = (polygon.getBoundsInParent().getMinY() + polygon.getBoundsInParent().getMaxY()) / 2.0;
			double size = polygon.getBoundsInParent().getHeight() * UNIT_SCALE;
			group.getChildren().clear();
			group.getChildren().addAll(polygon);
			getCell().getOccupants().forEach(unit -> {
				SimpleUnitView unitView = new SimpleUnitView(unit.getName(), unit.getLocation(), getController(),
						getClickHandler(), this);
				unitViews.add(unitView);
				unitView.setSize(size);
				group.getChildren().add(unitView.getObject());
				unitView.getObject().toFront();
				unitView.getObject().relocate(xCenter - unitView.getObject().getWidth() / 2.0,
						yCenter - unitView.getObject().getHeight() / 2.0);
			});
			setContextMenu();
			polygon.toBack();
		}
		installToolTips();
	}

	private void installToolTips() {
		unitViews.forEach(uv -> Tooltip.install(uv.getObject(), new Tooltip(getToolTipString(uv))));
	}

	private void setContextMenu() {
		contextMenu.getItems().clear();
		getCell().getOccupants().forEach(e -> {
			MenuItem item = new MenuItem(getPolyglot().get("Select").getValueSafe() + " " + e.getName());
			// getPolyglot().get("Select").addListener(change -> {
			// item.setText(getPolyglot().get("Select").getValueSafe() + " "
			// + e.getName());
			// });
			contextMenu.getItems().add(item);
			item.addEventHandler(ActionEvent.ACTION,
					event -> unitViews.stream().filter(p -> p.getUnitName().equals(item.getText().substring(7)))
							.forEach(f -> f.handleClick(null)));
		});
	}

	private String getToolTipString(UnitViewExternal uv) {
		String hp = "";
		UnitStat<Double> hitpoints = uv.getUnit().getHitPoints();
		if (Objects.nonNull(hitpoints)) {
			String formatString = "\n" + getPolyglot().get("Hitpoints").getValueSafe() + ": %2.0f/%2.0f";
			hp = String.format(formatString, hitpoints.getCurrentValue(), hitpoints.getMaxValue());
		}
		String formatString = getPolyglot().get("Name").getValueSafe() + ": %s\n"
				+ getPolyglot().get("Position").getValueSafe() + ": %s%s";
		return String.format(formatString, uv.getUnitName(), uv.getUnit().getLocation().toString(), hp);
	}

	private void mouseOver() {
		// System.out.println(unitViews.size());
		// System.out.println("mousing over");
		if (unitViews.size() != 0) {
			for (int i = 0; i < unitViews.size(); i++) {
				// unitViews.get(i).getObject().setLayoutY(unitViews.get(i).getObject().getLayoutY()
				// - i * 30);;
			}
		}
	}

	private void mouseOut() {
		if (unitViews.size() != 0) {
			for (int i = 0; i < unitViews.size(); i++) {
				unitViews.get(i).getObject().setTranslateY(i * 10);
			}
		}
	}

	private void pushUp() {

	}

	private void initialize(CoordinateTuple cellLocation) {
		this.cellLocation = cellLocation;
		unitCache = new HashSet<>();
		polygon = new Polygon();
		group = new Group();
		contextMenu = new ContextMenu();
		polygon.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
				event -> contextMenu.show(polygon, event.getScreenX(), event.getScreenY()));
		polygon.setOnMouseClicked(event -> handleClick(null));
		update();
	}

	@Override
	public CoordinateTuple getLocation() {
		return cellLocation;
	}

	@Override
	public Cell getCell() {
		return getController().getCell(cellLocation);
	}

	@Override
	public void setClickHandler(ClickHandler clickHandler) {
		super.setClickHandler(clickHandler);
		unitViews.forEach(e -> setClickHandler(clickHandler));
	}

	@Override
	public String toString() {
		return getCell().toString();
	}
}
