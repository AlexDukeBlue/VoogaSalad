package frontend.factory.worldview;

import backend.grid.CoordinateTuple;
import frontend.View;
import frontend.interfaces.worldview.UnitViewExternal;
import frontend.interfaces.worldview.UnitViewObserver;
import frontend.util.BaseUIManager;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class SimpleUnitView extends BaseUIManager<Node> implements UnitViewExternal {

	private Collection<UnitViewObserver> observers;

	private ImageView unitView;
	private String unitName;
	private CoordinateTuple unitLocation;

	/**
	 * Creates a new UnitView. Sets all values to default.
	 */
	public SimpleUnitView(String unitName, CoordinateTuple unitLocation, String unitImgPath) {
		initialize(unitName, unitLocation, unitImgPath);
	}

	public String getUnitName() {
		return unitName;
	}

	public CoordinateTuple getUnitLocation() {
		return unitLocation;
	}

	public void setX(double x) {
		unitView.setX(x);
	}

	public double getX() {
		return unitView.getX();
	}

	public DoubleProperty xProperty() {
		return unitView.xProperty();
	}

	public void setY(double y) {
		unitView.setY(y);
	}

	public double getY() {
		return unitView.getY();
	}

	public DoubleProperty yProperty() {
		return unitView.yProperty();
	}

	public void setFitWidth(double fitWidth) {
		unitView.setFitWidth(fitWidth);
	}

	public double getFitWidth() {
		return unitView.getFitWidth();
	}

	public DoubleProperty fitWidthProperty() {
		return unitView.fitWidthProperty();
	}

	public void setFitHeight(double fitHeight) {
		unitView.setFitHeight(fitHeight);
	}

	public double getFitHeight() {
		return unitView.getFitHeight();
	}

	public DoubleProperty fitHeightProperty() {
		return unitView.fitHeightProperty();
	}

	/**
	 * Returns an object that allows the user to see the Unit object on a grid
	 *
	 * @return Region object representing the visualization of the Unit
	 */
	@Override
	public ImageView getObject() {
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

	private void initialize(String unitName, CoordinateTuple unitLocation, String unitImgPath) {
		observers = new ArrayList<>();
		this.unitName = unitName;
		this.unitLocation = unitLocation;
		unitView = new ImageView(View.getImg(unitImgPath));
		unitView.setOnMouseClicked(event -> observers.stream().filter(Objects::nonNull).forEach(observer -> observer.didClickUnitViewExternalInterface(this)));
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
