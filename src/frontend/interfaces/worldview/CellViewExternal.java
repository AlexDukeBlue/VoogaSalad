package frontend.interfaces.worldview;

import java.util.Collection;

public interface CellViewExternal extends UnitViewExternal {

	void addCellViewObserver(CellViewObserver observer);

	void addAllCellViewObservers(Collection<CellViewObserver> cellViewObservers);

	void removeCellViewObserver(CellViewObserver observer);

	void removeAllCellViewObservers(Collection<CellViewObserver> cellViewObservers);

}
