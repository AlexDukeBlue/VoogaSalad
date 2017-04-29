package frontend.factory.worldview;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.View;
import frontend.factory.worldview.layout.GridLayoutDelegate;
import frontend.factory.worldview.layout.GridLayoutDelegateFactory;
import frontend.interfaces.worldview.GridViewExternal;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class SimpleGridView extends ClickableUIComponent<ScrollPane> implements GridViewExternal {

	private static final double MIN = 10, MAX = 100, SCALE = 0.750;
	private final ScrollPane myScrollPane;
	private final Pane cellViewObjects;
	private final Map<CoordinateTuple, SimpleCellView> cellViews;
	private final GridLayoutDelegate myLayoutManager;
	private Set<CoordinateTuple> savedGridCoordinates;

	public SimpleGridView(Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		myScrollPane = new ScrollPane();
		cellViewObjects = new Pane();
		cellViews = new HashMap<>();
		myLayoutManager = new GridLayoutDelegateFactory();
		initialize();
	}

	@Override
	public void update() {
		if (!getController().getGrid().getCells().keySet().equals(savedGridCoordinates)) {
			cellViews.values().forEach(e -> getController().removeListener(e));
			cellViews.clear();
			cellViewObjects.getChildren().clear();
			populateCellViews();
			savedGridCoordinates = getController().getGrid().getCells().keySet();
		}
	}

	@Override
	public ScrollPane getNode() {
		return myScrollPane;
	}

	private void populateCellViews() {
		cellViewObjects.setBackground(new Background(new BackgroundFill(new ImagePattern(getImg(getController().getGrid().getImgPath())), null, null)));
		getController().getGrid().getCells().keySet().forEach(coordinate -> {
			SimpleCellView cl = new SimpleCellView(coordinate, getController(), getClickHandler(), myLayoutManager.layoutCell(SCALE, MIN, MAX, coordinate, getController().getGrid()));
			cellViews.put(coordinate, cl);
			cellViewObjects.getChildren().add(cl.getNode());
		});
	}

	private void initialize() {
		myScrollPane.setOnZoom(event -> {
			cellViewObjects.setScaleX(cellViewObjects.getScaleX() * event.getZoomFactor());
			cellViewObjects.setScaleY(cellViewObjects.getScaleY() * event.getZoomFactor());
			event.consume();
		});

		myScrollPane.addEventFilter(ScrollEvent.ANY, event -> {
			if (event.isShortcutDown()) {
				cellViewObjects.setScaleX(cellViewObjects.getScaleX() - event.getDeltaY() / 700);
				cellViewObjects.setScaleY(cellViewObjects.getScaleY() - event.getDeltaY() / 700);
				event.consume();
			}
		});
		populateCellViews();
		myScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		myScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		myScrollPane.setPannable(true);
		myScrollPane.setContent(new Group(cellViewObjects));
		myScrollPane.setOnKeyPressed(event -> {
			if(event.getCode().equals(KeyCode.DELETE) || event.getCode().equals(KeyCode.BACK_SPACE)){
				handleClick(event, null);
			}
		});
	}

	@Override
	public void setClickHandler(ClickHandler clickHandler) {
		super.setClickHandler(clickHandler);
		cellViews.values().forEach(e -> e.setClickHandler(clickHandler));
	}

	@Override
	public void highlightRange(Collection<CoordinateTuple> highlightedCells) {
		cellViews.entrySet().stream().filter(e -> !highlightedCells.contains(e.getKey())).forEach(e -> e.getValue().darken());
	}

	@Override
	public void resetHighlighting() {
		cellViews.values().forEach(SimpleCellView::unDarken);
	}

	@Override
	public GameBoard getEntity() {
		return getController().getReadOnlyGameState().getGrid();
	}
}
