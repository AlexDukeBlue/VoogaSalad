package frontend.factory.wizard.wizards.strategies.wizard_pages.util;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.GridPattern;
import backend.grid.ModifiableGameBoard;
import backend.util.AuthoringGameState;
import frontend.factory.worldview.layout.GridLayoutDelegate;
import frontend.factory.worldview.layout.GridLayoutDelegateFactory;
import frontend.util.BaseUIManager;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class WizardGrid extends BaseUIManager<Region> {
	private static final double MIN = 10, MAX = 100, SCALE = 0.750;
	private static final Paint UNCLICKED_FILL = Color.WHITE, CLICKED_FILL = Color.GREEN, BORDER = Color.BLACK;
	private static final double BORDER_WIDTH = 5.0;

	private ScrollPane pane;
	private Pane gridView;
	private Map<Polygon, CoordinateTuple> polygons;
	private GridLayoutDelegate delegate;
	private Collection<Polygon> clickedPolygons;

	public WizardGrid(AuthoringGameState gameState) {
		initialize(gameState);
	}

	@Override
	public Region getNode() {
		return pane;
	}

	public GridPattern getGridPattern() {
		return new GridPattern("", "", "",
				clickedPolygons.stream().map(polygon -> polygons.get(polygon)).collect(Collectors.toList()));
	}

	private void initialize(AuthoringGameState gameState) {
		pane = new ScrollPane();
		gridView = new Pane();
		pane.setContent(new Group(gridView));
		polygons = new HashMap<>();
		delegate = new GridLayoutDelegateFactory();
		clickedPolygons = new HashSet<>();
		pane.setOnZoom(event -> {
			gridView.setScaleX(gridView.getScaleX() * event.getZoomFactor());
			gridView.setScaleY(gridView.getScaleY() * event.getZoomFactor());
			event.consume();
		});
		gridView.addEventFilter(ScrollEvent.ANY, event -> {
			if (event.isShortcutDown()) {
				gridView.setScaleX(gridView.getScaleX() - event.getDeltaY() / 700);
				gridView.setScaleY(gridView.getScaleY() - event.getDeltaY() / 700);
				event.consume();
			}
		});
		pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		GameBoard board = new ModifiableGameBoard(gameState.getGrid().getName(), gameState.getGrid().getTemplateCell(),
				gameState.getGrid().getRows() * 2, gameState.getGrid().getColumns() * 2, gameState.getGrid().getBoundsHandler(),
				gameState.getGrid().getDescription(), gameState.getGrid().getImgPath()).build();

		board.getCells().keySet().forEach(coordinate -> {
			Polygon polygon = delegate.layoutCell(SCALE, MIN, MAX, coordinate, board);
			polygon.setStroke(BORDER);
			polygon.setStrokeWidth(BORDER_WIDTH);
			polygon.setFill(UNCLICKED_FILL);
			EventHandler<Event> onMouseDrag = event -> {
				if (polygon.getFill().equals(UNCLICKED_FILL)) {
					polygon.setFill(CLICKED_FILL);
					clickedPolygons.add(polygon);
				} else {
					polygon.setFill(UNCLICKED_FILL);
					clickedPolygons.remove(polygon);
				}
			};
			polygon.setOnMouseClicked(onMouseDrag);
			polygon.setOnDragEntered(onMouseDrag);
			polygons.put(polygon, coordinate);
			gridView.getChildren().add(polygon);
			if (coordinate.equals(CoordinateTuple.getOrigin(board.dimension()))) {
				polygon.setOnMouseClicked(event -> {
				});
				polygon.setFill(Color.RED);
			}
		});
	}

}
