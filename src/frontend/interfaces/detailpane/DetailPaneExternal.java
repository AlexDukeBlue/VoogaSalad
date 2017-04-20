package frontend.interfaces.detailpane;

import javafx.scene.Node;

public interface DetailPaneExternal {
	
	void addDetailPaneObserver(DetailPaneObserver observer);
	
	void removeDetailPaneObserver(DetailPaneObserver observer);
	
	Node getObject();

}
