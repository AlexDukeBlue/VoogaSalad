package frontend.factory.ObserverViewTrial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Observable;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SizingView implements ObservingView {

	private Stage stage;
	private StackPane stack;
	private Collection<ViewComponent<?>> components;

	public SizingView(ViewComponent<?>... component) {
		stage = new Stage();
		stack = new StackPane();
		components = new ArrayList<ViewComponent<?>>();
		Arrays.asList(component).stream().forEach(p -> {
			stack.getChildren().add((Pane) p.getObject());
			addComponents(p);
		});
	}
	
	public SizingView(){
		stage = new Stage();
		stack = new StackPane();
		components = new ArrayList<ViewComponent<?>>();
	}

	@Override
	public void update(Observable o, Object arg) {
		double x = ((ArrayList<Double>) arg).get(0);
		double y = ((ArrayList<Double>) arg).get(1);
		stage.setWidth(x);
		stage.setHeight(y);
	}

	@Override
	public Stage getObject() {
		setLayout();
		return stage;
	}

	@Override
	public void setLayout() {
		Scene scene;
		if (!stack.equals(null)) {
			scene = new Scene(stack);
		} else {
			scene = new Scene(new Pane(new Text("Nope")));
		}
		stage.setScene(scene);
	}

	@Override
	public void addComponents(ViewComponent<?>... component) {
			components.addAll(Arrays.asList(component));
			Arrays.asList(component).stream().forEach(e -> stack.getChildren().add((Node) e.getObject()));
	}

	@Override
	public Collection<ViewComponent<?>> retrieveComponents() {
		return components;
	}

}
