package frontend.interfaces.worldview;

import java.util.Collection;

public interface UnitViewExternal {

	void addUnitViewObserver(UnitViewObserver observer);
	
	void addAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers);

	void removeUnitViewObserver(UnitViewObserver observer);
	
	void removeAllUnitViewObservers(Collection<UnitViewObserver> unitViewObservers);

}
