package frontend.interfaces.templatepane;

import javafx.scene.layout.Region;

public interface TemplatePaneExternal {

	void addTemplatePaneObserver(TemplatePaneObserver observer);

	void removeTemplatePaneObserver(TemplatePaneObserver observer);

	Region getObject();

}
