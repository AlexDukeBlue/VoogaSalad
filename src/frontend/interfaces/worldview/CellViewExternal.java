package frontend.interfaces.worldview;

import java.util.Collection;

import backend.grid.CoordinateTuple;

public interface CellViewExternal {

	void addCellViewObserver(CellViewObserver observer);

	void addAllCellViewObservers(Collection<CellViewObserver> cellViewObservers);

	void removeCellViewObserver(CellViewObserver observer);

	void removeAllCellViewObservers(Collection<CellViewObserver> cellViewObservers);
	
	CoordinateTuple getLocation();

	void addUnitViewObserver(UnitViewObserver observer);

	void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers);

	void removeUnitViewObserver(UnitViewObserver observer);

	void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers);

}
