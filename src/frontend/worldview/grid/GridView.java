package frontend.worldview.grid;

import controller.Controller;
import frontend.View;
import frontend.factory.worldview.layout.GridLayoutDelegate;
import frontend.factory.worldview.layout.GridLayoutDelegateFactory;
import frontend.interfaces.worldview.*;
import frontend.util.BaseUIManager;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * Holds a grid to be displayed in the development and player GUI inside a
 * ScrollPane. It can have Sprites added to a particular cell or have all cells
 * updated after something occurs in the game. Contains a group of CellView
 * objects and a collection of CellView's, when update method is called, it can
 * tell all of the units/cells to update themselves
 *
 * @author Andreas Santos,Timmy Huang Created 3/29/2017
 */
public class GridView extends BaseUIManager<ScrollPane> implements GridViewExternal {
	private static final double MIN = 10, MAX = 100, SCALE = 0.750;
	private final Collection<GridViewObserver> observers;
	private final Collection<CellViewExternal> cellViews;
	private final GridLayoutDelegate myLayoutManager;
	private final ScrollPane myScrollPane;
	private final Pane cellViewObjects;

	public GridView(Controller controller) {
		super(controller);
		cellViewObjects = new Pane();
		cellViews = new ArrayList<>();
		observers = new ArrayList<>();
		myLayoutManager = new GridLayoutDelegateFactory();
		myScrollPane = initialize();
		update();
	}

	@Override
	public void update() {

	}

	private ScrollPane initialize() {
		ScrollPane scrollPane = new ScrollPane();
		Group zoomGroup = new Group(cellViewObjects);
		scrollPane.setOnZoom(event -> {
			cellViewObjects.setScaleX(cellViewObjects.getScaleX() * event.getZoomFactor());
			cellViewObjects.setScaleY(cellViewObjects.getScaleY() * event.getZoomFactor());
		});
		scrollPane.addEventFilter(ScrollEvent.ANY, event -> {
			if (event.isShortcutDown()) {
				cellViewObjects.setScaleX(cellViewObjects.getScaleX() + event.getDeltaY() / 500);
				cellViewObjects.setScaleY(cellViewObjects.getScaleY() + event.getDeltaY() / 500);
				event.consume();
			}
		});
		populateCellViews();
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setPannable(true);
		scrollPane.setContent(zoomGroup);
		return scrollPane;
	}

	@Override
	public void addGridViewObserver(GridViewObserver observer) {
		addAllGridViewObservers(Collections.singleton(observer));
	}

	@Override
	public void addAllGridViewObservers(Collection<GridViewObserver> gridViewObservers) {
		observers.addAll(gridViewObservers);
	}

	@Override
	public void removeGridViewObserver(GridViewObserver observer) {
		removeAllGridViewObservers(Collections.singleton(observer));
	}

	@Override
	public void removeAllGridViewObservers(Collection<GridViewObserver> gridViewObservers) {
		observers.removeAll(gridViewObservers);
	}

	@Override
	public void addCellViewObserver(CellViewObserver observer) {
		addAllCellViewObservers(Collections.singleton(observer));
	}

	@Override
	public void addAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		cellViews.forEach(e -> e.addAllCellViewObservers(cellViewObservers));
	}

	@Override
	public void removeCellViewObserver(CellViewObserver observer) {
		removeAllCellViewObservers(Collections.singleton(observer));
	}

	@Override
	public void removeAllCellViewObservers(Collection<CellViewObserver> cellViewObservers) {
		cellViews.forEach(cellView -> cellView.removeAllCellViewObservers(cellViewObservers));
	}

	@Override
	public void addUnitViewObserver(UnitViewObserver observer) {
		addAllUnitViewObservers(Collections.singleton(observer));
	}

	@Override
	public void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		cellViews.forEach(cellView -> cellView.addAllUnitViewObservers(unitViewObservers));
	}

	@Override
	public void removeUnitViewObserver(UnitViewObserver observer) {
		removeAllUnitViewObservers(Collections.singleton(observer));
	}

	@Override
	public void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers) {
		cellViews.forEach(cellView -> cellView.removeAllUnitViewObservers(unitViewObservers));
	}

	@Override
	public ScrollPane getObject() {
		return myScrollPane;
	}

	private void populateCellViews() {
		cellViewObjects.setBackground(new Background(new BackgroundFill(new ImagePattern(View.getImg(getController().getGrid().getImgPath())), null, null)));
		getController().getGrid().getCells().keySet().forEach(coordinate -> {
			CellView cl = new CellView(coordinate, getController());
			cellViews.add(cl);
			myLayoutManager.layoutCell(cl, SCALE, MIN, MAX);
			cl.update();
			cellViewObjects.getChildren().add(cl.getObject());
		});
	}
}