package frontend.factory.wizard.wizards.strategies.wizard_pages.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import backend.grid.CoordinateTuple;
import backend.grid.GridPattern;
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
		ArrayList<Integer> max = new ArrayList<>(gameState.getGrid().dimension());
		for (int i = 0; i < gameState.getGrid().dimension(); i++) {
			max.add(0);
		}
		// CoordinateTuple center = new CoordinateTuple(centerCoordinates);
		gameState.getGrid().getCells().keySet().stream().forEach(coordinate -> {
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
			if (coordinate.euclideanDistanceTo(CoordinateTuple.getOrigin(gameState.getGrid().dimension())) == 0) {
				polygon.setOnMouseClicked(event -> {
				});
				polygon.setFill(Color.RED);
			}
			// for (int i = 0; i < max.size(); i++) {
			// if (coordinate.get(i) > max.get(i)) {
			// max.set(i, coordinate.get(i));
			// }
			// }
			System.out.println(coordinate);
		});
		// for (int i = 0; i < max.size(); i++) {
		// max.set(i, max.get(i) / 2);
		// }
		// CoordinateTuple center = new CoordinateTuple(max);
		// polygons.keySet().stream().forEach(polygon -> {
		// if (polygons.get(polygon).equals(center)) {
		// polygon.setOnMouseClicked(event -> {
		// });
		// polygon.setFill(Color.RED);
		// }
		// });
	}

}
