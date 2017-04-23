package frontend.util;

import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.View;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;


/**
 * @author Created by th174 on 4/21/17.
 */
public abstract class VoogaEntityButton extends SelectableUIComponent<Button> {
	private final Button entityButton;
	private final VoogaEntity entity;

	public VoogaEntityButton(VoogaEntity entity, int size, Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		this.entity = entity;
		ImageView sprite = new ImageView(View.getImg(entity.getImgPath()));
		entityButton = new Button("", sprite);
		entityButton.setPadding(new Insets(5, 5, 5, 5));
		sprite.setFitWidth(size);
		sprite.setFitHeight(size);
		entityButton.setTooltip(new Tooltip(entity.getFormattedName()));
		entityButton.setPadding(Insets.EMPTY);
		entityButton.setOnMouseClicked(event -> setAsSelected());
	}

	@Override
	public Button getObject() {
		return entityButton;
	}

	protected VoogaEntity getEntity() {
		return entity;
	}
}
