package frontend.interfaces.worldview;

import java.util.Collection;

public interface WorldViewExternal extends GridViewExternal {

	void addWorldViewObserver(WorldViewObserver observer);

	void addAllWorldViewObservers(Collection<WorldViewObserver> worldViewObservers);

	void removeWorldViewObserver(WorldViewObserver observer);

	void removeAllWorldViewObservers(Collection<WorldViewObserver> worldViewObservers);

	GridViewExternal getGridViewExternal();

}
