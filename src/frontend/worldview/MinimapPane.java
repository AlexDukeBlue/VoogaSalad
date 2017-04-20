package frontend.worldview;

import frontend.util.BaseUIManager;
import frontend.worldview.grid.actual_classes.oldclasses.GridView;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Created by th174 on 4/19/17.
 */
public class MinimapPane extends BaseUIManager<Region> {
	private static final double MINIMAP_SCALE = .2;
	private final Rectangle gridViewPortBounds;
	private final Pane view;

	public MinimapPane(GridView gridView) {
		ScrollPane scrollPane = gridView.getObject();
		this.gridViewPortBounds = new Rectangle();
		gridViewPortBounds.setFill(Color.TRANSPARENT);
		gridViewPortBounds.setStroke(Color.RED);
		gridViewPortBounds.setStrokeWidth(2);
		Rectangle minimap = new Rectangle(scrollPane.getContent().getBoundsInLocal().getWidth() * MINIMAP_SCALE,
				scrollPane.getContent().getBoundsInLocal().getHeight() * MINIMAP_SCALE);
		minimap.setMouseTransparent(true);
		view = new Pane();
		view.getChildren().addAll(minimap, gridViewPortBounds);
		ChangeListener<Object> changeListener = (observable, oldValue, newValue) -> {
			double viewPortWidth = scrollPane.getViewportBounds().getWidth();
			double viewPortHeight = scrollPane.getViewportBounds().getHeight();
			double contentWidth = scrollPane.getContent().getBoundsInLocal().getWidth();
			double contentHeight = scrollPane.getContent().getBoundsInLocal().getHeight();
			double minimapWidth = minimap.getWidth();
			double minimapHeight = minimap.getHeight();
			gridViewPortBounds.setWidth(Math.min(1, viewPortWidth / contentWidth) * minimapWidth);
			gridViewPortBounds.setHeight(Math.min(1, viewPortHeight / contentHeight) * minimapHeight);
			gridViewPortBounds.relocate(
					calcMinimapOffset(scrollPane.getHvalue(), scrollPane.getHmin(), scrollPane.getHmax(), contentWidth,
							viewPortWidth, minimapWidth),
					calcMinimapOffset(scrollPane.getVvalue(), scrollPane.getVmin(), scrollPane.getVmax(), contentHeight,
							viewPortHeight, minimapHeight));
		};
		scrollPane.viewportBoundsProperty().addListener(changeListener);
		scrollPane.getContent().boundsInLocalProperty().addListener(changeListener);
		scrollPane.hvalueProperty().addListener(changeListener);
		scrollPane.vvalueProperty().addListener(changeListener);
		EventHandler<MouseEvent> mouseEvent = event -> {
			double viewPortWidth = scrollPane.getViewportBounds().getWidth();
			double viewPortHeight = scrollPane.getViewportBounds().getHeight();
			double contentWidth = scrollPane.getContent().getBoundsInLocal().getWidth();
			double contentHeight = scrollPane.getContent().getBoundsInLocal().getHeight();
			double minimapWidth = minimap.getWidth();
			double minimapHeight = minimap.getHeight();
			double newX = Math.max(0, event.getX() - gridViewPortBounds.getWidth() / 2.0);
			double newY = Math.max(0, event.getY() - gridViewPortBounds.getHeight() / 2.0);
			gridViewPortBounds.relocate(newX, newY);
			scrollPane.setHvalue(calcScrollOffset(newX, scrollPane.getHmin(), scrollPane.getHmax(), contentWidth,
					viewPortWidth, minimapWidth));
			scrollPane.setVvalue(calcScrollOffset(newY, scrollPane.getVmin(), scrollPane.getVmax(), contentHeight,
					viewPortHeight, minimapHeight));
		};
		view.setOnMousePressed(mouseEvent);
		view.setOnMouseDragged(mouseEvent);
	}

	private double calcMinimapOffset(double scrollValue, double scrollMin, double scrollMax, double contentSize,
			double viewportSize, double minimapSize) {
		return (contentSize - viewportSize) * (scrollValue - scrollMin) / (scrollMax - scrollMin) / contentSize
				* minimapSize;
	}

	private double calcScrollOffset(double minimapOffset, double scrollMin, double scrollMax, double contentSize,
			double viewportSize, double minimapSize) {
		return minimapOffset * contentSize / minimapSize / (contentSize - viewportSize) * (scrollMax - scrollMin)
				+ scrollMin;
	}

	@Override
	public Region getObject() {
		return view;
	}
}
