package frontend.factory.detailpane;

import controller.Controller;
import frontend.ClickHandler;
import frontend.interfaces.detailpane.DetailPaneExternal;

public class DetailPaneFactory {
	
	public static DetailPaneExternal newDetailPane(Controller controller, ClickHandler clickHandler){
		return new DetailPane(clickHandler,controller);
	}

}
