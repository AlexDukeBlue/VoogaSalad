package frontend.interfaces.detailpane;

import backend.util.VoogaEntity;
import javafx.scene.layout.Region;

public interface DetailPaneExternal {
	Region getObject();

	void setContent(VoogaEntity entity);
}
