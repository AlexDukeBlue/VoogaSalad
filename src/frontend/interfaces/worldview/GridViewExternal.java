package frontend.interfaces.worldview;

import frontend.ClickHandler;
import javafx.scene.control.ScrollPane;

public interface GridViewExternal {
	ScrollPane getObject();

	void setClickHandler(ClickHandler clickHandler);
}
