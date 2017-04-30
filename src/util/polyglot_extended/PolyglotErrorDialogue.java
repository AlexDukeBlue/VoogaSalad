package util.polyglot_extended;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Yes, I realize this is a singleton, but...
 * We wanted to create an alert dialogue to go along with the polyglot utility that we used.
 * The reason we made the alert dialogue a singleton is that if there was an error changing languages,
 * the error would occur in multiple areas which would have caused multiple alert dialogues, This way, there is only
 * one.
 * 
 * The PolyglotErrorDialogue serves to let the user know that there was an error when trying to change language using
 * the Polyglot utility.
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
