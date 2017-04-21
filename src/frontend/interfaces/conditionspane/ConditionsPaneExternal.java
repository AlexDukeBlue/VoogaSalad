/**
 * 
 */
package frontend.interfaces.conditionspane;

import java.util.Collection;

import javafx.scene.layout.Region;



/**
 * @author Stone Mathers
 * Created 4/20/2017
 */
public interface ConditionsPaneExternal {

	void addConditionsPaneObserver(ConditionsPaneObserver observer);

	void addAllConditionsPaneObservers(Collection<ConditionsPaneObserver> observers);

	void removeConditionsPaneObserver(ConditionsPaneObserver observer);

	void removeAllConditionsPaneObservers(Collection<ConditionsPaneObserver> observers);

	Region getObject();
	
}
