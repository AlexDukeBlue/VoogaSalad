package frontend.interfaces.detailpane;

import backend.util.VoogaEntity;
import frontend.ClickHandler;
import javafx.scene.layout.Region;
import polyglot.Polyglot;

public interface DetailPaneExternal {

	Region getNode();

	void setContent(VoogaEntity entity);

	Polyglot getPolyglot();

	void setClickHandler(ClickHandler clickHandler);

}
