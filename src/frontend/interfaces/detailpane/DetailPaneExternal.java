package frontend.interfaces.detailpane;

import backend.util.VoogaEntity;
import frontend.ClickHandler;
import javafx.scene.layout.Region;

public interface DetailPaneExternal {
	Region getObject();

	void setContent(VoogaEntity entity);

	void setClickHandler(ClickHandler clickHandler);
}
