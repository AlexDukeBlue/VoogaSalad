package frontend.worldview.grid.interfaces;

public interface CellViewObservable {
	
	void addCellViewObserver(CellViewObserver observer);
	
	void removeCellViewObserver(CellViewObserver observer);

}
