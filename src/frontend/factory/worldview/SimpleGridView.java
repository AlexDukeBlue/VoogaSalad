package frontend.factory.worldview;

import controller.Controller;
import frontend.View;
import frontend.factory.worldview.layout.GridLayoutDelegate;
import frontend.factory.worldview.layout.GridLayoutDelegateFactory;
import frontend.interfaces.worldview.*;
import frontend.util.BaseUIManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;
import java.util.Collection;

class SimpleGridView extends BaseUIManager<Node> implements GridViewExternal {

	private Collection<GridViewObserver> observers;
	private static final double MIN = 10, MAX = 100, SCALE = 0.750;
	private ScrollPane myScrollPane;
	private Pane cellViewObjects;
	private Collection<CellViewExternal> cellViews;
	private GridLayoutDelegate myLayoutManager;

	public SimpleGridView(Controller controller, Collection<GridViewObserver> gridObservers,
	                      Collection<CellViewObserver> cellObservers, Collection<UnitViewObserver> unitObservers) {
		super(controller);
		initialize(gridObservers, cellObservers, unitObservers);
	}

	@Override
	public ScrollPane getObject() {
		return myScrollPane;
	}

	private void populateCellViews(Collection<CellViewObserver> cellObservers,
	                               Collection<UnitViewObserver> unitObservers) {
		cellViewObjects.setBackground(new Background(
				new BackgroundFill(new ImagePattern(View.getImg(getController().getGrid().getImgPath())), null, null)));
		cellViews = new ArrayList<>();
		getController().getGrid().getCells().keySet().forEach(coordinate -> {
			SimpleCellView cl = new SimpleCellView(coordinate, getController());
			cellViews.add(cl);
			cl.addAllCellViewObservers(cellObservers);
			cl.addAllUnitViewObservers(unitObservers);
			myLayoutManager.layoutCell(cl, SCALE, MIN, MAX);
			cl.update();
			cellViewObjects.getChildren().add(cl.getObject());
		});
	}

	@Override
	public void addGridViewObserver(GridViewObserver observer) {
		if (!observers.contains(observer) && observer != null) {
			observers.add(observer);
		}
	}

	@Override
	public void removeGridViewObserver(GridViewObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	private void initialize(Collection<GridViewObserver> gridObservers, Collection<CellViewObserver> cellObservers,
	                        Collection<UnitViewObserver> unitObservers) {
		observers = new ArrayList<>();
		if (gridObservers != null) {
			gridObservers.forEach(observer -> addGridViewObserver(observer));
		}

		myScrollPane = new ScrollPane();
		cellViewObjects = new Pane();
		Group zoomGroup = new Group(cellViewObjects);
		myScrollPane.setOnZoom(event -> {
			cellViewObjects.setScaleX(cellViewObjects.getScaleX() * event.getZoomFactor());
			cellViewObjects.setScaleY(cellViewObjects.getScaleY() * event.getZoomFactor());
		});
		myScrollPane.addEventFilter(ScrollEvent.ANY, event -> {
			if (event.isShortcutDown()) {
				cellViewObjects.setScaleX(cellViewObjects.getScaleX() + event.getDeltaY() / 500);
				cellViewObjects.setScaleY(cellViewObjects.getScaleY() + event.getDeltaY() / 500);
				event.consume();
			}
		});
		myLayoutManager = new GridLayoutDelegateFactory();
		populateCellViews(cellObservers, unitObservers);
		myScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		myScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		myScrollPane.setPannable(true);
		myScrollPane.setContent(zoomGroup);
	}

	@Override
	public void addCellViewObserver(CellViewObserver observer) {
		cellViews.forEach(cellView -> cellView.addCellViewObserver(observer));
	}

	@Override
	public void addAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		cellViews.forEach(cellView -> cellView.addAllCellViewObservers(cellViewObservers));
	}

	@Override
	public void removeCellViewObserver(CellViewObserver observer) {
		cellViews.forEach(cellView -> cellView.removeCellViewObserver(observer));
	}

	@Override
	public void removeAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		cellViews.forEach(cellView -> cellView.removeAllCellViewObservers(cellViewObservers));
	}

	@Override
	public void addUnitViewObserver(UnitViewObserver observer) {
		cellViews.forEach(cellView -> cellView.addUnitViewObserver(observer));
	}

	@Override
	public void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		cellViews.forEach(cellView -> cellView.addAllUnitViewObservers(unitViewObservers));
	}

	@Override
	public void removeUnitViewObserver(UnitViewObserver observer) {
		cellViews.forEach(cellView -> cellView.removeUnitViewObserver(observer));
	}

	@Override
	public void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		cellViews.forEach(cellView -> cellView.removeAllUnitViewObservers(unitViewObservers));
	}

	@Override
	public void addAllGridViewObservers(Collection<GridViewObserver> gridViewObservers) {
		gridViewObservers.forEach(observer -> addGridViewObserver(observer));
	}

	@Override
	public void removeAllGridViewObservers(Collection<GridViewObserver> gridViewObservers) {
		if (gridViewObservers != null) {
			gridViewObservers.stream().forEach(observer -> removeGridViewObserver(observer));
		}
	}

}