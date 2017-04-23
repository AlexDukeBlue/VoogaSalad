package frontend.factory.worldview;

import controller.Controller;
import frontend.ComponentClickHandler;
import frontend.interfaces.worldview.WorldViewExternal;

public class WorldViewFactory {

	public static WorldViewExternal newWorldView(Controller controller, ComponentClickHandler clickHandler) {
		return new SimpleWorldView(controller, clickHandler);
	}

}
