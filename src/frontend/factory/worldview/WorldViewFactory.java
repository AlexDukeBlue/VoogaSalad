package frontend.factory.worldview;

import controller.Controller;
import frontend.interfaces.worldview.WorldViewExternal;

public class WorldViewFactory {

	public static WorldViewExternal newWorldView(Controller controller) {
		return new SimpleWorldView(controller, null);
	}

}
