package frontend.detailpane.interfaces;

public interface DetailPaneObservable {
	
	void addDetailPaneObserver(DetailPaneObserver observer);
	
	void removeDetailPaneObserver(DetailPaneObserver observer);

}
