package frontend.factory;

import backend.util.VoogaEntity;
import frontend.interfaces.GameObserver;
import frontend.interfaces.templatepane.TemplatePaneExternal;
import frontend.interfaces.worldview.CellViewExternal;
import frontend.interfaces.worldview.UnitViewExternal;

class AuthoringObserver implements GameObserver {

	@Override
	public void didClickCellViewExternalInterface(CellViewExternal cell) {
		// TODO Auto-generated method stub
		System.out.println("CellViewExternalCell: " + cell + "\n");
	}

	@Override
	public void didClickUnitViewExternalInterface(UnitViewExternal unit) {
		// TODO Auto-generated method stub
		System.out.println("UnitViewExternalCell: " + unit + "\n");
	}

	@Override
	public void didClickVoogaEntity(TemplatePaneExternal templatePane, VoogaEntity entity) {
		// TODO Auto-generated method stub
		System.out.println("TemplatePane: " + templatePane);
		System.out.println("VoogaEntity: " + entity + "\n");
	}

}
