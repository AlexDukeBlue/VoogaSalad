package frontend.worldview.grid.interfaces;

public interface GridViewObserved {
	
	void addGridViewObserver(GridViewObserver observer);
	
	void removeGridViewObserver(GridViewObserver observer);

}
