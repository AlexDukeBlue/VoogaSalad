/**
 * A cell within the frontend GridDisplay.
 */
package frontend.worldview.grid;

import java.util.function.Consumer;

import backend.cell.Cell;
import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.util.GameplayState;
import backend.util.ReadonlyGameplayState;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import util.net.Modifier;

/**
 * A Cell object is an immovable object on which Terrains and Units can be
 * placed.
 * 
 * The CellView is the UI representation of the backend Cell class. It allows
 * the user to interact with the Cell by dragging and dropping Units, Abilities,
 * or Terrains onto it, as well as selecting the items on it.
 * 
 * The CellView extends BaseUIManager so that it can change css stylesheets,
 * languages, and be updated by the controller.
 * 
 * @author Dylan Peters
 */
public class CellView extends BaseUIManager<Parent> {

	Cell cellModel;
	Polygon polygon;
	Group group;

	/**
	 * Creates a new CellView instance. Sets all values to default.
	 * 
	 * @param cellModel
	 *            The Cell object that this CellView will visually represent.
	 * @param controller
	 *            the controller object that this CellView will send information
	 *            to when the user interacts with the CellView
	 */
	public CellView(Cell cellModel, Controller<ReadonlyGameplayState> controller) {
		setController(controller);
		this.cellModel = cellModel;
		polygon = new Polygon();
		group = new Group();
		group.translateXProperty().bind(polygon.translateXProperty());
		group.translateYProperty().bind(polygon.translateYProperty());

		update(cellModel);
	}

	/**
	 * Updates the CellView to reflect changes made to the Game State.
	 */
	@Override
	public void update() {
		update(getController().getAuthoringGameState().getGrid().get(cellModel.getLocation()));
	}

	/**
	 * Sets the action that is performed when a cell is clicked.
	 * 
	 * @param consumer
	 *            consumer to execute when the cell is clicked
	 */
	public void setOnCellClick(Consumer<CellView> consumer) {
		polygon.setOnMouseClicked(event -> consumer.accept(this));
	}

	/**
	 * Adds a copy of the Sprite to the cell and sends the request to the
	 * controller.
	 * 
	 * @param sprite
	 *            sprite to copy and add to the cell
	 */
	public void add(VoogaEntity sprite) {
		Modifier<? extends GameplayState> toSend = game -> {
			game.getGrid().get(cellModel.getLocation()).arrive((Unit) sprite.copy(), game);
			return game;
		};
		getController().sendModifier(toSend);
	}

	/**
	 * Returns an object that can be displayed to the user to show the Cell
	 */
	@Override
	public Parent getObject() {
		return group;
	}

	/**
	 * Returns the coordinateTuple at which the CellView is displayed
	 * 
	 * @return DisplayCoordinates at which the CellView is displayed.
	 */
	CoordinateTuple getCoordinateTuple() {
		return cellModel.getLocation();
	}
	
	
	/**
	 * sets the x position of a polygon
	 * @param x
	 */
	public void setX(double x){
		polygon.setLayoutX(x);
	}
	
	/**
	 * sets the y position of a polygon
	 * @param y
	 */
	public void setY(double y){
		polygon.setLayoutY(y);
	}
	

	/**
	 * returns the polygon that serves as the shape of the cell
	 * @return polygon
	 */
	public Polygon getPolygon(){
		return polygon;
	}
	
	/**
	 * sets the group to contain a different polygon
	 * @param cellModel
	 * an instance of a cell
	 */
	public void setPolygon(Polygon polygon){
		if (group.getChildren().contains(polygon)){
			group.getChildren().remove(polygon);
		}
		this.polygon = polygon;
		update(cellModel);
	}
	
	/**
	 * passes a cell's location to the controller for the backend to use, and determine its validity
	 */
	public void update(){
		update(getController().getAuthoringGameState().getGrid().get(cellModel.getLocation()));
	}
	
	/**
	 * sets a cell's visual shape to a polygon as described in the following method
	 * @param cellModel
	 * an instance of a cell
	 */
	public void update(Cell cellModel){
		this.cellModel = cellModel;
		group.getChildren().clear();
		Image polygonImage = new Image(cellModel.getTerrain().getImgPath());
		Paint polygonFill = new ImagePattern(polygonImage);
		polygon.setFill(polygonFill);
		polygon.setStrokeWidth(10);
		group.getChildren().add(polygon);
		cellModel.getOccupants().stream().forEach(unit -> {
			ImageView imageView = new ImageView(new Image(unit.getImgPath()));
			imageView.setFitWidth(75);
			imageView.setFitHeight(75);
			if (polygon.getPoints().size() >= 2) {
				imageView.setX(polygon.getPoints().get(0));
				imageView.setY(polygon.getPoints().get(1));
			}
			group.getChildren().add(imageView);
		});

	}

	
	/**
	 * sets an action event when a cell is clicked on
	 */
	public void setOnCellClick(Consumer<CellView> consumer){
		polygon.setOnMouseClicked(event -> consumer.accept(this));
	}
	
	/**
	 * Adds a new copy of a sprite to the gamestate by sending controller request
	 * @param sprite
	 * either a terrain or unit
	 */
	public void add(VoogaEntity sprite){
		Modifier<GameplayState> toSend = game -> {
			game.getGrid().get(cellModel.getLocation()).arrive((Unit) sprite.copy(), game);
			return game;
		};
		getController().sendModifier(toSend);
	}
	
	@Override
	public Parent getObject() {
		return group;
	}
	
}