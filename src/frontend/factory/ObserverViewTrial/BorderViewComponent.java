package frontend.factory.ObserverViewTrial;

import java.util.ArrayList;

import javafx.scene.layout.BorderPane;

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
			System.out.println(event.getButton().toString());
			change();
			setChanged();
			this.notifyObservers(coord);
			System.out.println(this.hasChanged());
		});
	}

}
