package frontend.factory.worldview;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
import backend.unit.properties.UnitStat;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.View;
import frontend.interfaces.worldview.CellViewExternal;
import frontend.interfaces.worldview.UnitViewExternal;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SimpleCellView extends ClickableUIComponent<Group> implements CellViewExternal {
	private static final Paint CELL_OUTLINE = Color.BLACK;
	private static final double CELL_STROKE = 2;
	private final CoordinateTuple cellLocation;
	private final Polygon polygon;
	private final Polygon polygonMask;
	private final Group updateGroup;
	private final ContextMenu contextMenu;
	private final Map<String, SimpleUnitView> unitViews;

	private String terrainCache;
	private int unitCount;

	/**
	 * Creates a new CellView instance. Sets all values to default.
	 *
	 * @param cellLocation    The Cell object that this CellView will visually represent.
	 * @param controller      the controller object that this CellView will send information
	 * @param clickHandler
	 * @param polygonCellView
	 */
	public SimpleCellView(CoordinateTuple cellLocation, Controller controller, ClickHandler clickHandler, Polygon polygonCellView) {
		super(controller, clickHandler);
		this.unitViews = new HashMap<>();
		this.cellLocation = cellLocation;
		contextMenu = new ContextMenu();
		updateGroup = new Group();
		this.polygon = polygonCellView;
		polygon.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> contextMenu.show(polygon, event.getScreenX(), event.getScreenY()));
		polygon.setOnMouseClicked(event -> handleClick(event, null));
		polygon.setStroke(CELL_OUTLINE);
		polygon.setStrokeWidth(CELL_STROKE);
		polygonMask = new Polygon(polygon.getPoints().stream().mapToDouble(e -> e).toArray());
		polygonMask.setFill(Color.TRANSPARENT);
		polygonMask.layoutXProperty().bind(polygon.layoutXProperty());
		polygonMask.layoutYProperty().bind(polygon.layoutYProperty());
		polygonMask.setMouseTransparent(true);
		getPolyglot().addLanguageChangeHandler(change -> {
			setContextMenu();
			installToolTips();
		});
		this.unitCount = -2;
		update();
	}

	/**
	 * Returns an object that can be displayed to the user to show the Cell
	 */
	@Override
	public Group getObject() {
		return new Group(polygon, polygonMask, updateGroup);
	}

	/**
	 * passes a cell's location to the controller for the backend to use, and
	 * determine its validity
	 */
	@Override
	public void update() {
		if (Objects.nonNull(getCell())) {
			if (unitCount < 0 || !getCell().getTerrain().getImgPath().equals(terrainCache)) {
				if (getController().getGrid().getImgPath().length() < 1) {
					polygon.setFill(new ImagePattern(View.getImg(getCell().getTerrain().getImgPath())));
				} else {
					polygon.setFill(Color.TRANSPARENT);
				}
				terrainCache = getCell().getTerrain().getImgPath();
			}
			if (unitCount != getCell().getOccupants().size()) {
				unitCount = unitCount < 0 ? getCell().getOccupants().size() : unitCount + 1;
				unitViews.values().forEach(e -> getController().removeListener(e));
				unitViews.clear();
				updateGroup.getChildren().clear();
				getCell().getOccupants().forEach(unit -> {
					SimpleUnitView unitView = new SimpleUnitView(unit.getName(), unit.getLocation(), polygon.boundsInParentProperty(), getController(), getClickHandler());
					unitViews.put(unit.getName(), unitView);
					updateGroup.getChildren().add(unitView.getObject());
				});
				if (getCell().getOccupants().size() <= 1) {
					unitViews.values().forEach(unitView -> unitView.getObject().setOnMouseClicked(event -> unitView.handleClick(event, null)));
				} else {
					unitViews.values().forEach(unitView -> unitView.getObject().setOnMouseClicked(event -> contextMenu.show(polygon, event.getScreenX(), event.getScreenY())));
					Text numOccupants = new Text(getCell().getOccupants().size() + "");
					numOccupants.setFont(new Font(13));
					numOccupants.setLayoutX(polygon.getBoundsInParent().getMaxX() - numOccupants.getLayoutBounds().getWidth() - 4);
					numOccupants.setLayoutY(polygon.getBoundsInParent().getMaxY() - numOccupants.getLayoutBounds().getHeight() + 9);
					updateGroup.getChildren().add(numOccupants);
					numOccupants.toFront();
				}
				setContextMenu();
			}
			updateGroup.toFront();
			installToolTips();
		} else {
			getController().removeListener(this);
		}
	}

	private void installToolTips() {
		unitViews.values().forEach(uv -> Tooltip.install(uv.getObject(), new Tooltip(getToolTipString(uv))));
	}

	private void setContextMenu() {
		contextMenu.getItems().clear();
		getCell().getOccupants().forEach(e -> {
			MenuItem item = new MenuItem(e.getName());
			contextMenu.getItems().add(item);
			item.setOnAction(event -> unitViews.get(e.getName()).handleClick(event, null));
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
		unitViews.values().forEach(e -> setClickHandler(clickHandler));
	}

	@Override
	public String toString() {
		return getCell().toString();
	}

	@Override
	public void darken() {
		polygonMask.setFill(new Color(0, 0, 0, .7));
	}

	@Override
	public void unDarken() {
		polygonMask.setFill(Color.TRANSPARENT);
	}
}
