package frontend.factory.abilitypane;

import backend.util.Ability;
import backend.util.HasActiveAbilities;
import backend.util.HasTriggeredAbilities;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.View;
import frontend.util.VoogaEntityButton;
import frontend.util.VoogaEntityButtonFactory;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.Collection;

/**
 * @author Created by th174 on 4/21/17.
 */
public class AbilityPane extends ClickableUIComponent<ScrollPane> {
	private ScrollPane scrollPane;
	private GridPane content;
	private Button cancelButton;

	public AbilityPane(Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		content = new GridPane();
		ImageView cancelImg = new ImageView(View.getImg(getResourceBundle().getString("cancelImgPath")));
		cancelImg.setFitWidth(50);
		cancelImg.setFitHeight(50);
		cancelButton = new Button("", cancelImg);
		cancelButton.setCancelButton(true);
		cancelButton.setPadding(Insets.EMPTY);
		cancelButton.setOnMouseClicked(event -> getClickHandler().cancel());
		scrollPane = new ScrollPane(content);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setMinViewportHeight(0);
		scrollPane.setMinViewportWidth(0);
	}

	public void setContent(VoogaEntity entity) {
		content.getChildren().clear();
		content.addRow(0);
		content.addRow(1);
		content.add(cancelButton, 0, 2);
		if (entity instanceof HasActiveAbilities) {
			content.addRow(0, createRow(entity, ((HasActiveAbilities) entity).getActiveAbilities()));
		}
		if (entity instanceof HasTriggeredAbilities) {
			content.addRow(1, createRow(entity, ((HasTriggeredAbilities) entity).getTriggeredAbilities()));
		}
	}

	public void setCancelButtonEvent(EventHandler<MouseEvent> onCancelClicked) {
		cancelButton.setOnMouseClicked(onCancelClicked);
	}

	private Button[] createRow(VoogaEntity entity, Collection<? extends Ability> collection) {
		return collection.parallelStream().map(e -> VoogaEntityButtonFactory.createVoogaEntityButton(entity, e, 100, getController(), getClickHandler())).map(VoogaEntityButton::getObject).toArray(Button[]::new);
	}

	@Override
	public ScrollPane getObject() {
		return scrollPane;
	}
}
