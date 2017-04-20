package frontend.worldview;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Created by th174 on 4/19/17.
 */
public class MinimapPane {
	private final ScrollPane gridView;
	private final Rectangle gridViewPortBounds;
	private final Pane view;

	public MinimapPane(ScrollPane gridView) {
		this.gridView = gridView;
		this.gridViewPortBounds = new Rectangle();
		gridViewPortBounds.setStroke(Color.WHITE);
		gridViewPortBounds.setStrokeWidth(1);
		view = new Pane(gridViewPortBounds);
		ChangeListener<Object> changeListener = (observable, oldValue, newValue) -> {
			Bounds bounds = gridView.getViewportBounds();
			int left = -1 * (int) bounds.getMinX();
			int right = left + (int) bounds.getWidth();
			System.out.println("hval:" + gridView.getHvalue() + " left:" + left + " right:" + right);
		};
		gridView.viewportBoundsProperty().addListener(changeListener);
		gridView.hvalueProperty().addListener(changeListener);
		gridView.vvalueProperty().addListener(changeListener);
	}


}
