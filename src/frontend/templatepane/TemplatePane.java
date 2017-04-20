package frontend.templatepane;

import backend.cell.Terrain;
import backend.unit.Unit;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.detailpane.DetailPane;
import frontend.util.BaseUIManager;
import frontend.worldview.WorldView;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Collection;

/**
 * @author Faith Rodriguez
 *         Created 3/29/2017
 *         
 *         This class is responsible for creating the sidebar that contains dropdown boxes of all of the created
 *         units and terrains.  These units, when clicked, appear in a Display Pane on the bottom of the screen 
 *         with their relevant information and included abilities.
 *         When dragged, these sprites can be added to the game board in development board
 *         
 *         This class is dependent on the DetailPane.java class and the CellView.java classes to make the clicking
 *         and dragging features work
 */

public class TemplatePane extends BaseUIManager<VBox> {

	VBox pane = new VBox();
	Collection<? extends Unit> units;
	Collection<? extends Terrain> terrains;
	DetailPane detailPane;
	WorldView worldView;
	
	public TemplatePane(DetailPane detailPaneIn, WorldView worldViewIn, Controller controller) {
		super(controller);
		//units = (Collection<? extends Unit>) getController().getAuthoringGameState().getTemplateByCategory(AuthoringGameState.UNIT).getAll();
		//terrains = (Collection<? extends Terrain>) getController().getAuthoringGameState().getTemplateByCategory(AuthoringGameState.TERRAIN).getAll();
		units = getController().getUnitTemplates();
		terrains = getController().getTerrainTemplates();
		detailPane = detailPaneIn;
		worldView = worldViewIn;

		createCollabsible("unit", units);
		createCollabsible("terrain", terrains);

	}

	private void createCollabsible(String label, Collection<? extends VoogaEntity> sprites) {
		TitledPane spritePane = new TitledPane();
		spritePane.setText(label);
		VBox content = createContent(sprites, label);
		ScrollPane scroller = new ScrollPane();
		scroller.setContent(content);
		spritePane.setContent(scroller);
		spritePane.setCollapsible(true);
		spritePane.setExpanded(false);
		pane.getChildren().add(spritePane);
	}

	private VBox createContent(Collection<? extends VoogaEntity> sprites, String spriteType) {
		VBox contentPane = new VBox();
		for (VoogaEntity sprite : sprites) {
			VBox spriteContent = new VBox();
			spriteContent.setBorder(new Border(new BorderStroke(Color.BLACK, 
		            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			spriteContent.setPadding(new Insets(5, 5, 5, 5));
			// fix getName and getImage once communication sorted
			Label spriteName = new Label(sprite.getFormattedName());
			spriteContent.getChildren().add(spriteName);
			if (sprite.getImgPath() != null) {
				Image spriteImage = new Image(getClass().getClassLoader().getResourceAsStream(sprite.getImgPath()));
				ImageView imageNode = new ImageView(spriteImage);
				imageNode.setFitHeight(40);
				imageNode.setFitWidth(40);
				spriteContent.getChildren().add(imageNode);
			}
			setOnDrag(spriteContent, sprite, spriteType);
			setOnClick(spriteContent, sprite, spriteType);
			contentPane.getChildren().add(spriteContent);
		}
		return contentPane;
	}

	private void setOnDrag(Node o, VoogaEntity sprite, String spriteType) {
		//ImageView spriteImage = new ImageView(getImage(o));
		o.setOnDragDetected(event -> {
			//	               worldView.addSprite(sprite, spriteType);
		});
	}

	private void setOnClick(Node o, VoogaEntity sprite, String spriteType) {
		o.setOnMouseClicked(event -> {
			setChanged();
			notifyObservers(sprite);
			clearChanged();
		}
//		detailPane.setContent(sprite, spriteType)
		);
	}

	private void updatePane() {
		pane.getChildren().clear();
		createCollabsible("Terrain", terrains);
		createCollabsible("Unit", units);
	}

	private void updateUnits(Collection<? extends Unit> unitsIn) {
		//sprites will (I am fairly certain) contain all available sprites, not just the new ones
		units = unitsIn;
	}


	private void updateTerrains(Collection<? extends Terrain> terrainsIn) {
		terrains = terrainsIn;
	}

	public void updateTemplatePane(){
		updateTerrains(getController().getTerrainTemplates());
		updateUnits(getController().getUnitTemplates());
		updatePane();
	}

	@Override
	public VBox getObject() {
		return pane;
	}

}
