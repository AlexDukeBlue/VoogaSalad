package frontend.interfaces.worldview;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

public interface WorldViewExternal {

	GridViewExternal getGridView();

	Region getObject();

	ScrollPane getGridPane();

}
