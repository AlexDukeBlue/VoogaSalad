package frontend.worldview.grid.classes.newclasses;

import java.util.ArrayList;
import java.util.Collection;

import frontend.worldview.grid.interfaces.GridViewExternalInterface;
import frontend.worldview.grid.interfaces.GridViewObservable;
import frontend.worldview.grid.interfaces.GridViewObserver;

class SimpleGridView implements GridViewExternalInterface, GridViewObservable{
	
	private Collection<GridViewObserver> observers;
	
	public SimpleGridView(){
		initialize();
	}

	@Override
	public void addGridViewObserver(GridViewObserver observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	@Override
	public void removeGridViewObserver(GridViewObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}
	
	private void initialize(){
		observers = new ArrayList<>();
	}

}
