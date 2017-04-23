package frontend.interfaces.worldview;

import frontend.ClickHandler;
import javafx.scene.control.ScrollPane;
import polyglot.Polyglot;

public interface GridViewExternal {
	ScrollPane getObject();

	void setClickHandler(ClickHandler clickHandler);

	Polyglot getPolyglot();
}
