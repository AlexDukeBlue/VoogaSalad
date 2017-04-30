package util.polyglot_extended;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Yes, I realize this is a singleton, but...
 * @author Andreas
 *
 */
public class PolyglotErrorDialogue {
	
	private static PolyglotErrorDialogue polyglotErrorDialogue;
	
	private PolyglotErrorDialogue(){
		initialize();
	}
	
	private void initialize() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Polyglot Error");
		alert.setHeaderText("Error Loading Language");
		alert.setContentText("An error occurred when changing the language");
		alert.showAndWait();
	}

	public static synchronized PolyglotErrorDialogue getInstance(){
		if (polyglotErrorDialogue == null){
			polyglotErrorDialogue = new PolyglotErrorDialogue();
		}
		return polyglotErrorDialogue;
	}
}
