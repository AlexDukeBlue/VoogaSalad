package frontend.factory.templatepane;

import backend.util.HasLocation;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ComponentClickHandler;
import frontend.factory.wizard.WizardFactory;
import frontend.interfaces.templatepane.TemplatePaneExternal;
import frontend.interfaces.templatepane.TemplatePaneObserver;
import frontend.util.AddRemoveButton;
import frontend.util.BaseUIManager;
import frontend.util.VoogaEntityButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

	public TemplatePane(Controller controller, ComponentClickHandler clickHandler) {
		super(controller, clickHandler);
		contents = new HashMap<>();
		Stream.of("Units", "Terrains")
				.map(e -> new Pair<>(e, getController().getAuthoringGameState().getTemplateByCategory(e).getAll()))
				.forEach(e -> createCollabsible(e.getKey(), (Collection<? extends HasLocation>) e.getValue()));
		update();
	}

	/**
	 * @author th174
	 */
	@Override
	public void update() {
		contents.forEach((key, value) -> {
			if (value.getChildren().size() != getController().getAuthoringGameState().getTemplateByCategory(key)
					.size()) {
				value.getChildren().clear();
				getController().getAuthoringGameState().getTemplateByCategory(key).stream()
						.filter(HasLocation.class::isInstance)
						.map(HasLocation.class::cast)
						.map(entity -> new TemplateButton(entity, key, 50, getClickHandler()))
						.map(VoogaEntityButton::getObject).forEach(value.getChildren()::add);
			}
		});
	}

	@Override
	public VBox getObject() {
		return pane;
	}

	private void createCollabsible(String label, Collection<? extends HasLocation> sprites) {
		TitledPane spritePane = new TitledPane();
		spritePane.setText(label);
		VBox contentPane = new VBox();
		contentPane.setPadding(Insets.EMPTY);
		contentPane.setAlignment(Pos.CENTER_RIGHT);
		contentPane.setSpacing(0);
		sprites.stream()
				.map(entity -> new TemplateButton(entity, label, 50, getClickHandler()))
				.map(VoogaEntityButton::getObject).forEach(contentPane.getChildren()::add);
		contents.put(label, contentPane);
		ScrollPane scroller = new ScrollPane();
		scroller.setContent(contents.get(label));
		AddRemoveButton addRemoveButton = new AddRemoveButton();
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
