package frontend.factory.worldview;

import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * @author Created by th174 on 4/19/17.
 */
public class MinimapPane extends BaseUIManager<Pane> {
	private final Rectangle gridViewPortBounds;
	private final Pane view;
	private final Node mapContent;

	private ImageView minimapSnapshot;

	public MinimapPane(ScrollPane scrollPane, Controller controller) {
		super(controller);
		this.mapContent = scrollPane.getContent();
		this.gridViewPortBounds = new Rectangle();
		view = new Pane();
		gridViewPortBounds.setFill(Color.TRANSPARENT);
		gridViewPortBounds.setStroke(Color.RED);
		gridViewPortBounds.setStrokeWidth(2);
		gridViewPortBounds.setStrokeType(StrokeType.INSIDE);
		double ratio = mapContent.getBoundsInLocal().getHeight() / mapContent.getBoundsInLocal().getWidth();
		view.setMinWidth(150);
		view.setMinHeight(150 * ratio);
		view.minHeightProperty().bind(view.widthProperty().multiply(ratio));
		view.maxHeightProperty().bind(view.widthProperty().multiply(ratio));
		minimapSnapshot = new ImageView();
		minimapSnapshot.setSmooth(true);
		minimapSnapshot.setMouseTransparent(true);
		minimapSnapshot.fitWidthProperty().bind(view.widthProperty());
		minimapSnapshot.fitHeightProperty().bind(view.heightProperty());
		minimapSnapshot.setPreserveRatio(true);
		view.getChildren().addAll(minimapSnapshot, gridViewPortBounds);
		ChangeListener<Object> changeListener = (observable, oldValue, newValue) -> {
			double viewPortWidth = scrollPane.getViewportBounds().getWidth();
			double viewPortHeight = scrollPane.getViewportBounds().getHeight();
			double contentWidth = mapContent.getBoundsInLocal().getWidth();
			double contentHeight = mapContent.getBoundsInLocal().getHeight();
			double minimapWidth = minimapSnapshot.getFitWidth();
			double minimapHeight = minimapSnapshot.getFitHeight();
			gridViewPortBounds.setWidth(Math.min(1, viewPortWidth / contentWidth) * minimapWidth);
			gridViewPortBounds.setHeight(Math.min(1, viewPortHeight / contentHeight) * minimapHeight);
			gridViewPortBounds.relocate(
					calcMinimapOffset(scrollPane.getHvalue(), scrollPane.getHmin(), scrollPane.getHmax(), contentWidth, viewPortWidth, minimapWidth),
					calcMinimapOffset(scrollPane.getVvalue(), scrollPane.getVmin(), scrollPane.getVmax(), contentHeight, viewPortHeight, minimapHeight));
		};
		scrollPane.viewportBoundsProperty().addListener(changeListener);
		mapContent.boundsInLocalProperty().addListener(changeListener);
		scrollPane.hvalueProperty().addListener(changeListener);
		scrollPane.vvalueProperty().addListener(changeListener);
		EventHandler<MouseEvent> mouseEvent = event -> {
			double viewPortWidth = scrollPane.getViewportBounds().getWidth();
			double viewPortHeight = scrollPane.getViewportBounds().getHeight();
			double contentWidth = mapContent.getBoundsInLocal().getWidth();
			double contentHeight = mapContent.getBoundsInLocal().getHeight();
			double minimapWidth = minimapSnapshot.getFitWidth();
			double minimapHeight = minimapSnapshot.getFitHeight();
			double newX = Math.max(0, event.getX() - gridViewPortBounds.getWidth() / 2.0);
			double newY = Math.max(0, event.getY() - gridViewPortBounds.getHeight() / 2.0);
			gridViewPortBounds.relocate(newX, newY);
			scrollPane.setHvalue(calcScrollOffset(newX, scrollPane.getHmin(), scrollPane.getHmax(), contentWidth, viewPortWidth, minimapWidth));
			scrollPane.setVvalue(calcScrollOffset(newY, scrollPane.getVmin(), scrollPane.getVmax(), contentHeight, viewPortHeight, minimapHeight));
		};
		view.setOnMousePressed(mouseEvent);
		view.setOnMouseDragged(mouseEvent);
		update();
	}

	@Override
	public void update() {
		mapContent.snapshot(param -> {
			minimapSnapshot.setImage(param.getImage());
			return null;
		}, null, null);
	}

	private double calcMinimapOffset(double scrollValue, double scrollMin, double scrollMax, double contentSize, double viewportSize, double minimapSize) {
		return (contentSize - viewportSize) * (scrollValue - scrollMin) / (scrollMax - scrollMin) / contentSize * minimapSize;
	}

	private double calcScrollOffset(double minimapOffset, double scrollMin, double scrollMax, double contentSize, double viewportSize, double minimapSize) {
		return minimapOffset * contentSize / minimapSize / (contentSize - viewportSize) * (scrollMax - scrollMin) + scrollMin;
	}

	@Override
	public Pane getNode() {
		return view;
	}
}