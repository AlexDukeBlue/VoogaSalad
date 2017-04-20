package frontend.worldview.grid.interfaces;

public interface UnitViewObservable {

	void addUnitViewObserver(UnitViewObserver observer);

	void removeUnitViewObserver(UnitViewObserver observer);

}
