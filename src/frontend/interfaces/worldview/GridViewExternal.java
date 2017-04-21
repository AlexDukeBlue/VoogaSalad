package frontend.interfaces.worldview;

import java.util.Collection;

import javafx.scene.control.ScrollPane;

public interface GridViewExternal {

	void addGridViewObserver(GridViewObserver observer);

	void addAllGridViewObservers(Collection<GridViewObserver> gridViewObservers);

	void removeGridViewObserver(GridViewObserver observer);

	void removeAllGridViewObservers(Collection<GridViewObserver> gridViewObservers);

	void addCellViewObserver(CellViewObserver observer);

	void addAllCellViewObservers(Collection<CellViewObserver> cellViewObservers);

	void removeCellViewObserver(CellViewObserver observer);

	void removeAllCellViewObservers(Collection<CellViewObserver> cellViewObservers);

	void addUnitViewObserver(UnitViewObserver observer);

	void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers);

	void removeUnitViewObserver(UnitViewObserver observer);

	void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers);

	ScrollPane getObject();

}
