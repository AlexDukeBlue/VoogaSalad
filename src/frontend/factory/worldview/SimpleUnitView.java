package frontend.factory.worldview;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.util.GameplayState;
import backend.util.HasLocation;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.View;
import frontend.interfaces.worldview.UnitViewExternal;
import frontend.util.GameBoardObjectView;
import frontend.util.SelectableUIComponent;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.Objects;

/**
 * @author th174
 */
public final class SimpleUnitView extends SelectableUIComponent<Pane> implements UnitViewExternal {
	private static final double UNIT_SCALE = 0.75;
	private final BorderPane unitView;
	private final String unitName;
	private final CoordinateTuple unitLocation;
	private final Rectangle healthBar;
	private final Rectangle remainingHealthBar;

	/**
	 * Creates a new UnitView. Sets all values to default.
	 */
	public SimpleUnitView(String unitName, CoordinateTuple unitLocation, ReadOnlyObjectProperty<Bounds> boundsProperty, Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		this.unitName = unitName;
		this.unitLocation = unitLocation;
		ImageView imageView = new ImageView(View.getImg(getController().getCell(unitLocation).getOccupantByName(unitName).getImgPath()));
		imageView.setManaged(true);
		imageView.setPickOnBounds(true);
		imageView.setPreserveRatio(true);
		unitView = new BorderPane();
		unitView.setPickOnBounds(true);
		if (Objects.nonNull(getUnit().getHitPoints())) {
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
		unitView.setOnMouseClicked(event -> handleClick(null));
		boundsProperty.addListener((observable, oldValue, newValue) -> centerInBounds(newValue));
		centerInBounds(boundsProperty.getValue());
		update();
	}

	@Override
	public void update() {
		try {
			double fractionRemaining = getUnit().getHitPoints().getFractionRemaining();
			remainingHealthBar.heightProperty().bind(healthBar.heightProperty().multiply(fractionRemaining));
			if (fractionRemaining < 1 / 3.0) {
				remainingHealthBar.setFill(Color.RED);
			} else if (fractionRemaining < 2 / 3.0) {
				remainingHealthBar.setFill(Color.YELLOW);
			} else {
				remainingHealthBar.setFill(Color.GREEN);
			}
		} catch (NullPointerException ignored) {
		}
	}

	@Override
	public String getUnitName() {
		return unitName;
	}

	@Override
	public Unit getUnit() {
		return getController().getCell(unitLocation).getOccupantByName(unitName);
	}

	@Override
	public CoordinateTuple getUnitLocation() {
		return unitLocation;
	}

	public void centerInBounds(Bounds bounds) {
		((ImageView) unitView.getCenter()).setFitHeight(bounds.getHeight() * UNIT_SCALE);
		unitView.setMinWidth(bounds.getWidth() * UNIT_SCALE);
		unitView.setMaxWidth(bounds.getWidth() * UNIT_SCALE);
		unitView.setMinHeight(bounds.getHeight() * UNIT_SCALE);
		unitView.setMaxHeight(bounds.getHeight() * UNIT_SCALE);
		unitView.setLayoutX(bounds.getMinX() + bounds.getWidth() * (1 - UNIT_SCALE) / 2);
		unitView.setLayoutY(bounds.getMinY() + bounds.getHeight() * (1 - UNIT_SCALE) / 2);
	}

	/**
	 * Returns an object that allows the user to see the Unit object on a grid
	 *
	 * @return Region object representing the visualization of the Unit
	 */
	@Override
	public Pane getObject() {
		return unitView;
	}


	@Override
	public void select(ClickHandler clickHandler) {
		clickHandler.getGridPane().highlightRange(getUnit().getLegalMoves(getController().getGrid()));
	}

	@Override
	public void deselect(ClickHandler clickHandler) {
		clickHandler.getGridPane().resetHighlighting();
	}

	@Override
	public void actInAuthoringMode(ClickableUIComponent target, Object additonalInfo, ClickHandler clickHandler) {
		if (target instanceof GameBoardObjectView && ((GameBoardObjectView) target).getEntity() instanceof HasLocation) {
			CoordinateTuple unitLocation = getUnitLocation();
			String unitName = getUnitName();
			CoordinateTuple targetLocation = ((HasLocation) ((GameBoardObjectView) target).getEntity()).getLocation();
			getController().sendModifier((GameplayState gameState) -> {
				Unit unitToMove = gameState.getGrid().get(unitLocation).getOccupantByName(unitName);
				unitToMove.moveTo(gameState.getGrid().get(targetLocation), gameState);
				return gameState;
			});
		}
		clickHandler.cancel();
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler) {
		//TODO Gameplay
		actInAuthoringMode(target, additionalInfo, clickHandler);
	}
}
