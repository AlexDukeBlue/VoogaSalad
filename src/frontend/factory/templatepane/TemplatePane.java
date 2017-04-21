package frontend.factory.templatepane;

import backend.cell.ModifiableTerrain;
import backend.cell.Terrain;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.View;
import frontend.factory.worldview.MinimapPane;
import frontend.interfaces.templatepane.TemplatePaneExternal;
import frontend.interfaces.templatepane.TemplatePaneObserver;
import frontend.util.BaseUIManager;
import frontend.wizards.TerrainWizard;
import frontend.wizards.UnitWizard;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Faith Rodriguez Created 3/29/2017
 *         <p>
 *         This class is responsible for creating the sidebar that contains
 *         dropdown boxes of all of the created units and terrains. These units,
 *         when clicked, appear in a Display Pane on the bottom of the screen
 *         with their relevant information and included abilities. When dragged,
 *         these sprites can be added to the game board in development board
 *         <p>
 *         This class is dependent on the DetailPane.java class and the
 *         CellView.java classes to make the clicking and dragging features work
 */

class TemplatePane extends BaseUIManager<Region> implements TemplatePaneExternal {

	private VBox pane = new VBox();
	private Collection<? extends Unit> units;
	private Collection<? extends Terrain> terrains;
	private Collection<TemplatePaneObserver> observers;
	private MinimapPane mapPane;

	private Button addUnitButton;
	private Button addTerrainButton;

	public TemplatePane(Controller controller, MinimapPane mapPane) {
		super(controller);
		this.mapPane = mapPane;
		observers = new ArrayList<>();
		units = getController().getUnitTemplates();
		terrains = getController().getTerrainTemplates();

		createCollabsible("unit", units);
		addUnitButton();
		createCollabsible("terrain", terrains);
		update();
	}

	@Override
	public void update() {
		updateTerrains(getController().getTerrainTemplates());
		updateUnits(getController().getUnitTemplates());
		updatePane();
	}

	@Override
	public VBox getObject() {
		return pane;
	}

	@Override
	public void addTemplatePaneObserver(TemplatePaneObserver observer) {
		if (!observers.contains(observer) && observer != null) {
			observers.add(observer);
		}
	}

	@Override
	public void removeTemplatePaneObserver(TemplatePaneObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	public TemplatePane(Collection<? extends Unit> unitTemplate) {
		// TODO Auto-generated constructor stub
	}

	private void addUnitButton() {
		addUnitButton = new Button("add unit");
		addUnitButton.setOnAction(e -> {
			UnitWizard wiz = new UnitWizard(getController().getAuthoringGameState());
			wiz.show();
			wiz.addObserver((o, arg) -> getController().addUnitTemplates((ModifiableUnit) arg));
		});
		pane.getChildren().add(addUnitButton);
	}

	private void addTerrainButton() {
		addTerrainButton = new Button("add terrain");
		addTerrainButton.setOnAction(e -> {
			TerrainWizard wiz = new TerrainWizard(getController().getAuthoringGameState());
			wiz.show();
			wiz.addObserver((o, arg) -> getController().addTerrainTemplates((ModifiableTerrain) arg));
		});
		pane.getChildren().add(addTerrainButton);
	}
	
	private void createCollabsible(String label, Collection<? extends VoogaEntity> sprites) {
		TitledPane spritePane = new TitledPane();
		spritePane.setText(label);
		VBox content = createContent(sprites);
		ScrollPane scroller = new ScrollPane();
		scroller.setContent(content);
		spritePane.setContent(scroller);
		spritePane.setCollapsible(true);
		spritePane.setExpanded(false);
		pane.getChildren().add(spritePane);
	}

	private VBox createContent(Collection<? extends VoogaEntity> sprites) {
		VBox contentPane = new VBox();
		contentPane.setPadding(new Insets(5, 5, 5, 5));
		contentPane.setSpacing(5);
		for (VoogaEntity sprite : sprites) {
			VBox spriteContent = new VBox();
			spriteContent.setBorder(new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			spriteContent.setPadding(new Insets(5, 5, 5, 5));
			Label spriteName = new Label(sprite.getFormattedName());
			spriteContent.getChildren().add(spriteName);
			if (sprite.getImgPath() != null) {
				ImageView imageNode = new ImageView(View.getImg(sprite.getImgPath()));
				imageNode.setFitHeight(40);
				imageNode.setFitWidth(40);
				spriteContent.getChildren().add(imageNode);
			}
			setOnClick(spriteContent, sprite);
			contentPane.getChildren().add(spriteContent);
		}
		return contentPane;
	}

	private void setOnClick(Node o, VoogaEntity sprite) {
		o.setOnMouseClicked(event -> observers.forEach(observer -> observer.didClickVoogaEntity(this, sprite)));
	}

	private void updatePane() {
		pane.getChildren().clear();
		pane.getChildren().add(mapPane.getObject());
		createCollabsible("Unit", units);
		addUnitButton();
		createCollabsible("Terrain", terrains);
		addTerrainButton();
	}

	private void updateUnits(Collection<? extends Unit> unitsIn) {
		units = unitsIn;
	}

	private void updateTerrains(Collection<? extends Terrain> terrainsIn) {
		terrains = terrainsIn;
	}

	@Override
	public void addAllTemplatePaneObservers(Collection<TemplatePaneObserver> observers) {
		if (observers != null) {
			observers.forEach(this::addTemplatePraneObserver);
		}
	}

	@Override
	public void removeAllTemplatePaneObservers(Collection<TemplatePaneObserver> observers) {
		if (observers != null) {
			observers.forEach(this::removeTemplatePaneObserver);
		}
	}

}
