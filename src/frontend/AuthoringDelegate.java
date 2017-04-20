package frontend;

import backend.util.VoogaEntity;
import frontend.detailpane.interfaces.DetailPaneObserver;
import frontend.templatepane.interfaces.TemplatePaneExternal;
import frontend.templatepane.interfaces.TemplatePaneObserver;
import frontend.worldview.grid.interfaces.CellViewExternalInterface;
import frontend.worldview.grid.interfaces.CellViewObserver;
import frontend.worldview.grid.interfaces.GridViewObserver;
import frontend.worldview.grid.interfaces.UnitViewExternalInterface;
import frontend.worldview.grid.interfaces.UnitViewObserver;

public class AuthoringDelegate
		implements GridViewObserver, CellViewObserver, UnitViewObserver, TemplatePaneObserver, DetailPaneObserver {

	@Override
	public void didClickVoogaEntity(TemplatePaneExternal templatePane, VoogaEntity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didClickUnitViewExternalInterface(UnitViewExternalInterface unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didClickCellViewExternalInterface(CellViewExternalInterface cell) {
		// TODO Auto-generated method stub
		
	}

}
