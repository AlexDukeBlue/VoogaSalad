package frontend.templatepane.interfaces;

public interface TemplatePaneObservable {
	
	void addTemplatePaneObserver(TemplatePaneObserver observer);
	
	void removeTemplatePaneObserver(TemplatePaneObserver observer);

}
