package frontend.interfaces.worldview;

import frontend.ClickHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

public interface WorldViewExternal {

	GridViewExternal getGridView();

	Region getObject();

	ScrollPane getGridPane();

	void setClickHandler(ClickHandler clickHandler);

}
