package frontend.factory.templatepane;

import backend.unit.Unit;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.View;
import frontend.interfaces.templatepane.TemplatePaneExternal;
import frontend.interfaces.templatepane.TemplatePaneObserver;
import frontend.util.BaseUIManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	private Map<String, VBox> contents;
	private Map<VBox, Integer> numberOfTemplates;
	private Collection<TemplatePaneObserver> observers;

	public TemplatePane(Controller controller) {
		super(controller);
		contents = new HashMap<>();
		numberOfTemplates = new HashMap<>();
		observers = new ArrayList<>();
		Stream.of("Units", "Terrains").map(e -> new Pair<>(e, getController().getAuthoringGameState().getTemplateByCategory(e).getAll())).forEach(e -> createCollabsible(e.getKey(), e.getValue()));
		update();
	}

	@Override
	public void update() {
		contents.forEach((key, value) -> {
			if (value.getChildren().size() != numberOfTemplates.get(value)) {
				value.getChildren().clear();
				value.getChildren().addAll(createContent(getController().getAuthoringGameState().getTemplateByCategory(key).getAll()));
				numberOfTemplates.put(value, value.getChildren().size());
			}
		});
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

	private HBox createAddRemoveButton() {
		Button addButton = new Button("+");
		Button removeButton = new Button("-");
		addButton.setAlignment(Pos.CENTER);
		removeButton.setAlignment(Pos.CENTER);
		addButton.setMinSize(20, 20);
		addButton.setMaxSize(20, 20);
		removeButton.setMinSize(20, 20);
		removeButton.setMaxSize(20, 20);
		addButton.setBorder(Border.EMPTY);
		addButton.setPadding(Insets.EMPTY);
		removeButton.setBorder(Border.EMPTY);
		removeButton.setPadding(Insets.EMPTY);
		HBox box = new HBox(addButton, removeButton);
		box.setAlignment(Pos.TOP_RIGHT);
		box.setSpacing(0);
		box.setPadding(Insets.EMPTY);
		//Need to be able to add template with XML, remove with name
		return box;
	}

//	private void addUnitButton() {
//		Button addUnitButton = new Button("add unit");
//		addUnitButton.setOnAction(e -> {
//			UnitWizard wiz = new UnitWizard(getController().getAuthoringGameState());
//			wiz.show();
//			wiz.addObserver((o, arg) -> getController().addUnitTemplates((ModifiableUnit) arg));
//		});
//		pane.getChildren().add(addUnitButton);
//	}
//
//	private void addTerrainButton() {
//		Button addTerrainButton = new Button("add terrain");
//		addTerrainButton.setOnAction(e -> {
//			TerrainWizard wiz = new TerrainWizard(getController().getAuthoringGameState());
//			wiz.show();
//			wiz.addObserver((o, arg) -> getController().addTerrainTemplates((ModifiableTerrain) arg));
//		});
//		pane.getChildren().add(addTerrainButton);
//	}

	private void createCollabsible(String label, Collection<? extends VoogaEntity> sprites) {
		TitledPane spritePane = new TitledPane();
		spritePane.setText(label);
		VBox contentPane = new VBox();
		contentPane.setPadding(Insets.EMPTY);
		contentPane.setSpacing(0);
		contentPane.getChildren().addAll(createContent(sprites));
		contents.put(label, contentPane);
		numberOfTemplates.put(contentPane, contentPane.getChildren().size());
		ScrollPane scroller = new ScrollPane();
		scroller.setContent(contents.get(label));
		VBox box = new VBox(scroller, createAddRemoveButton());
		box.setSpacing(2);
		box.setPadding(new Insets(2, 2, 2, 2));
		spritePane.setContent(box);
		spritePane.setCollapsible(true);
		spritePane.setExpanded(false);
		pane.getChildren().add(spritePane);
	}

	private Collection<VBox> createContent(Collection<? extends VoogaEntity> sprites) {
		return sprites.parallelStream().map(sprite -> {
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
			return spriteContent;
		}).collect(Collectors.toList());
	}


	private void setOnClick(Node o, VoogaEntity sprite) {
		o.setOnMouseClicked(event -> observers.forEach(observer -> observer.didClickVoogaEntity(this, sprite)));
	}

	@Override
	public void addAllTemplatePaneObservers(Collection<TemplatePaneObserver> observers) {
		if (observers != null) {
			observers.forEach(this::addTemplatePaneObserver);
		}
	}

	@Override
	public void removeAllTemplatePaneObservers(Collection<TemplatePaneObserver> observers) {
		if (observers != null) {
			observers.forEach(this::removeTemplatePaneObserver);
		}
	}

}
