package frontend.interfaces;

import frontend.interfaces.detailpane.DetailPaneObserver;
import frontend.interfaces.templatepane.TemplatePaneObserver;
import frontend.interfaces.worldview.WorldViewObserver;

public interface GameObserver
		extends WorldViewObserver, TemplatePaneObserver, DetailPaneObserver {

}
