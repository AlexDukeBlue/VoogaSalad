package frontend.worldview.grid.classes.newclasses;

import java.util.ArrayList;
import java.util.Collection;

import frontend.worldview.grid.interfaces.UnitViewExternalInterface;
import frontend.worldview.grid.interfaces.UnitViewObservable;
import frontend.worldview.grid.interfaces.UnitViewObserver;

public class SimpleUnitView implements UnitViewExternalInterface, UnitViewObservable {
	
	private Collection<UnitViewObserver> observers;
	
	public SimpleUnitView(){
		initialize();
	}

	@Override
	public void addUnitViewObserver(UnitViewObserver observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	@Override
	public void removeUnitViewObserver(UnitViewObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}
	
	private void initialize(){
		observers = new ArrayList<>();
	}

}
