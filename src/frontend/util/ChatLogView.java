package frontend.util;

import backend.player.ChatMessage;
import controller.Controller;
import frontend.View;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 3/31/2017.
 */

public class ChatLogView extends BaseUIManager<BorderPane> {
	private final String header;
	private final BorderPane pane;
	private final TextArea textArea;
	private final MediaPlayer mediaPlayer;
	private final ImageView showHideArrow;
	private int logLength;

	public ChatLogView(Controller controller) {
		super(controller);
		String formatString = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n----" + getPolyglot().get("Joined").getValueSafe() + " [No place like 127.0.0.1]'s " + getPolyglot().get("chat room").getValueSafe() + "!----\n\n---%s----\n\n";
		header = String.format(formatString, Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)));
		pane = new BorderPane();
		showHideArrow = new ImageView(View.getImg(getResourceBundle().getString("trianglePath")));
		showHideArrow.setSmooth(true);
		showHideArrow.setOnMouseClicked(event -> setExpandedState(!isExpanded()));
		showHideArrow.setFitWidth(20);
		showHideArrow.setFitHeight(20);
		pane.setMaxSize(1000, 600);
		textArea = initTextArea();
		pane.setBottom(initTextInputBox());
		pane.setPickOnBounds(true);
		mediaPlayer = new MediaPlayer(new Media(Paths.get(getResourceBundle().getString("steamMessageSoundPath")).toUri().toString()));
	}

	@Override
	public BorderPane getNode() {
		return pane;
	}

	public void setExpandedState(boolean expandedState) {
		pane.setCenter(expandedState ? textArea : null);
		showHideArrow.setRotate(expandedState ? 270 : 0);
	}

	public boolean isExpanded() {
		return Objects.nonNull(pane.getCenter());
	}

	private TextArea initTextArea() {
		TextArea textArea = new TextArea(header);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMouseTransparent(true);
		textArea.positionCaret(textArea.getText().length());
		return textArea;
	}

	@Override
	public void update() {
		List<ChatMessage> chatlog = getController().getPlayer(getController().getMyPlayerName()).getChatLog();
		if (chatlog.size() > logLength) {
			logLength = chatlog.size();
			textArea.setText(header + chatlog.stream().map(Object::toString).collect(Collectors.joining("\n\n")));
			textArea.positionCaret(textArea.getText().length());
			mediaPlayer.seek(Duration.ZERO);
			mediaPlayer.play();
			setExpandedState(true);
		}
	}

	private HBox initTextInputBox() {
		HBox bottomBox = new HBox();
		bottomBox.getStyleClass().add("hbox");
		bottomBox.setAlignment(Pos.CENTER);
		ChoiceBox<ChatMessage.AccessLevel> chatModeChooser = initChoiceBox();
		TextField to = new TextField(getPolyglot().get("To").getValueSafe() + ": ");
		getPolyglot().setOnLanguageChange(event -> {
			to.setText(getPolyglot().get("To").getValueSafe() + ": ");
		});
		to.setMinWidth(35);
		to.setEditable(false);
		to.setMouseTransparent(true);
		TextField messageRecipientField = new TextField();
		messageRecipientField.setMinWidth(80);
		chatModeChooser.setOnAction(event -> showOrHideRecipientField(bottomBox, chatModeChooser, to, messageRecipientField));
		TextField textContentInputField = new TextField();
		textContentInputField.setMinWidth(200);
		textContentInputField.setPrefWidth(1000);
		textContentInputField.setOnKeyPressed(evt -> submitMessage(evt, chatModeChooser, textContentInputField, messageRecipientField));
		textContentInputField.setOnMouseClicked(evt -> setExpandedState(true));
		bottomBox.getChildren().addAll(chatModeChooser, textContentInputField, showHideArrow);
		return bottomBox;
	}

	private ChoiceBox<ChatMessage.AccessLevel> initChoiceBox() {
		ChoiceBox<ChatMessage.AccessLevel> chatModeChooser = new ChoiceBox<>(FXCollections.observableArrayList(ChatMessage.AccessLevel.values()));
		chatModeChooser.setMinWidth(110);
		chatModeChooser.setValue(ChatMessage.AccessLevel.ALL);
		return chatModeChooser;
	}

	private void showOrHideRecipientField(HBox bottomBox, ChoiceBox<ChatMessage.AccessLevel> chatModeChooser,
	                                      TextField toLabel, TextField messageRecipientField) {
		if (chatModeChooser.getValue().equals(ChatMessage.AccessLevel.WHISPER)) {
			bottomBox.getChildren().add(1, messageRecipientField);
			bottomBox.getChildren().add(1, toLabel);
		} else {
			bottomBox.getChildren().remove(toLabel);
			bottomBox.getChildren().remove(messageRecipientField);
			messageRecipientField.clear();
		}
	}

	private void submitMessage(KeyEvent evt, ChoiceBox<ChatMessage.AccessLevel> chatModeChooser,
	                           TextField textContentInputField, TextField messageRecipientField) {
		if (evt.getCode() == KeyCode.ENTER && textContentInputField.getText().length() > 0) {
			getController().sendMessage(textContentInputField.getText(), chatModeChooser.getValue(), messageRecipientField.getText());
			textContentInputField.clear();
			evt.consume();
		}
	}
}
