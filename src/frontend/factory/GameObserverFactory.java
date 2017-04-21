package frontend.factory;

import controller.Controller;
import frontend.interfaces.GameObserver;
import frontend.interfaces.detailpane.DetailPaneExternal;
import frontend.interfaces.templatepane.TemplatePaneExternal;
import frontend.interfaces.worldview.WorldViewExternal;

public class GameObserverFactory {

	public static GameObserver newGameObserver(Controller controller, WorldViewExternal worldView,
			DetailPaneExternal detailPane, TemplatePaneExternal templatePane) {
		return new AuthoringObserver(controller, worldView, detailPane, templatePane);
	}

}
