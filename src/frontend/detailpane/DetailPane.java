/**
 * 
 * 
 * @author Faith Rodriguez
 * Created 4/9/2017
 */
package frontend.detailpane;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import backend.cell.ModifiableTerrain;
import backend.cell.Terrain;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.VoogaEntity;
import frontend.util.BaseUIManager;
import frontend.worldview.WorldView;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * 
 * @author Faith Rodriguez
 * 
 *         This class displays details about the units, as well as lets the user
 *         change aspects of a sprite and activate a unit or terrain's
 *         abilities.
 * 
 *         This class is dependent on TemplatePane and CellView classes for its
 *         ActionEvents to work effectively
 *
 */
public class DetailPane extends BaseUIManager<Region> {

	private HBox fullPane = new HBox();
	private VBox infoPane = new VBox();
	private VBox AAPane = new VBox();
	private VBox imagePane = new VBox();
	private Label spriteInfo;
	private String content = "";
	private String AAContent = "";
	private WorldView worldView;
	
	private double PANE_WIDTH = 1000;
	

	public DetailPane(WorldView worldView) {
		this.worldView = worldView;
		paneSetup();	
		setLabel();
		clearContent();

	}
	
	private void paneSetup() {
		fullPane.setPrefWidth(PANE_WIDTH);
		fullPane.setPadding(new Insets(5, 5, 5, 5));
		fullPane.getChildren().add(imagePane);
		fullPane.getChildren().add(infoPane);
		fullPane.getChildren().add(AAPane);
		imagePane.setPrefWidth(fullPane.getPrefWidth()/4);
		infoPane.setPrefWidth(fullPane.getPrefWidth()/2);
		AAPane.setPrefWidth(fullPane.getPrefWidth()/4);
	}
	
	private void setLabel() {
		spriteInfo = new Label(content);
		infoPane.getChildren().add(spriteInfo);
		spriteInfo.setWrapText(true);
	}
	
	/**
	 * Updates the content of the detail pane to information relating to the
	 * VoogaEntity sprite
	 * 
	 * @param sprite
	 *            A sprite that has just been clicked on in the TemplatePane
	 * @param spriteType
	 *            A string revealing whether the sprite is a unit or terrain
	 */
	public void setContent(VoogaEntity sprite, String spriteType) {
		clearContent();
		setImageContent(sprite);
		setInfoContent(sprite, spriteType);
	}
	
	private void setImageContent(VoogaEntity sprite) {
		Text name = new Text(sprite.getName() + "\n");
		name.setFont(Font.font(20));
		Image image = new Image(sprite.getImgPath());
		ImageView spriteImage = new ImageView(image);
		spriteImage.setFitHeight(50);
		spriteImage.setFitWidth(50);
		imagePane.getChildren().add(name);
		imagePane.getChildren().add(spriteImage);		
	}
	
	private void setInfoContent(VoogaEntity sprite, String spriteType) {
		addString("Description", sprite.getDescription());
		Label newSpriteInfo;
		if (sprite instanceof Unit) {
			newSpriteInfo = new Label(setUnitContent((ModifiableUnit) sprite));	
			setActiveAbilititesContent((ModifiableUnit) sprite);
		}
		else {
			newSpriteInfo = new Label(setTerrainContent((ModifiableTerrain) sprite));
		}
		spriteInfo = newSpriteInfo;
		setLabel();
	}
	
	private String setUnitContent(ModifiableUnit unit) {
		addMoveCosts(unit);
		content = addCollection("DefensiveModifiers", unit.getDefensiveModifiers(), content);
		addString("Hit Points", unit.getHitPoints().toString());
		addString("Move Points", unit.getMovePoints().toString());
		addString("Move Pattern", unit.getMovePattern().toString());
		return content;
	}

	private void setActiveAbilititesContent(ModifiableUnit unit) {
		AAContent = addCollection("Active Abilities", unit.getActiveAbilities(), AAContent);
		Label AALabel = new Label(AAContent);
		AAPane.getChildren().add(AALabel);
		
	}
	
	private String setTerrainContent(ModifiableTerrain terrain) {
		addString("Default Move Cost", ((Integer) terrain.getDefaultMoveCost()).toString());
		addString("Default Defense Modifier", ((Integer) terrain.getDefaultMoveCost()).toString());
		return content;
	}
	
	private String addCollection(String label, Collection<? extends VoogaEntity> collection, String content) {
		content = checkForNull(label,content);
		for (VoogaEntity o : collection) {
		content += o + "\n";
					if (o.getImgPath() != null) {
						Image oImage = new Image(o.getImgPath());
						ImageView oIV = new ImageView(oImage);
						content += oIV;
					}
		}
		content += "\n";
		return content;
	}
	
	private void addString(String label, String value) {
		content = checkForNull(label,content);
		content += value + "\n";
	}

	private void addMoveCosts(ModifiableUnit unit) {
		content += "Terrain Move Costs: \n";
		Map<Terrain, Integer> MC = unit.getTerrainMoveCosts();
		for (VoogaEntity t : MC.keySet()) {
			content = content + t.getName() + ": " + MC.get(t).toString() + "\n";
		}
	}

	private void clearContent() {
		content = "";
		AAContent = "";
		infoPane.getChildren().remove(spriteInfo);
		AAPane.getChildren().clear();
		imagePane.getChildren().clear();
	}
	
	private String checkForNull(String label, String content) {
		if (label != null) {
			content += label + ": "; 
		}
		return content;
	}

	@Override
	public Region getObject() {
		return fullPane;
	}

}