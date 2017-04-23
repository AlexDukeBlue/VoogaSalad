package frontend.factory.detailpane;

import frontend.ComponentClickHandler;
import frontend.interfaces.detailpane.DetailPaneExternal;

public class DetailPaneFactory {
	
	public static DetailPaneExternal newDetailPane(ComponentClickHandler clickHandler){
		return new DetailPane(clickHandler);
	}

}
