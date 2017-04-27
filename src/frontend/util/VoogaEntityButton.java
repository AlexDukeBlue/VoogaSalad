package frontend.util;

import backend.cell.Terrain;
import backend.unit.Unit;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.View;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;


/**
 * @author Created by th174 on 4/21/17.
 */
public class VoogaEntityButton extends SelectableUIComponent<Button> {
	private final Button entityButton;
	private VoogaEntity entity;

	public VoogaEntityButton(VoogaEntity entity, int size, Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		this.entity = entity;
		ImageView sprite = new ImageView(View.getImg(entity.getImgPath()));
		sprite.setSmooth(true);
		sprite.setPreserveRatio(true);
		entityButton = new Button("", sprite);
		entityButton.setPadding(new Insets(10, 10, 10, 10));
		sprite.setFitWidth(size);
		sprite.setFitHeight(size);
		entityButton.setPrefSize(size,size);
		entityButton.setTooltip(new Tooltip(entity.getFormattedName()));
		entityButton.setPadding(Insets.EMPTY);
		entityButton.setOnMouseClicked(event -> setAsSelected());
		entityButton.setOnDragDetected(event -> {
			getObject().startFullDrag();
			if(entity instanceof Unit || entity instanceof Terrain){
				getObject().setCursor(new ImageCursor(sprite.snapshot(null, null)));
			}
			setAsSelected();
		});
	}

	@Override
	public Button getObject() {
		return entityButton;
	}

	@Override
	public VoogaEntity getEntity() {
		return entity;
	}

	protected final void setEntity(VoogaEntity entity) {
		this.entity = entity;
	}

	@Override
	public void actInAuthoringMode(ClickableUIComponent target, Object additonalInfo, ClickHandler clickHandler, Event event) {
		setAsSelected();
	}

	@Override
	public void actInGameplayMode(ClickableUIComponent target, Object additionalInfo, ClickHandler clickHandler, Event event) {
		actInAuthoringMode(target, additionalInfo, clickHandler, event);
	}
}
