package util;

import javafx.scene.control.Alert;

public class AlertFactory {
	
	public static Alert errorAlert(String title, String header, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		return alert;
	}
	
	public static Alert warningAlert(String title, String header, String content) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		return alert;
	}

}
