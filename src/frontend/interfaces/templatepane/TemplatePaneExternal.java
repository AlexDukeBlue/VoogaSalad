package frontend.interfaces.templatepane;

import frontend.ClickHandler;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.VBox;
import util.polyglot.Polyglot;

public interface TemplatePaneExternal {

	VBox getNode();

	Polyglot getPolyglot();

	void setClickHandler(ClickHandler clickHandler);

	ObjectProperty<String> getStyleSheet();
}
