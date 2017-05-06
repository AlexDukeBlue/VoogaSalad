package frontend.factory.detailpane;

import backend.cell.Cell;
import backend.cell.ModifiableTerrain;
import backend.cell.Terrain;
import backend.unit.Unit;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.interfaces.detailpane.DetailPaneExternal;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Faith Rodriguez
 *         <p>
 *         This class displays details about any sprite that the user selects
 *         from the template pane, as well as lets the user change aspects of a
 *         sprite and activate a unit or terrain's abilities.
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
	private VoogaEntity currentSprite;
	private ResourceBundle resources;
	private Button editBtn;
	private ScrollPane scrollPane;
	private Button exportButton;
	private boolean authorMode;

	private double PANE_WIDTH = 1000;

	public DetailPane(ClickHandler clickHandler, Controller controller) {
		super(controller, clickHandler);
		resources = ResourceBundle.getBundle("frontend/factory/detailpane/resources");
		paneSetup();
		setLabel();
		clearContent();
		getPolyglot().setOnLanguageChange(change -> {
			if (currentSprite != null) {
				setContent(currentSprite);
			}
		});
		setAuthorMode();
		scrollPane = new ScrollPane();
		scrollPane.setContent(fullPane);
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
	 * @param sprite
	 *            A sprite that has just been clicked on in the TemplatePane
	 */
	public void setContent(VoogaEntity sprite) {
		currentSprite = sprite;
		clearContent();
		if (Objects.nonNull(sprite)) {
			setImageContent(sprite);
			setInfoContent(sprite);
		}
	}

	/**
	 * Returns the entire detail pane region
	 */
	@Override
	public Region getNode() {
		return scrollPane;
	}

	private void setImageContent(VoogaEntity sprite) {
		Label name = new Label(sprite.getName() + "\n");
		name.setFont(Font.font(25));
		name.setMinWidth(Region.USE_PREF_SIZE + 10);
		ImageView spriteImage = new ImageView(getImg(sprite.getImgPath()));
		spriteImage.setSmooth(true);
		spriteImage.setPreserveRatio(true);
		spriteImage.setFitWidth(120);
		Tooltip.install(spriteImage, new Tooltip(sprite.getImgPath()));
		imagePane.setOnMouseClicked(event -> {
			Stage expandImageWindow = new Stage();
			ImageView expandedImage = new ImageView(getImg(sprite.getImgPath()));
			expandedImage.setSmooth(true);
			expandedImage.setPreserveRatio(true);
			expandImageWindow.setTitle(sprite.getImgPath());
			expandImageWindow.setScene(new Scene(new Group(expandedImage)));
			expandImageWindow.show();
		});
		imagePane.getChildren().add(name);
		imagePane.getChildren().add(spriteImage);
	}

	private void setInfoContent(VoogaEntity sprite) {
		addString(getPolyglot().get("Description").getValueSafe(), sprite.getDescription());
		Label newSpriteInfo;
		if (sprite instanceof Unit) {
			newSpriteInfo = new Label(setUnitContent((Unit) sprite));
		} else if (sprite instanceof Cell) {
			newSpriteInfo = new Label(setTerrainContent(((Cell) sprite).getTerrain()));
		} else if (sprite instanceof ModifiableTerrain) {
			newSpriteInfo = new Label(setTerrainContent((ModifiableTerrain) sprite));
		} else {
			newSpriteInfo = new Label(content);
		}
		exportButton = new Button(getPolyglot().get("Export").get());
		exportButton.setOnAction(e -> {
			try {
				FileChooser chooser = new FileChooser();
				chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".xml Files", "*.xml"));
				chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
				chooser.setInitialFileName(sprite.getFormattedName() + ".xml");
				File file = chooser.showSaveDialog(null);
				getController().save(sprite, Paths.get(file.getPath()));
			} catch (NullPointerException | IOException e1) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.titleProperty().bind(getPolyglot().get("NoFileSelected"));
				alert.contentTextProperty().bind(getPolyglot().get("TryAgain"));
				alert.show();
			}
		});
		infoPane.getChildren().add(exportButton);
		spriteInfo = newSpriteInfo;
		setLabel();
	}

	private String setUnitContent(Unit unit) {
		addMoveCosts(unit);
		content = addCollection(getPolyglot().get("DefensiveModifiers").getValueSafe(), unit.getDefensiveModifiers(),
				content);
		unit.getUnitStats().forEach(e -> addString(e.getName(), e.getCurrentValue().toString()));
		addString("Move Pattern", Objects.nonNull(unit.getMovePattern()) ? unit.getMovePattern().toString() : "");
		if (authorMode)
			createButton(unit, resources.getString("Unit"));
		return content;
	}

	private String setTerrainContent(Terrain terrain) {
		addString(getPolyglot().get("DefaultMoveCosts").getValueSafe(),
				((Integer) terrain.getDefaultMoveCost()).toString());
		addString(getPolyglot().get("DefenseModifiers").getValueSafe(), "\n" + terrain.getDefensiveModifiers().stream()
				.map(Object::toString).collect(Collectors.joining("\n")).replaceAll("(?m)^", "\t"));
		if (authorMode)
			createButton(terrain, "Terrain");
		return content;
	}

	private void createButton(VoogaEntity unit, String unitType) {
		if (unit instanceof Unit) {
			editBtn = new Button(getPolyglot().get("Edit").get());
			infoPane.getChildren().add(editBtn);
			editBtn.setOnMouseClicked(e -> {
				new DetailEdit(unit, unitType, getController());
			});
		}
	}

	private String addCollection(String label, Collection<? extends VoogaEntity> collection, String content) {
		content = checkForNull(label, content);
		for (VoogaEntity o : collection) {
			content += o + "\n";
			if (o.getImgPath() != null) {
				ImageView oIV = new ImageView(getImg(o.getImgPath()));
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
		infoPane.getChildren().removeAll(exportButton, spriteInfo, editBtn);
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
	public void setAuthorMode() {
		authorMode = true;
	}

	@Override
	public void setPlayMode() {
		authorMode = false;
	}
}