package frontend.util;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import util.scripting.VoogaScriptEngine;
import util.scripting.VoogaScriptEngineManager;
import util.scripting.VoogaScriptException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * TODO: RESOUCE BUNDLE PLSSSS
 *
 * @author Created by th174 on 4/11/2017.
 */
public class ScriptingDialog extends BaseUIManager<Region> {
	private final BorderPane pane;
	private final TextArea scriptArea;
	private final ComboBox<String> languagesMenu;
	private VoogaScriptEngine scriptEngine;

	public ScriptingDialog(Controller controller) {
		super(controller);
		languagesMenu = new ComboBox<>(FXCollections.observableArrayList(VoogaScriptEngineManager.getAllSupportedScriptingLanguages()));
		pane = new BorderPane();
		scriptArea = new TextArea();
		Button compileButton = new Button("Compile");
		compileButton.setOnAction(evt -> {
			try {
				scriptEngine = VoogaScriptEngineManager.read(languagesMenu.getValue(), scriptArea.getText());
			} catch (VoogaScriptException e) {
				handleException(e);
			}
		});
		HBox topBox = new HBox(new Label("Choose scripting language: "));
		topBox.getChildren().add(languagesMenu);
		topBox.setAlignment(Pos.CENTER);
		HBox bottomBox = new HBox(compileButton);
		bottomBox.setAlignment(Pos.TOP_RIGHT);
		pane.setTop(topBox);
		pane.setBottom(bottomBox);
		pane.setCenter(scriptArea);
	}

	private void handleException(Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Syntax Error!");
		alert.setHeaderText(null);
		alert.setContentText("An error occurred compiling your code!");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.getCause().printStackTrace(pw);
		String exceptionText = sw.toString();
		Label label = new Label("Stacktrace:");
		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public Region getObject() {
		return pane;
	}

	public Optional<VoogaScriptEngine> getScriptEngine() {
		return Optional.ofNullable(scriptEngine);
	}
}
