package frontend.factory.ObserverViewTrial;

import javafx.scene.layout.BorderPane;

import java.util.ArrayList;

public class BorderViewComponent extends ViewComponent<BorderPane> {

	boolean makeChange = true;
	
	public BorderViewComponent(String name, ObservingView view, BorderPane pane) {
		super(name, view, pane);
	}
	
	private void change(){
		makeChange = !makeChange;
	}

	@Override
	protected void initialize() {
		getComponent().setOnMouseClicked(event -> {
			double x = event.getX();
			double y = event.getY();
			ArrayList<Double> coord = new ArrayList<>();
			coord.add(x);
			coord.add(y);
			change();
			setChanged();
			this.notifyObservers(coord);
		});
	}

}
