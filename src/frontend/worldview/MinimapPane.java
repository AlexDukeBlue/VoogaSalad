package frontend.worldview;

import frontend.util.BaseUIManager;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Created by th174 on 4/19/17.
 */
public class MinimapPane extends BaseUIManager<Region> {
	private final ScrollPane gridView;
	private final Rectangle gridViewPortBounds;
	private final Pane view;

	public MinimapPane(ScrollPane gridView) {
		this.gridView = gridView;
		this.gridViewPortBounds = new Rectangle();
		gridViewPortBounds.setFill(Color.TRANSPARENT);
		gridViewPortBounds.setStroke(Color.RED);
		gridViewPortBounds.setStrokeWidth(2);
		view = new Pane(gridViewPortBounds);
		ChangeListener<Object> changeListener = (observable, oldValue, newValue) -> {
			double viewportWidth = gridView.getViewportBounds().getWidth();
			double hoffset = calcOffset(gridView.getHmin(), gridView.getHmax(), gridView.getHvalue(), gridView.getContent().getBoundsInParent().getWidth(), viewportWidth);
			double viewportHeight = gridView.getViewportBounds().getHeight();
			double voffset = calcOffset(gridView.getVmin(), gridView.getVmax(), gridView.getVvalue(), gridView.getContent().getBoundsInParent().getHeight(), viewportHeight);
			gridViewPortBounds.setX(hoffset / 10);
			gridViewPortBounds.setY(voffset / 10);
			gridViewPortBounds.setWidth(viewportWidth / 10);
			gridViewPortBounds.setHeight(viewportHeight / 10);
		};
		gridView.viewportBoundsProperty().addListener(changeListener);
		gridView.hvalueProperty().addListener(changeListener);
		gridView.vvalueProperty().addListener(changeListener);
	}

	private double calcOffset(double min, double max, double value, double contentSize, double viewportSize) {
		return Math.max(0, contentSize - viewportSize) * (value - min) / (max - min);
	}

	@Override
	public Region getObject() {
		return view;
	}
}
