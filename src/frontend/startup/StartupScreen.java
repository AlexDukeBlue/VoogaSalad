package frontend.startup;

import backend.util.ImmutableGameState;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import util.net.ObservableClient;


public class StartupScreen {
    private Scene primaryScene;
    private BorderPane primaryPane;
    private double width, height;
    private StartupMenuBar fileMenu;
    private StartupSelectionScreen selectionScreen;
    private ObservableClient<ImmutableGameState> myClient;

    public StartupScreen(ObservableClient<ImmutableGameState> client) {
        this(client, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
    }

    public StartupScreen(ObservableClient<ImmutableGameState> client, double width, double height) {
    	myClient = client;
        this.initPrimaryScene();
        this.width = width;
        this.height = height;
    }

    private void initPrimaryScene() {
        this.primaryScene = new Scene(this.initPrimaryPane());
    }

    private BorderPane initPrimaryPane() {
        System.out.println("here");
        this.fileMenu = new StartupMenuBar(this);
        this.selectionScreen = new StartupSelectionScreen(this, myClient);
        BackgroundImage bi = new BackgroundImage(new Image("frontend/properties/Screen Shot 2017-04-07 at 3.22.00 PM.png"), 
        		BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, new BackgroundSize(width, height, false, false, true, true));
        Background imgv = new Background(bi);
        this.primaryPane = new BorderPane() {{
            resize(width, height);
            setTop(fileMenu);
            setBottom(selectionScreen);
            selectionScreen.setAlignment(Pos.CENTER);
            setBackground(imgv);
        }};
//        System.out.println(selectionScreen.getChildren());
        return primaryPane;
    }
    
    public BorderPane getPrimaryPane(){
    	return primaryPane;
    }

    public Scene getPrimaryScene() {
        return primaryScene;
    }
}
