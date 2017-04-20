package frontend.worldview.grid.interfaces;

public interface GridViewObservable {
	
	void addGridViewObserver(GridViewObserver observer);
	
	void removeGridViewObserver(GridViewObserver observer);

}
