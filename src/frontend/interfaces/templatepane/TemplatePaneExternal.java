package frontend.interfaces.templatepane;

import frontend.ClickHandler;
import javafx.scene.layout.VBox;
import polyglot.Polyglot;

public interface TemplatePaneExternal {

	VBox getObject();

	Polyglot getPolyglot();

	void setClickHandler(ClickHandler clickHandler);
}
