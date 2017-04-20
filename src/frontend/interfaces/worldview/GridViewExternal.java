package frontend.interfaces.worldview;

import java.util.Collection;

import javafx.scene.control.ScrollPane;

public interface GridViewExternal extends CellViewExternal {

	void addGridViewObserver(GridViewObserver observer);

	void addAllGridViewObservers(Collection<GridViewObserver> gridViewObservers);

	void removeGridViewObserver(GridViewObserver observer);

	void removeAllGridViewObservers(Collection<GridViewObserver> gridViewObservers);

	ScrollPane getObject();

}
