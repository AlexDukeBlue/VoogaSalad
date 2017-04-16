import backend.grid.GridPattern;
import frontend.startup.StartupScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static void main(String[] args) {
//        new Thread() {
//            public void run() {
//                try {
//                    VoogaServerMain.main(args);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        @SuppressWarnings("unused")
        GridPattern pattern = GridPattern.HEXAGONAL_ADJACENT;
        StartupScreen starter = new StartupScreen(primaryStage, 700.0, 700.0);
        primaryStage.setScene(starter.getPrimaryScene());
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
