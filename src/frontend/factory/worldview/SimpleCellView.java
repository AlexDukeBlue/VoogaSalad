package frontend.factory.worldview;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
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
import java.util.List;
import java.util.Objects;

class SimpleCellView extends ClickableUIComponent<Group> implements CellViewLayoutInterface, CellViewExternal {
	private static final Paint CELL_OUTLINE = Color.BLACK;
	private static final double CELL_STROKE = 2;
	private static final double UNIT_SCALE = 0.75;
	private final CoordinateTuple cellLocation;
	private Polygon polygon;
	private final Group group;
	private final ContextMenu contextMenu;
	private final List<SimpleUnitView> unitViews;

	private String terrainCache;
	private int unitCount;

	/**
	 * Creates a new CellView instance. Sets all values to default.
	 *
	 * @param cellLocation The Cell object that this CellView will visually represent.
	 * @param controller   the controller object that this CellView will send information
	 * @param clickHandler
	 */
	public SimpleCellView(CoordinateTuple cellLocation, Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		this.unitViews = new ArrayList<>();
		this.polygon = new Polygon();
		this.cellLocation = cellLocation;
		contextMenu = new ContextMenu();
		group = new Group();
		setPolygon(new Polygon());
		getPolyglot().addLanguageChangeHandler(change -> {
			setContextMenu();
			installToolTips();
		});
		update();
		this.unitCount = -2;
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
	 * @param polygon Shape of cellview an instance of a cell
	 */
	@Override
	public void setPolygon(Polygon polygon) {
		group.getChildren().remove(polygon);
		this.polygon = polygon;
		polygon.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> contextMenu.show(polygon, event.getScreenX(), event.getScreenY()));
		polygon.setOnMouseEntered(event -> mouseOver());
		polygon.setOnMouseClicked(event -> handleClick(null));
		polygon.setStroke(CELL_OUTLINE);
		update();
	}

	/**
	 * passes a cell's location to the controller for the backend to use, and
	 * determine its validity
	 */
	@Override
	public void update() {
		if (Objects.nonNull(getCell()) && (unitCount < 0 || !getCell().getTerrain().getImgPath().equals(terrainCache))) {
			if (getController().getGrid().getImgPath().length() < 1) {
				polygon.setFill(new ImagePattern(View.getImg(getCell().getTerrain().getImgPath())));
			} else {
				polygon.setFill(Color.TRANSPARENT);
			}
			terrainCache = getCell().getTerrain().getImgPath();
		}
		double xCenter = (polygon.getBoundsInParent().getMinX() + polygon.getBoundsInParent().getMaxX()) / 2.0;
		double yCenter = (polygon.getBoundsInParent().getMinY() + polygon.getBoundsInParent().getMaxY()) / 2.0;
		if (unitCount != getCell().getOccupants().size()) {
			unitCount = unitCount < 0 ? getCell().getOccupants().size() : unitCount + 1;
			unitViews.forEach(e -> getController().removeFromUpdated(e));
			unitViews.clear();
			group.getChildren().clear();
			group.getChildren().addAll(polygon);
			getCell().getOccupants().forEach(unit -> {
				SimpleUnitView unitView = new SimpleUnitView(unit.getName(), unit.getLocation(), getController(), getClickHandler());
				unitViews.add(unitView);
				unitView.setSize(polygon.getBoundsInParent().getHeight() * UNIT_SCALE);
				group.getChildren().add(unitView.getObject());
				unitView.getObject().toFront();
			});
			setContextMenu();

		}
		unitViews.forEach(unitView -> {
			unitView.setSize(polygon.getBoundsInParent().getHeight() * UNIT_SCALE);
			unitView.getObject().relocate(
					xCenter - unitView.getObject().getWidth() / 2.0,
					yCenter - unitView.getObject().getHeight() / 2.0);

		});
		polygon.toBack();
		installToolTips();
	}

	private void installToolTips() {
		unitViews.forEach(uv -> Tooltip.install(uv.getObject(), new Tooltip(getToolTipString(uv))));
	}

	private void setContextMenu() {
		contextMenu.getItems().clear();
		getCell().getOccupants().forEach(e -> {
			MenuItem item = new MenuItem(getPolyglot().get("Select").getValueSafe() + " " + e.getName());
			contextMenu.getItems().add(item);
			item.addEventHandler(ActionEvent.ACTION, event -> unitViews.stream().filter(p -> p.getUnitName().equals(item.getText().substring(7)))
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
		polygon.setStrokeWidth(CELL_STROKE);
	}

	@Override
	public String toString() {
		return getCell().toString();
	}
}
