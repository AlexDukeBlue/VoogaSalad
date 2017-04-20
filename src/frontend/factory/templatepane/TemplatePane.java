package frontend.factory.templatepane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import backend.cell.Terrain;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.interfaces.templatepane.TemplatePaneExternal;
import frontend.interfaces.templatepane.TemplatePaneObserver;
import frontend.util.BaseUIManager;
import frontend.wizards.UnitWizard;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * @author Faith Rodriguez Created 3/29/2017
 * 
 *         This class is responsible for creating the sidebar that contains
 *         dropdown boxes of all of the created units and terrains. These units,
 *         when clicked, appear in a Display Pane on the bottom of the screen
 *         with their relevant information and included abilities. When dragged,
 *         these sprites can be added to the game board in development board
 * 
 *         This class is dependent on the DetailPane.java class and the
 *         CellView.java classes to make the clicking and dragging features work
 */

class TemplatePane extends BaseUIManager<Region> implements TemplatePaneExternal {

	private VBox pane = new VBox();
	private Collection<? extends Unit> units;
	private Collection<? extends Terrain> terrains;
	private Collection<TemplatePaneObserver> observers;

	private Button addUnitButton;

	public TemplatePane(Controller controller) {
		super(controller);

		observers = new ArrayList<>();
		units = getController().getUnitTemplates();
		terrains = getController().getTerrainTemplates();

		createCollabsible("unit", units);
		addUnitButton();
		createCollabsible("terrain", terrains);

	}

	@Override
	public void update() {
		updateTerrains(getController().getTerrainTemplates());
		updateUnits(getController().getUnitTemplates());
		updatePane();
	}

	@Override
	public Region getObject() {
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
			wiz.addObserver(new Observer() {

				@Override
				public void update(Observable o, Object arg) {
					getController().addUnitTemplates((ModifiableUnit) arg);
					System.out.println(((ModifiableUnit) arg).getImgPath());
				}
			});
		});
		pane.getChildren().add(addUnitButton);
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
		contentPane.setPadding(new Insets(5, 5, 5, 5));
		contentPane.setSpacing(5);
		for (VoogaEntity sprite : sprites) {
			VBox spriteContent = new VBox();
			spriteContent.setBorder(new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			spriteContent.setPadding(new Insets(5, 5, 5, 5));
			// fix getName and getImage once communication sorted
			Label spriteName = new Label(sprite.getFormattedName());
			spriteContent.getChildren().add(spriteName);
			if (sprite.getImgPath() != null) {
				System.out.println(sprite.getImgPath());
				Image spriteImage = new Image(getClass().getClassLoader().getResourceAsStream(sprite.getImgPath()));
				ImageView imageNode = new ImageView(spriteImage);
				imageNode.setFitHeight(40);
				imageNode.setFitWidth(40);
				spriteContent.getChildren().add(imageNode);
			}
			setOnClick(spriteContent, sprite, spriteType);
			contentPane.getChildren().add(spriteContent);
		}
		return contentPane;
	}

	private void setOnClick(Node o, VoogaEntity sprite, String spriteType) {
		o.setOnMouseClicked(event -> {
			observers.stream().forEach(observer -> observer.didClickVoogaEntity(this, sprite));
		});
	}

	private void updatePane() {
		pane.getChildren().clear();
		createCollabsible("Unit", units);
		addUnitButton();
		createCollabsible("Terrain", terrains);
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
			observers.stream().forEach(observer -> addTemplatePaneObserver(observer));
		}
	}

	@Override
	public void removeAllTemplatePaneObservers(Collection<TemplatePaneObserver> observers) {
		if (observers != null) {
			observers.stream().forEach(observer -> removeTemplatePaneObserver(observer));
		}
	}

}
