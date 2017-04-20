package frontend.factory.worldview;

import controller.Controller;
import frontend.interfaces.GameObserver;
import frontend.interfaces.worldview.WorldViewExternal;

public class WorldViewFactory {

	public static WorldViewExternal newWorldView(Controller controller, GameObserver gameDelegate) {
		return new SimpleWorldView(controller, gameDelegate);
	}

}
