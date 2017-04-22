package frontend.interfaces.worldview;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import javafx.scene.layout.Pane;

import java.util.Collection;

public interface UnitViewExternal {

	void addUnitViewObserver(UnitViewObserver observer);

	void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers);

	void removeUnitViewObserver(UnitViewObserver observer);

	void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers);

	Pane getObject();

	String getUnitName();

	Unit getUnit();

	CoordinateTuple getUnitLocation();
}
