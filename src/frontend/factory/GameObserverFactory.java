package frontend.factory;

import controller.Controller;
import frontend.factory.abilitypane.AbilityPane;
import frontend.interfaces.GameObserver;
import frontend.interfaces.detailpane.DetailPaneExternal;
import frontend.interfaces.templatepane.TemplatePaneExternal;
import frontend.interfaces.worldview.WorldViewExternal;

public class GameObserverFactory {

	public static GameObserver newGameObserver(Controller controller, WorldViewExternal worldView,
	                                           DetailPaneExternal detailPane, AbilityPane abilityPane, TemplatePaneExternal templatePane) {
		return new AuthoringObserver(controller, worldView, detailPane, abilityPane, templatePane);
	}

}
