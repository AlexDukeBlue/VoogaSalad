package frontend.factory.worldview;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.Shape;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.factory.worldview.layout.GridLayoutDelegate;
import frontend.factory.worldview.layout.GridLayoutDelegateFactory;
import frontend.interfaces.worldview.GridViewExternal;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

/**
 * The GridView class creates a UI object that visually represents a back end
 * grid object.
 * 
 * @author Dylan Peters
 *
 */
class GridView extends ClickableUIComponent<ScrollPane> implements GridViewExternal {

	private static final double MIN = 10, MAX = 100, SCALE = 0.750;
	public static final double FULL_CIRCLE = Math.PI * 2;
	private final ScrollPane myScrollPane;
	private final Pane cellViewObjects;
	private final Map<CoordinateTuple, CellView> cellViews;
	private final GridLayoutDelegate myLayoutManager;
	private Set<CoordinateTuple> savedGridCoordinates;

	/**
	 * Creates a new instance of GridView. Sets all values to default.
	 * 
	 * @param controller
	 *            The Controller with which the GridView will communicate to
	 *            update itself.
	 * @param clickHandler
	 *            the ClickHandler that will determine the program's behavior
	 *            when a part of the GridView is clicked.
	 */
	public GridView(Controller controller, ClickHandler clickHandler) {
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

	@Override
	public void setClickHandler(ClickHandler clickHandler) {
		super.setClickHandler(clickHandler);
		cellViews.values().forEach(e -> e.setClickHandler(clickHandler));
	}

	@Override
	public void highlightRange(Collection<CoordinateTuple> highlightedCells) {
		cellViews.entrySet().stream().filter(e -> !highlightedCells.contains(e.getKey()))
				.forEach(e -> e.getValue().darken());
	}

	@Override
	public void resetHighlighting() {
		cellViews.values().forEach(CellView::unDarken);
	}

	@Override
	public GameBoard getEntity() {
		return getController().getReadOnlyGameState().getGrid();
	}

	private void populateCellViews() {
		double width = MIN + ((MAX - MIN) * SCALE);
		double radius = width /(Math.cos(FULL_CIRCLE / 12) - Math.cos((FULL_CIRCLE / 12) * 5));
		double xOffset = (getController().getGrid().getRows());
		double inset = (0.7*xOffset)*radius;
		if (getController().getShape().equals(Shape.SQUARE)){
			cellViewObjects.setBackground(new Background(new BackgroundFill(new ImagePattern(getImg(getController().getGrid().getImgPath())), null, null)));
		} else {
			cellViewObjects.setBackground(new Background(new BackgroundFill(new ImagePattern(getImg(getController().getGrid().getImgPath())), null, new Insets(-1*inset, 0, inset, 0))));
		}
		getController().getGrid().getCells().keySet().forEach(coordinate -> {
			CellView cl = new CellView(coordinate, getController(), getClickHandler(),
					myLayoutManager.layoutCell(SCALE, MIN, MAX, coordinate, getController().getGrid()));
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
			if (event.getCode().equals(KeyCode.DELETE) || event.getCode().equals(KeyCode.BACK_SPACE)) {
				handleClick(event, null);
			}
		});
	}
}
