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
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.Objects;

public class SimpleUnitView extends SelectableUIComponent<Pane> implements UnitViewExternal {
	private final Pane unitView;
	private final String unitName;
	private final CoordinateTuple unitLocation;
	private Rectangle healthBar;
	private Rectangle remainingHealthBar;

	/**
	 * Creates a new UnitView. Sets all values to default.
	 */
	public SimpleUnitView(String unitName, CoordinateTuple unitLocation, Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		this.unitName = unitName;
		this.unitLocation = unitLocation;
		ImageView imageView = new ImageView(View.getImg(getController().getCell(unitLocation).getOccupantByName(unitName).getImgPath()));
		imageView.setX(3);
		unitView = new Pane(imageView);
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
			unitView.getChildren().addAll(remainingHealthBar, healthBar);
		}
		unitView.setOnMouseClicked(event -> handleClick(null));
		imageView.fitHeightProperty().bind(unitView.heightProperty());
		imageView.fitWidthProperty().bind(unitView.widthProperty().subtract(3));
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

	public String getUnitName() {
		return unitName;
	}

	public Unit getUnit() {
		return getController().getCell(unitLocation).getOccupantByName(unitName);
	}

	public CoordinateTuple getUnitLocation() {
		return unitLocation;
	}

	public void setSize(double size) {
		unitView.setMinSize(size, size);
		unitView.setMaxSize(size, size);
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
	public void deselect() {
		super.deselect();
	}

	@Override
	public void actInAuthoringMode(ClickableUIComponent target, Object additonalInfo) {
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
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo) {

	}
}
