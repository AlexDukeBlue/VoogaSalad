package frontend.interfaces.detailpane;

import java.util.Collection;

import backend.util.VoogaEntity;
import javafx.scene.layout.Region;

public interface DetailPaneExternal {
	
	void addDetailPaneObserver(DetailPaneObserver observer);
	
	void addAllDetailPaneObservers(Collection<DetailPaneObserver> observers);
	
	void removeDetailPaneObserver(DetailPaneObserver observer);
	
	void removeAllDetailPaneObservers(Collection<DetailPaneObserver> observers);
	
	Region getObject();

	void setContent(VoogaEntity entity, String string);

}
