package frontend.interfaces.worldview;

import java.util.Collection;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

public interface WorldViewExternal {

	void addWorldViewObserver(WorldViewObserver observer);

	void addAllWorldViewObservers(Collection<WorldViewObserver> worldViewObservers);

	void removeWorldViewObserver(WorldViewObserver observer);

	void removeAllWorldViewObservers(Collection<WorldViewObserver> worldViewObservers);

	GridViewExternal getGridViewExternal();

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

	Region getObject();

	ScrollPane getGridPane();

}
