package frontend.factory.templatepane;

import backend.util.HasShape;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.factory.wizard.WizardFactory;
import frontend.interfaces.templatepane.TemplatePaneExternal;
import frontend.util.AddRemoveButton;
import frontend.util.VoogaEntityButton;
import frontend.util.VoogaEntityButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Faith Rodriguez, Timmy Huang Created 3/29/2017
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

class TemplatePane extends ClickableUIComponent<Region> implements TemplatePaneExternal {

	private final Map<String, Collection<String>> templateNamesCache;
	private VBox pane = new VBox();
	private Map<String, VBox> contents;

	public TemplatePane(Controller controller, ClickHandler clickHandler, String... templateCategories) {
		super(controller, clickHandler);
		contents = new HashMap<>();
		templateNamesCache = new HashMap<>();
		Arrays.stream(templateCategories)
				.map(e -> new Pair<>(e, getController().getAuthoringGameState().getTemplateByCategory(e).getAll()))
				.forEach(e -> createCollabsible(e.getKey(), e.getValue()));
		update();
	}

	@Override
	public void update() {
		contents.forEach((key, value) -> {
			Collection<String> newTemplateNames = getController().getAuthoringGameState().getTemplateByCategory(key).getAll().stream().map(VoogaEntity::getName).collect(Collectors.toSet());
			if (!newTemplateNames.equals(templateNamesCache.get(key))) {
				templateNamesCache.put(key, newTemplateNames);
				value.getChildren().clear();
				createButtons(getController().getAuthoringGameState().getTemplateByCategory(key).stream().filter(e -> !(e instanceof HasShape) || ((HasShape) e).getShape().equals(getController().getShape())).collect(Collectors.toList()), key, value.getChildren());
			}
		});
	}

	private void createButtons(Collection<? extends VoogaEntity> voogaEntities, String entityType, Collection<Node> parent) {
		voogaEntities.stream().map(entity -> VoogaEntityButtonFactory.createVoogaEntityButton(entity, entityType, 70, getController(), getClickHandler()))
				.map(VoogaEntityButton::getObject).forEach(parent::add);
	}

	@Override
	public VBox getObject() {
		return pane;
	}

	private void createCollabsible(String label, Collection<? extends VoogaEntity> sprites) {
		TitledPane spritePane = new TitledPane();
		spritePane.textProperty().bind(getPolyglot().get(label));
		VBox contentPane = new VBox();
		contentPane.setPadding(Insets.EMPTY);
		contentPane.setAlignment(Pos.CENTER_RIGHT);
		contentPane.setSpacing(0);
		createButtons(sprites, label, contentPane.getChildren());
		sprites.stream()
				.map(entity -> VoogaEntityButtonFactory.createVoogaEntityButton(entity, label, 50, getController(), getClickHandler()))
				.map(VoogaEntityButton::getObject).forEach(contentPane.getChildren()::add);
		contents.put(label, contentPane);
		ScrollPane scroller = new ScrollPane();
		scroller.setContent(contents.get(label));
		AddRemoveButton addRemoveButton = new AddRemoveButton(getClickHandler());
		addRemoveButton.setOnAddClicked(e -> WizardFactory.newWizard(label, getController().getAuthoringGameState()).addObserver((o, arg) -> getController().addTemplatesByCategory(label, (VoogaEntity) arg)));
		VBox box = new VBox(scroller, addRemoveButton.getObject());
		box.setSpacing(2);
		box.setPadding(new Insets(2, 2, 2, 2));
		spritePane.setContent(box);
		spritePane.setCollapsible(true);
		spritePane.setExpanded(false);
		pane.getChildren().add(spritePane);
	}
}
