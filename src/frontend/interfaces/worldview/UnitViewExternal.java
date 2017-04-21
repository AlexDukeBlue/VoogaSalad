package frontend.interfaces.worldview;

import backend.grid.CoordinateTuple;
import javafx.scene.image.ImageView;

import java.util.Collection;

public interface UnitViewExternal {

	void addUnitViewObserver(UnitViewObserver observer);

	void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers);

	void removeUnitViewObserver(UnitViewObserver observer);

	void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers);

	ImageView getObject();

	String getUnitName();

	CoordinateTuple getUnitLocation();
}