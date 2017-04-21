package frontend.factory.ObserverViewTrial;

import java.util.Collection;
import java.util.Observer;

import javafx.stage.Stage;

public interface ObservingView extends Observer {

	/**
	 * This will return a Stage with all of the components in it.
	 * 
	 * @return Stage
	 */
	Stage getObject();
	
	/**
	 * This method will internally take all present ViewComponents and add their layouts to the EditableView's Pane.
	 */
	void setLayout();
	
	void addComponents(ViewComponent<?>... components);
	
	Collection<ViewComponent<?>> retrieveComponents();
}
