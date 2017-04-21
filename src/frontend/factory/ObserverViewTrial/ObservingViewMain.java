package frontend.factory.ObserverViewTrial;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class ObservingViewMain extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane();
		pane.setCenter(new Pane());
		((Region) pane.getCenter()).setBackground(new Background(new BackgroundImage(new Image("https://sites.psu.edu/siowfa16/files/2016/10/YeDYzSR-10apkm4.png", 600, 600, false, false), null, null, null, null)));
		SizingView view = new SizingView();
		BorderViewComponent comp = new BorderViewComponent("Hi", view, pane);
		view.addComponents(comp);
		view.getObject().show();
	}

}
