package frontend.interfaces;

import frontend.interfaces.conditionspane.ConditionsPaneObserver;
import frontend.interfaces.detailpane.DetailPaneObserver;
import frontend.interfaces.templatepane.TemplatePaneObserver;
import frontend.interfaces.worldview.CellViewObserver;
import frontend.interfaces.worldview.GridViewObserver;
import frontend.interfaces.worldview.UnitViewObserver;
import frontend.interfaces.worldview.WorldViewObserver;

public interface GameObserver extends WorldViewObserver, GridViewObserver, CellViewObserver, UnitViewObserver,
		TemplatePaneObserver, DetailPaneObserver, ConditionsPaneObserver {

}
