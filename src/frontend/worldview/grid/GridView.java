package frontend.worldview.grid;

import java.util.ArrayList;
import java.util.Collection;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.AuthoringGameState;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.View;
import frontend.util.BaseUIManager;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import util.net.Modifier;

/**
 * Holds a grid to be displayed in the development and player GUI inside a
 * ScrollPane. It can have Sprites added to a particular cell or have all cells
 * updated after something occurs in the game. Contains a group of CellView
 * objects and a collection of CellView's, when update method is called, it can
 * tell all of the units/cells to update themselves
 *
 * @author Andreas Santos Created 3/29/2017
 */

public class GridView extends BaseUIManager<Region> implements UnitViewDelegate {
	private static final double MIN = 10, MAX = 100, SCALE = 0.750;

	private ScrollPane myScrollPane;
	private Pane cellViewObjects;
	private LayoutManager myLayoutManager;
	private Collection<CellView> cellViews;
	private Unit unitToArrive;
	private boolean shouldCopy = true;

	public GridView(Controller controller) {
		setController(controller);
		initialize();
		update();
	}

	private void initialize() {
		myScrollPane = new ScrollPane();
		myScrollPane.setOnZoom(new EventHandler<ZoomEvent>() {

			@Override
			public void handle(ZoomEvent event) {
				Node e = myScrollPane.getContent();
				if (e.getScaleX() < 1.5) {
					e.setScaleX(e.getScaleX() * event.getZoomFactor());
					e.setScaleY(e.getScaleY() * event.getZoomFactor());
				} else {
					e.setScaleX(1.1);
					e.setScaleY(1.1);
				}
				;
			}

		});
		cellViewObjects = new Pane();
		cellViews = new ArrayList<>();
		myLayoutManager = new LayoutManagerFactory();
		populateCellViews();
		myScrollPane.setContent(cellViewObjects);
	}

	@Override
	public Region getObject() {
		return myScrollPane;
	}

	private void populateCellViews() {
		cellViewObjects.setBackground(new Background(
				new BackgroundFill(new ImagePattern(View.getImg(getController().getGrid().getImgPath())), null, null)));
		getController().getGrid().getCells().values().forEach(cell -> {
			CellView cl = new CellView(cell, getController(), this);
			myLayoutManager.layoutCell(cl, SCALE, MIN, MAX);
			cl.update();
			cellViews.add(cl);
			cellViewObjects.getChildren().add(cl.getObject());

			cl.getPolygon().setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if(event.getButton().equals(MouseButton.PRIMARY)) cellClicked(cl);
				}
			});
		});
	}

	public void setTemplateEntityToAdd(VoogaEntity template) {
		if (template instanceof Unit) {
			unitToArrive = (Unit) template;
			shouldCopy = true;
		}
	}

	private void cellClicked(CellView cell) {
		if (unitToArrive != null) {
			CoordinateTuple tuple = cell.getCoordinateTuple();
			Unit unitToArrive;
			if (shouldCopy) {
				unitToArrive = this.unitToArrive.copy();
			} else {
				unitToArrive = this.unitToArrive;
			}
			boolean shouldCopy = this.shouldCopy;
			Modifier<? extends AuthoringGameState> modifier = gameState -> {
				if (shouldCopy) {
					gameState.getGrid().get(tuple).addOccupants(unitToArrive);
					((ModifiableUnit) unitToArrive).setCurrentCell(gameState.getGrid().get(tuple));
				} else {
					Cell previous = unitToArrive.getCurrentCell();
					unitToArrive.moveTo(gameState.getGrid().get(tuple), gameState);
					Cell current = unitToArrive.getCurrentCell();
					System.out.println("Previous: " + previous + "\n" + previous.getOccupants().size());
					System.out.println("Current: " + current + "\n" + current.getOccupants().size());
				}
				return gameState;
			};
			getController().sendModifier(modifier);
			System.out.println(getController().getGrid());
		}
	}

	@Override
	public void unitClicked(UnitView unitView) {
		unitToArrive = unitView.getUnit();
		shouldCopy = false;
	}

}
