package frontend.interfaces.worldview;

import frontend.ClickHandler;
import javafx.scene.layout.Region;
import util.polyglot.Polyglot;

public interface WorldViewExternal {

	GridViewExternal getGridView();

	Region getNode();

	Polyglot getPolyglot();

	void setClickHandler(ClickHandler clickHandler);
}
