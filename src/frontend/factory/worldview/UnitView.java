package frontend.factory.worldview;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.util.HasLocation;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.interfaces.worldview.UnitViewExternal;
import frontend.util.GameBoardObjectView;
import frontend.util.SelectableUIComponent;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.Collections;
import java.util.Objects;

/**
 * UnitView creates a UI manager that manipulates a Node to represent a Unit
 * object. It can be added to things like a GridView, CellView, WorldView, etc.
 *
 * @author th174, Dylan Peters
 */
class UnitView extends SelectableUIComponent<Pane> implements UnitViewExternal {
	private static final double UNIT_SCALE = 0.75;
	private static final String DEFAULT_COLOR = "#ffffff";
	private final BorderPane unitView;
	private final String unitName;
	private final CoordinateTuple unitLocation;
	private final Rectangle healthBar;
	private final Rectangle remainingHealthBar;
	private final DropShadow dropShadow;

	/**
	 * Creates a new UnitView. Sets all values to default.
	 */
	public UnitView(String unitName, CoordinateTuple unitLocation, ReadOnlyObjectProperty<Bounds> boundsProperty,
	                Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		this.unitName = unitName;
		this.unitLocation = unitLocation;
		ImageView imageView = new ImageView(getImg(getEntity().getImgPath()));
		imageView.setManaged(true);
		imageView.setPickOnBounds(true);
		imageView.setPreserveRatio(true);
		dropShadow = new DropShadow(15, Color.WHITE);
		dropShadow.setSpread(.3);
		imageView.setEffect(dropShadow);
		unitView = new BorderPane();
		unitView.setPickOnBounds(true);
		if (Objects.nonNull(getEntity().getHitPoints())) {
			remainingHealthBar = new Rectangle();
			remainingHealthBar.setWidth(5);
			healthBar = new Rectangle();
			healthBar.setWidth(5);
			healthBar.setFill(Color.TRANSPARENT);
			healthBar.setStrokeWidth(1);
			healthBar.setStrokeType(StrokeType.OUTSIDE);
			healthBar.setStroke(Color.WHITE);
			healthBar.heightProperty().bind(unitView.heightProperty());
			unitView.setLeft(new Group(healthBar, remainingHealthBar));
		} else {
			healthBar = null;
			remainingHealthBar = null;
		}
		unitView.setCenter(imageView);
		boundsProperty.addListener((observable, oldValue, newValue) -> centerInBounds(newValue));
		centerInBounds(boundsProperty.getValue());
		update();
	}

	@Override
	public void handleClick(Event event, Object o) {
		super.handleClick(event, o);
	}

	@Override
	public void update() {
		try {
			dropShadow.setColor(Color.web(getEntity().getTeam().isPresent() ? getEntity().getTeam().get().getColorString() : DEFAULT_COLOR));
			double fractionRemaining = getEntity().getHitPoints().getFractionRemaining();
			remainingHealthBar.heightProperty().bind(healthBar.heightProperty().multiply(fractionRemaining));
			if (fractionRemaining < 1 / 3.0) {
				remainingHealthBar.setFill(Color.RED);
			} else if (fractionRemaining < 2 / 3.0) {
				remainingHealthBar.setFill(Color.YELLOW);
			} else {
				remainingHealthBar.setFill(Color.GREEN);
			}
		} catch (NullPointerException ignored) {
			//this null pointer exception should be ignored because 
			//it occurs when the unit is removed from the grid when the grid is being updated. 
			//The issue fixes itself as soon as the update finishes
		}
	}

	@Override
	public String getUnitName() {
		return unitName;
	}

	@Override
	public Unit getEntity() {
		return getController().getCell(unitLocation).getOccupantByName(unitName);
	}

	@Override
	public CoordinateTuple getUnitLocation() {
		return unitLocation;
	}

	/**
	 * Returns an object that allows the user to see the Unit object on a grid
	 *
	 * @return Region object representing the visualization of the Unit
	 */
	@Override
	public Pane getNode() {
		return unitView;
	}

	@Override
	public void select(ClickHandler clickHandler) {
		clickHandler.highlightRange(canMove() ? getEntity().getLegalMoves(getController().getGrid()) : Collections.emptyList());
	}

	@Override
	public void deselect(ClickHandler clickHandler) {
		clickHandler.resetHighlighting();
	}

	@Override
	public void actInAuthoringMode(ClickableUIComponent target, Object additonalInfo, ClickHandler clickHandler, Event event) {
		System.err.println("authoring or gameplay");
		if (isValidMove(target)) {
			getController().moveUnit(getUnitName(), getUnitLocation(), ((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation());
			if (((GameBoardObjectView) target).getEntity() instanceof HasLocation) {
				playMedia(getController().getCell(((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation()).getTerrain().getSoundPath());
			}
		} else if (event instanceof KeyEvent && (((KeyEvent) event).getCode().equals(KeyCode.DELETE)
				|| ((KeyEvent) event).getCode().equals(KeyCode.BACK_SPACE))) {
			getController().removeUnitFromGrid(getUnitName(), getUnitLocation());
		}
		clickHandler.cancel();
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler,
	                              Event event) {
		System.err.println("this is gameplay mode");
		if (isValidMove(target)) {
			CoordinateTuple targetLocation = ((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation();
			if (canMove() && getEntity().getLegalMoves(getController().getGrid()).contains(targetLocation)) {
				actInAuthoringMode(target, additionalInfo, clickHandler, event);
			}
		}
		clickHandler.cancel();
	}

	private void centerInBounds(Bounds bounds) {
		((ImageView) unitView.getCenter()).setFitHeight(bounds.getHeight() * UNIT_SCALE);
		unitView.setMinWidth(bounds.getWidth() * UNIT_SCALE);
		unitView.setMaxWidth(bounds.getWidth() * UNIT_SCALE);
		unitView.setMinHeight(bounds.getHeight() * UNIT_SCALE);
		unitView.setMaxHeight(bounds.getHeight() * UNIT_SCALE);
		unitView.setLayoutX(bounds.getMinX() + bounds.getWidth() * (1 - UNIT_SCALE) / 2);
		unitView.setLayoutY(bounds.getMinY() + bounds.getHeight() * (1 - UNIT_SCALE) / 2);
	}

	private boolean canMove() {
		return !getEntity().getTeam().isPresent() || getController().isMyTeam() && getEntity().getTeam().isPresent() && getEntity().getTeam().get().equals(getController().getActiveTeam());
	}

	private boolean isValidMove(ClickableUIComponent target) {
		return target instanceof GameBoardObjectView
				&& ((GameBoardObjectView) target).getEntity() instanceof HasLocation;
	}

}
