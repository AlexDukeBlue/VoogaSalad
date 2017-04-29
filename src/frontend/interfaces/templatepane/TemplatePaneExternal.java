package frontend.interfaces.templatepane;

import frontend.ClickHandler;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Region;
import util.polyglot.Polyglot;

public interface TemplatePaneExternal {

	Region getNode();

	Polyglot getPolyglot();

	void setClickHandler(ClickHandler clickHandler);

	ObjectProperty<String> getStyleSheet();
}
