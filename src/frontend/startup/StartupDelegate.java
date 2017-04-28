package frontend.startup;

import javafx.stage.Stage;

interface StartupDelegate {

	void create(Stage stage);

	void join(Stage stage);

	void play(Stage stage);

}
