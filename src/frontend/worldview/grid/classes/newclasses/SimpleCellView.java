package frontend.worldview.grid.classes.newclasses;

import java.util.ArrayList;
import java.util.Collection;

import frontend.worldview.grid.interfaces.CellViewExternalInterface;
import frontend.worldview.grid.interfaces.CellViewObservable;
import frontend.worldview.grid.interfaces.CellViewObserver;

public class SimpleCellView implements CellViewExternalInterface, CellViewObservable {

	private Collection<CellViewObserver> observers;

	public SimpleCellView() {
		initialize();
	}

	@Override
	public void addCellViewObserver(CellViewObserver observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	@Override
	public void removeCellViewObserver(CellViewObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	private void initialize() {
		observers = new ArrayList<>();
	}

}
