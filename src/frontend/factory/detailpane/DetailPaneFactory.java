package frontend.factory.detailpane;

import frontend.ClickHandler;
import frontend.interfaces.detailpane.DetailPaneExternal;

public class DetailPaneFactory {
	
	public static DetailPaneExternal newDetailPane(ClickHandler clickHandler){
		return new DetailPane(clickHandler);
	}

}
