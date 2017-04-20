package frontend.interfaces.templatepane;

import java.util.Collection;

import javafx.scene.layout.Region;

public interface TemplatePaneExternal {

	void addTemplatePaneObserver(TemplatePaneObserver observer);

	void addAllTemplatePaneObservers(Collection<TemplatePaneObserver> observers);

	void removeTemplatePaneObserver(TemplatePaneObserver observer);

	void removeAllTemplatePaneObservers(Collection<TemplatePaneObserver> observers);

	Region getObject();

}
