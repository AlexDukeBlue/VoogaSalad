package frontend.factory;

import frontend.interfaces.GameObserver;

public class GameObserverFactory {

	public static GameObserver newGameObserver() {
		return new AuthoringObserver();
	}

}
