package frontend.interfaces.worldview;

import frontend.ClickHandler;
import javafx.scene.layout.Region;
import polyglot.Polyglot;

public interface WorldViewExternal {

	GridViewExternal getGridView();

	Region getObject();

	Polyglot getPolyglot();

	void setClickHandler(ClickHandler clickHandler);

}
