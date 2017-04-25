package frontend.factory.wizard.wizards.strategies.wizard_pages.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import backend.grid.GridPattern;
import backend.grid.ModifiableGameBoard;
import backend.util.AuthoringGameState;
import frontend.factory.worldview.layout.GridLayoutDelegate;
import frontend.factory.worldview.layout.GridLayoutDelegateFactory;
import frontend.util.BaseUIManager;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

public class WizardGrid extends BaseUIManager<Region> {
	private static final double MIN = 10, MAX = 100, SCALE = 0.750;
	private static final Paint UNCLICKED_FILL = Color.WHITE, CLICKED_FILL = Color.GREEN, BORDER = Color.BLACK;
	private static final double BORDER_WIDTH = 5.0;

	private ScrollPane pane;
	private Group group;
	private Map<Polygon, CoordinateTuple> polygons;
	private GridLayoutDelegate delegate;
	private Collection<Polygon> clickedPolygons;

	public WizardGrid(AuthoringGameState gameState) {
		initialize(gameState);
	}

	@Override
	public Region getObject() {
		return pane;
	}

	public GridPattern getGridPattern() {
		return new GridPattern("", "", "",
				clickedPolygons.stream().map(polygon -> polygons.get(polygon)).collect(Collectors.toList()));
	}

	private void initialize(AuthoringGameState gameState) {
		pane = new ScrollPane();
		group = new Group();
		pane.setContent(group);
		polygons = new HashMap<>();
		delegate = new GridLayoutDelegateFactory();
		clickedPolygons = new ArrayList<>();

		GameBoard board = new ModifiableGameBoard(gameState.getGrid().getName(), gameState.getGrid().getTemplateCell(),
				gameState.getGrid().getRows()*2, gameState.getGrid().getColumns()*2, gameState.getGrid().getBoundsHandler(),
				gameState.getGrid().getDescription(), gameState.getGrid().getImgPath()).build();

		board.getCells().keySet().stream().forEach(coordinate -> {
			Polygon polygon = delegate.layoutCell(SCALE, MIN, MAX, coordinate);
			polygon.setStroke(BORDER);
			polygon.setStrokeWidth(BORDER_WIDTH);
			polygon.setFill(UNCLICKED_FILL);
			polygon.setOnMouseClicked(event -> {
				if (polygon.getFill().equals(UNCLICKED_FILL)) {
					polygon.setFill(CLICKED_FILL);
					clickedPolygons.add(polygon);
				} else {
					polygon.setFill(UNCLICKED_FILL);
					clickedPolygons.remove(polygon);
				}
			});
			polygons.put(polygon, coordinate);
			group.getChildren().add(polygon);
			if (coordinate.euclideanDistanceTo(CoordinateTuple.getOrigin(board.dimension())) == 0) {
				polygon.setOnMouseClicked(event -> {
				});
				polygon.setFill(Color.RED);
			}
			System.out.println(coordinate);
		});
	}

}
