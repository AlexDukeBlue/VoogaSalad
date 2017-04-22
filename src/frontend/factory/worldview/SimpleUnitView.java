package frontend.factory.worldview;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import controller.Controller;
import frontend.View;
import frontend.interfaces.worldview.UnitViewExternal;
import frontend.interfaces.worldview.UnitViewObserver;
import frontend.util.BaseUIManager;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class SimpleUnitView extends BaseUIManager<Pane> implements UnitViewExternal {

	private final Collection<UnitViewObserver> observers;

	private final Pane unitView;
	private final String unitName;
	private final CoordinateTuple unitLocation;
	private Rectangle healthBar;
	private Rectangle remainingHealthBar;

	/**
	 * Creates a new UnitView. Sets all values to default.
	 */
	public SimpleUnitView(String unitName, CoordinateTuple unitLocation, Controller controller) {
		super(controller);
		observers = new ArrayList<>();
		this.unitName = unitName;
		this.unitLocation = unitLocation;
		ImageView imageView = new ImageView(View.getImg(getController().getCell(unitLocation).getOccupantByName(unitName).getImgPath()));
		imageView.setX(3);
		unitView = new Pane(imageView);
		if (Objects.nonNull(getUnit().getHitPoints())) {
			remainingHealthBar = new Rectangle();
			remainingHealthBar.setWidth(5);
			healthBar = new Rectangle();
			healthBar.setWidth(5);
			healthBar.setFill(Color.TRANSPARENT);
			healthBar.setStrokeWidth(1);
			healthBar.heightProperty().bind(unitView.heightProperty());
			unitView.getChildren().addAll(remainingHealthBar, healthBar);
		}
		unitView.setOnMouseClicked(event -> observers.stream().filter(Objects::nonNull).forEach(observer -> observer.didClickUnitViewExternalInterface(this)));
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
				healthBar.setStroke(Color.RED);
			} else if (fractionRemaining < 2 / 3.0) {
				remainingHealthBar.setFill(Color.YELLOW);
				healthBar.setStroke(Color.YELLOW);
			} else {
				remainingHealthBar.setFill(Color.GREEN);
				healthBar.setStroke(Color.GREEN);
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
	public void addUnitViewObserver(UnitViewObserver observer) {
		observers.addAll(Collections.singleton(observer));
	}

	@Override
	public void removeUnitViewObserver(UnitViewObserver observer) {
		observers.removeAll(Collections.singleton(observer));
	}

	@Override
	public void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		observers.addAll(unitViewObservers);
	}

	@Override
	public void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		observers.removeAll(unitViewObservers);
	}

}
