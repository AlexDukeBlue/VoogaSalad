package frontend.worldview.grid;

import controller.Controller;
import frontend.View;
import frontend.util.BaseUIManager;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;


/**
 * Holds a grid to be displayed in the development and player GUI inside a
 * ScrollPane. It can have Sprites added to a particular cell or have all cells
 * updated after something occurs in the game. Contains a group of CellView
 * objects and a collection of CellView's, when update method is called, it can
 * tell all of the units/cells to update themselves
 *
 * @author Andreas Santos Created 3/29/2017
 */
public class GridView extends BaseUIManager<ScrollPane> {
	private static final double MIN = 10, MAX = 100, SCALE = 0.750;
	private ScrollPane myScrollPane;
	private Pane cellViewObjects;

	public GridView(Controller controller) {
		super(controller);
		initialize();
		update();
	}

	@Override
	public void update() {

	}

	private void initialize() {
		myScrollPane = new ScrollPane();
		cellViewObjects = new Pane();
		Group zoomGroup = new Group(cellViewObjects);
		myScrollPane.setOnZoom(event -> {
			cellViewObjects.setScaleX(cellViewObjects.getScaleX() * event.getZoomFactor());
			cellViewObjects.setScaleY(cellViewObjects.getScaleY() * event.getZoomFactor());
		});
		myScrollPane.addEventFilter(ScrollEvent.ANY, event -> {
			if (event.isShortcutDown()) {
				cellViewObjects.setScaleX(cellViewObjects.getScaleX() + event.getDeltaY() / 500);
				cellViewObjects.setScaleY(cellViewObjects.getScaleY() + event.getDeltaY() / 500);
				event.consume();
			}
		});
		populateCellViews();
		myScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		myScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		myScrollPane.setPannable(true);
		myScrollPane.setContent(zoomGroup);
	}

	@Override
	public ScrollPane getObject() {
		return myScrollPane;
	}

	private void populateCellViews() {
		cellViewObjects.setBackground(new Background(new BackgroundFill(new ImagePattern(View.getImg(getController().getGrid().getImgPath())), null, null)));
		getController().getGrid().getCells().keySet().forEach(coordinate -> {
			CellView cl = new CellView(coordinate, getController());
			cl.update();
			cellViewObjects.getChildren().add(cl.getObject());
		});
	}
}