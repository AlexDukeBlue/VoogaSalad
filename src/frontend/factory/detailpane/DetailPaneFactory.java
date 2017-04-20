package frontend.factory.detailpane;

import frontend.interfaces.detailpane.DetailPaneExternal;

public class DetailPaneFactory {
	
	public static DetailPaneExternal newDetailPane(){
		return new DetailPane();
	}

}
