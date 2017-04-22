package frontend.util;

import backend.util.VoogaEntity;
import frontend.View;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author Created by th174 on 4/21/17.
 */
public class VoogaEntityButton extends BaseUIManager<Button> {
	private final Button entityButton;

	public VoogaEntityButton(VoogaEntity entity, int size, EventHandler<MouseEvent> onMouseClick) {
		ImageView sprite = new ImageView(View.getImg(entity.getImgPath()));
		entityButton = new Button("", sprite);
		entityButton.setPadding(new Insets(5, 5, 5, 5));
		sprite.setFitWidth(size);
		sprite.setFitHeight(size);
		entityButton.setTooltip(new Tooltip(entity.getFormattedName()));
		entityButton.setPadding(Insets.EMPTY);
		entityButton.setOnMouseClicked(onMouseClick);
	}

	@Override
	public Button getObject() {
		return entityButton;
	}

}
