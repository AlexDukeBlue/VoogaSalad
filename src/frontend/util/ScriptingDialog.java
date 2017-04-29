package frontend.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import controller.Controller;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import util.scripting.VoogaScriptEngine;
import util.scripting.VoogaScriptEngineManager;
import util.scripting.VoogaScriptException;

/**
 * TODO: RESOUCE BUNDLE PLSSSS
 *
 * @author Created by th174 on 4/11/2017.
 */
public class ScriptingDialog extends BaseUIManager<Region> {
	private final BorderPane pane;
	private final TextArea scriptArea;
	private final ChoiceBox<String> languagesMenu;
	private VoogaScriptEngine scriptEngine;
	private BooleanProperty hasCompiled;
	private String strategy;

	public ScriptingDialog(Controller controller) {
		super(controller);
		hasCompiled = new SimpleBooleanProperty(false);
		languagesMenu = new ChoiceBox<>(
				FXCollections.observableArrayList(VoogaScriptEngineManager.getAllSupportedScriptingLanguages()));
		pane = new BorderPane();
		scriptArea = new TextArea();
		Button compileButton = new Button();
		compileButton.textProperty().bind(getPolyglot().get("Compile"));
		compileButton.setOnAction(evt -> {
			try {
				scriptEngine = VoogaScriptEngineManager.read(languagesMenu.getValue(), scriptArea.getText());
				hasCompiled.setValue(true);
			} catch (VoogaScriptException e) {
				handleException(e);
			}
		});
		Label scriptingLabel = new Label();
		scriptingLabel.textProperty().bind(getPolyglot().get("chooseScriptingLang"));
		HBox topBox = new HBox(scriptingLabel);
		topBox.getChildren().add(languagesMenu);
		topBox.setAlignment(Pos.CENTER);
		HBox bottomBox = new HBox(compileButton);
		bottomBox.setAlignment(Pos.TOP_RIGHT);
		languagesMenu.setOnAction(
				event -> scriptArea.setText(VoogaScriptEngineManager.getDefaultText(languagesMenu.getValue())));
		pane.setTop(topBox);
		pane.setBottom(bottomBox);
		pane.setCenter(scriptArea);
	}

	public ReadOnlyBooleanProperty hasCompiled() {
		return hasCompiled;
	}

	public void setPrompt(String strat) {
		scriptArea.setPromptText(getResourceBundle().getString(strat + languagesMenu.getValue()));
		strategy = strat;
	}

	private void handleException(Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.titleProperty().bind(getPolyglot().get("syntaxError"));
		alert.setHeaderText(null);
		alert.contentTextProperty().bind(getPolyglot().get("compileError"));
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.getCause().printStackTrace(pw);
		String exceptionText = sw.toString();
		Label label = new Label();
		label.textProperty().bind(getPolyglot().get("stackTrace"));
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
		hasCompiled.setValue(false);
	}

	@Override
	public void update() {
	}

	@Override
	public Region getNode() {
		return pane;
	}

	public String getLanguage() {
		return languagesMenu.getValue();
	}

	public Optional<VoogaScriptEngine> getScriptEngine() {
		return Optional.ofNullable(scriptEngine);
	}
}
