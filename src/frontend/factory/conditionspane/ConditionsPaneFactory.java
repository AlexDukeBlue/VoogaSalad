/**
 * 
 */
package frontend.factory.conditionspane;

import controller.Controller;
import frontend.interfaces.conditionspane.ConditionsPaneExternal;

/**
 * @author Stone Mathers
 * Created 4/20/2017
 */
public class ConditionsPaneFactory {

	public static ConditionsPaneExternal newConditionsPane(Controller controller){
		return new ConditionsPane(controller);
	}

}
