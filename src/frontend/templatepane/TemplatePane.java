package frontend.templatepane;

import java.util.Collection;

import backend.cell.Terrain;
import backend.unit.Unit;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.detailpane.DetailPane;
import frontend.util.BaseUIManager;
import frontend.worldview.WorldView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * @author Faith Rodriguez
 *         Created 3/29/2017
 */

public class TemplatePane extends BaseUIManager<Region> {

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

	public TemplatePane(Collection<? extends Unit> unitTemplate) {
		// TODO Auto-generated constructor stub
	}

	private void createCollabsible(String label, Collection<? extends VoogaEntity> sprites) {
		TitledPane spritePane = new TitledPane();
		spritePane.setText(label);
		VBox content = createContent(sprites, label);
		spritePane.setContent(content);
		spritePane.setCollapsible(true);
		pane.getChildren().add(spritePane);
	}

	private VBox createContent(Collection<? extends VoogaEntity> sprites, String spriteType) {
		VBox contentPane = new VBox();
		for (VoogaEntity sprite : sprites) {
			VBox spriteContent = new VBox();
			// fix getName and getImage once communication sorted
			Label spriteName = new Label(sprite.getName());
			spriteContent.getChildren().add(spriteName);
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
		o.setOnMouseClicked(event -> detailPane.setContent(sprite, spriteType));
	}

	private void updatePane() {
		pane.getChildren().clear();
		createCollabsible("Terrain", terrains);
		createCollabsible("Unit", units);
	}

	public void updateUnits(Collection<? extends Unit> unitsIn) {
		//sprites will (I am fairly certain) contain all available sprites, not just the new ones
		units = unitsIn;
	}

	public void updateTerrains(Collection<? extends Terrain> terrainsIn) {
		terrains = terrainsIn;
	}
	
	@Override
	public void update(){
		updateTerrains(getController().getTerrainTemplates());
		updateUnits(getController().getUnitTemplates());
		updatePane();
	}

	@Override
	public Region getObject() {
		return pane;
	}

}
