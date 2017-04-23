package frontend.interfaces.worldview;

import frontend.ClickHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import polyglot.Polyglot;

public interface WorldViewExternal {

	GridViewExternal getGridView();

	Region getObject();

	ScrollPane getGridPane();

	Polyglot getPolyglot();

	void setClickHandler(ClickHandler clickHandler);

}
