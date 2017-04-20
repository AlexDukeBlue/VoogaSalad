package frontend.worldview.grid.external_interfaces;

public interface GridViewObserved {
	
	void addGridViewObserver(GridViewObserver observer);
	
	void removeGridViewObserver(GridViewObserver observer);

}
