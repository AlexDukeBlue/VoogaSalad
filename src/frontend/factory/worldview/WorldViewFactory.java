package frontend.factory.worldview;

import controller.Controller;
import frontend.ClickHandler;
import frontend.interfaces.worldview.WorldViewExternal;

public class WorldViewFactory {

	public static WorldViewExternal newWorldView(Controller controller, ClickHandler clickHandler) {
		return new SimpleWorldView(controller, clickHandler);
	}

}
