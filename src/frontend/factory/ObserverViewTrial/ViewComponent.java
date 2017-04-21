package frontend.factory.ObserverViewTrial;

import java.util.Observable;

public abstract class ViewComponent<T> extends Observable{

	private String nameOfThis;
	private ObservingView owner;
	private T uiComponent;
	
	public ViewComponent(String name, ObservingView view, T type){
		owner = view;
		nameOfThis = name;
		uiComponent = type;
		this.addObserver(view);
		initialize();
	}

	protected T getComponent(){
		return uiComponent;
	}
	
	protected abstract void initialize();
		
	protected T getObject(){
		return uiComponent;
	}
	
	protected String getName(){
		return nameOfThis;
	}
}
