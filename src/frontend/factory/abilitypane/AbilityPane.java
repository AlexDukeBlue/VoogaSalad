package frontend.factory.abilitypane;

import backend.unit.Unit;
import backend.util.Ability;
import backend.util.HasPassiveModifiers;
import backend.util.HasTriggeredAbilities;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.util.VoogaEntityButton;
import frontend.util.VoogaEntityButtonFactory;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.TilePane;

import java.util.Collection;

/**
 * @author Created by th174 on 4/21/17.
 *         <p>
 *         Sets the content for the Ability Pane, which allows the user to view and apply active abilities
 *         for each unit and terrain.
 */
public class AbilityPane extends ClickableUIComponent<SplitPane> {
	private final ScrollPane activeAbilities;
	private final TilePane activeAbilitiesContent;
	private final ScrollPane passiveAbilities;
	private final TilePane passiveAbilitiesContent;
	private final SplitPane abilityPane;

	public AbilityPane(Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		activeAbilitiesContent = new TilePane();
		passiveAbilitiesContent = new TilePane();
		abilityPane = new SplitPane();
		abilityPane.setOrientation(Orientation.VERTICAL);
		activeAbilities = new ScrollPane(activeAbilitiesContent);
		activeAbilities.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		activeAbilities.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		activeAbilities.setFitToWidth(true);
		activeAbilities.setFitToHeight(true);
		passiveAbilities = new ScrollPane(passiveAbilitiesContent);
		passiveAbilities.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		passiveAbilities.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		passiveAbilities.setFitToWidth(true);
		passiveAbilities.setFitToHeight(true);
	}

	/**
	 * Sets the details that should be displayed within the pane
	 *
	 * @param entity The VoogaEntity that has been selected for viewing
	 */
	public void setContent(VoogaEntity entity) {
		abilityPane.getItems().clear();
		activeAbilitiesContent.getChildren().clear();
		passiveAbilitiesContent.getChildren().clear();
		if (entity instanceof Unit) {
			activeAbilitiesContent.getChildren().addAll(createRow((Unit) entity));
			abilityPane.getItems().add(activeAbilities);
		}
		if (entity instanceof HasTriggeredAbilities) {
			passiveAbilitiesContent.getChildren().addAll(createRow(entity, ((HasTriggeredAbilities) entity).getTriggeredAbilities()));
			abilityPane.getItems().add(passiveAbilities);
		}
		if (entity instanceof HasPassiveModifiers) {
			passiveAbilitiesContent.getChildren().addAll(createRow(entity, ((HasPassiveModifiers) entity).getOffensiveModifiers()));
			passiveAbilitiesContent.getChildren().addAll(createRow(entity, ((HasPassiveModifiers) entity).getDefensiveModifiers()));
			if (!abilityPane.getItems().contains(passiveAbilities)) {
				abilityPane.getItems().add(passiveAbilities);
			}
		}
	}

	private Button[] createRow(Unit entity) {
		return entity.getActiveAbilities().parallelStream().map(e -> VoogaEntityButtonFactory.createVoogaEntityButton(entity, e, 80, getController(), getClickHandler())).map(VoogaEntityButton::getNode).toArray(Button[]::new);
	}

	private Button[] createRow(VoogaEntity entity, Collection<? extends Ability> collection) {
		return collection.parallelStream().map(e -> VoogaEntityButtonFactory.createVoogaEntityButton(entity, e, 60, getController(), getClickHandler())).map(VoogaEntityButton::getNode).toArray(Button[]::new);
	}

	@Override
	public SplitPane getNode() {
		return abilityPane;
	}
}
