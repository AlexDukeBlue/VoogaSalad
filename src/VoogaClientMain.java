//import frontend.UI;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Created by th174 on 4/4/2017.
 */
public class VoogaClientMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(ResourceBundle.getBundle("resources/Selections", Locale.getDefault()).getString("Title"));
//        UI userInterface = new UI();
//        primaryStage.setScene(userInterface.getPrimaryScene());
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}
