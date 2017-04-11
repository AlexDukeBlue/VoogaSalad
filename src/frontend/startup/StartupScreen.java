package frontend.startup;

import backend.util.GameplayState;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class StartupScreen {
    private Scene primaryScene;
    private BorderPane primaryPane;
    private double width, height;
    private StartupMenuBar fileMenu;
    private StartupSelectionScreen selectionScreen;
    //private ObservableClient<ImmutableGameState> myClient;
    private Stage stage;

    public StartupScreen() {
        this(new Stage(),Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
    }

    public StartupScreen(Stage stage, double width, double height) {
    	this.stage = stage;
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
        this.selectionScreen = new StartupSelectionScreen(stage,this);
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
