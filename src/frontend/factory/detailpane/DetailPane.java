/**
 * @author Faith Rodriguez
 * Created 4/9/2017
 */
package frontend.factory.detailpane;

import backend.cell.Cell;
import backend.cell.ModifiableTerrain;
import backend.cell.Terrain;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.VoogaEntity;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.View;
import frontend.interfaces.detailpane.DetailPaneExternal;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Faith Rodriguez
 *         <p>
 *         This class displays details about the units, as well as lets the user
 *         change aspects of a sprite and activate a unit or terrain's
 *         abilities.
 *         <p>
 *         This class is dependent on TemplatePane and CellView classes for its
 *         ActionEvents to work effectively
 */
class DetailPane extends ClickableUIComponent<Region> implements DetailPaneExternal {

	private HBox fullPane = new HBox();
	private VBox infoPane = new VBox();
	private VBox AAPane = new VBox();
	private VBox imagePane = new VBox();
	private Label spriteInfo;
	private String content = "";
	private String AAContent = "";
	private VoogaEntity currentSprite;

	private double PANE_WIDTH = 1000;

	public DetailPane(ClickHandler clickHandler) {
		super(clickHandler);
		paneSetup();
		setLabel();
		clearContent();
		getPolyglot().setOnLanguageChange(change -> {
//			paneSetup();
//			setLabel();
//			clearContent();
			if (currentSprite != null) {
				setContent(currentSprite);
			}
		});
	}

	private void paneSetup() {
		fullPane.setPrefWidth(PANE_WIDTH);
		fullPane.setPadding(new Insets(5, 5, 5, 5));
		fullPane.getChildren().add(imagePane);
		fullPane.getChildren().add(infoPane);
		fullPane.getChildren().add(AAPane);
		fullPane.setMinHeight(0);
		imagePane.setPrefWidth(fullPane.getPrefWidth() / 4);
		infoPane.setPrefWidth(fullPane.getPrefWidth() / 2);
		AAPane.setPrefWidth(fullPane.getPrefWidth() / 4);
	}

	private void setLabel() {
		spriteInfo = new Label(content);
		spriteInfo.setTranslateY(25);
		infoPane.getChildren().add(spriteInfo);
		spriteInfo.setWrapText(true);
	}

	/**
	 * Updates the content of the detail pane to information relating to the
	 * VoogaEntity sprite
	 *
	 * @param sprite     A sprite that has just been clicked on in the TemplatePane
	 * @param spriteType A string revealing whether the sprite is a unit or terrain
	 */
	public void setContent(VoogaEntity sprite) {
		currentSprite = sprite;
		clearContent();
		if (Objects.nonNull(sprite)) {
			setImageContent(sprite);
			setInfoContent(sprite);
		}
	}

	@Override
	public Region getObject() {
		return fullPane;
	}

	private void setImageContent(VoogaEntity sprite) {
		Label name = new Label(sprite.getName() + "\n");
		name.setFont(Font.font(25));
		name.setMinWidth(Region.USE_PREF_SIZE + 10);
		ImageView spriteImage = new ImageView(View.getImg(sprite.getImgPath()));
		spriteImage.setSmooth(true);
		spriteImage.setFitHeight(80);
		spriteImage.setFitWidth(80);
		imagePane.getChildren().add(name);
		imagePane.getChildren().add(spriteImage);
	}

	private void setInfoContent(VoogaEntity sprite) {
		addString(getPolyglot().get("Description").getValueSafe(), sprite.getDescription());
		Label newSpriteInfo;
		if (sprite instanceof Unit) {
			newSpriteInfo = new Label(setUnitContent((Unit) sprite));
			//setAbilityPaneContent((ModifiableUnit) sprite);
		} else if (sprite instanceof Cell) {
			newSpriteInfo = new Label(setTerrainContent(((Cell) sprite).getTerrain()));
		} else if (sprite instanceof ModifiableTerrain) {
			newSpriteInfo = new Label(setTerrainContent((ModifiableTerrain) sprite));
		} else {
			newSpriteInfo = new Label(content);
		}
		spriteInfo = newSpriteInfo;
		setLabel();
	}

	private String setUnitContent(Unit unit) {
		addMoveCosts(unit);
		content = addCollection(getPolyglot().get("DefensiveModifiers").getValueSafe(), unit.getDefensiveModifiers(), content);
		unit.getUnitStats().forEach(e -> addString(e.getFormattedName(), e.getCurrentValue().toString()));
		addString("Move Pattern", unit.getMovePattern().toString());
		return content;
	}

//	private void setAbilityPaneContent(Unit unit) {
//		AAContent = addCollection(getPolyglot().get("ActiveAbilities").getValueSafe(), unit.getActiveAbilities(), AAContent);
//		Label AALabel = new Label(AAContent);
//		AAPane.getChildren().add(AALabel);
//	}

	private String setTerrainContent(Terrain terrain) {
		addString(getPolyglot().get("DefaultMoveCosts").getValueSafe(), ((Integer) terrain.getDefaultMoveCost()).toString());
		addString(getPolyglot().get("DefenseModifiers").getValueSafe(), "\n" + terrain.getDefensiveModifiers().stream().map(Object::toString).collect(Collectors.joining("\n")).replaceAll("(?m)^", "\t"));
		return content;
	}

	private String addCollection(String label, Collection<? extends VoogaEntity> collection, String content) {
		content = checkForNull(label, content);
		for (VoogaEntity o : collection) {
			content += o + "\n";
			if (o.getImgPath() != null) {
				ImageView oIV = new ImageView(View.getImg(o.getImgPath()));
				content += oIV;
			}
		}
		content += "\n" + "\n";
		return content;
	}

	private void addString(String label, String value) {
		content = checkForNull(label, content);
		content += value + "\n" + "\n";
	}

	private void addMoveCosts(Unit unit) {
		content += getPolyglot().get("TerrainMoveCosts").getValueSafe() + ": \n";
		Map<String, Integer> MC = unit.getTerrainMoveCosts();
		for (String t : MC.keySet()) {
			content += t + ": " + MC.get(t).toString() + "\n";
		}
		content += "\n";
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
}