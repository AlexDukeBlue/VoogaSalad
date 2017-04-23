package frontend.util;

import backend.util.VoogaEntity;
import frontend.ComponentClickHandler;
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
	private final String entityName;

	public VoogaEntityButton(VoogaEntity entity, int size, ComponentClickHandler clickHandler) {
		super(clickHandler);
		this.entityName = entity.getName();
		ImageView sprite = new ImageView(View.getImg(entity.getImgPath()));
		entityButton = new Button("", sprite);
		entityButton.setPadding(new Insets(5, 5, 5, 5));
		sprite.setFitWidth(size);
		sprite.setFitHeight(size);
		entityButton.setTooltip(new Tooltip(entity.getFormattedName()));
		entityButton.setPadding(Insets.EMPTY);
		entityButton.setOnMouseClicked(event -> handleClick(null));
	}

	@Override
	public Button getObject() {
		return entityButton;
	}

	protected String getEntityName() {
		return entityName;
	}
}
