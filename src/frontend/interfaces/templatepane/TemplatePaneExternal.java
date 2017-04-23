package frontend.interfaces.templatepane;

import java.util.Collection;

import javafx.scene.layout.VBox;
import polyglot.Polyglot;

public interface TemplatePaneExternal {

	void addTemplatePaneObserver(TemplatePaneObserver observer);

	void addAllTemplatePaneObservers(Collection<TemplatePaneObserver> observers);

	void removeTemplatePaneObserver(TemplatePaneObserver observer);

	void removeAllTemplatePaneObservers(Collection<TemplatePaneObserver> observers);

	VBox getObject();
	
	Polyglot getPolyglot();

}
