package frontend.wizards.strategies.wizard_pages;

import frontend.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;

/**
 * The WizardPage for creating any object that needs to include an image, name, and/or description
 *
 * @author Andreas
 */
public class ImageNameDescriptionPage extends BaseWizardPage {
	private static final String DEFAULT_TITLE = "Set Image, Name, and Description";
	private static final String DEFAULT_DESCRIPTION = "You must choose a file for the image and set the name. The description is optional.";
	private static final double DEFAULT_INSETS = 10;
	private static final double DEFAULT_SPACING = 10;

	private HBox hbox;
	private Button uploadButton;
	private ImageView imageView;
	private String imagePath;
	private TextField nameField;
	private TextArea descriptionField;

	public ImageNameDescriptionPage() {
		this(DEFAULT_TITLE);
	}

	public ImageNameDescriptionPage(String title) {
		this(title, DEFAULT_DESCRIPTION);
	}

	public ImageNameDescriptionPage(String title, String description) {
		super(title, description);
		initialize();
	}

	@Override
	public Region getObject() {
		return hbox;
	}

	public Image getImage() {
		return imageView.getImage();
	}

	public String getName() {
		return nameField.getText();
	}

	public String getDescriptionBoxText() {
		return descriptionField.getText();
	}

	public String getImagePath() {
		return imagePath;
	}

	private void initialize() {
		imagePath = "";
		hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(DEFAULT_INSETS));
		hbox.setSpacing(DEFAULT_SPACING);
		uploadButton = new Button("Upload Image");
		imageView = new ImageView();
		nameField = new TextField();
		nameField.setPromptText("Name");
		descriptionField = new TextArea();
		descriptionField.setPromptText("Description (optional)");
		VBox imageBox = new VBox();
		imageBox.getChildren().addAll(imageView, uploadButton);
		VBox nameAndDescription = new VBox();
		nameAndDescription.getChildren().addAll(nameField, descriptionField);
		hbox.getChildren().addAll(imageBox, nameAndDescription);
		uploadButton.setOnAction(event -> setImage());
		imageView.fitWidthProperty().bind(imageBox.widthProperty());
		imageView.fitHeightProperty().bind(imageView.fitWidthProperty());
		imageView.imageProperty().addListener((observable, oldValue, newValue) -> checkIfCanNext());
		nameField.textProperty().addListener((observable, oldValue, newValue) -> checkIfCanNext());
		descriptionField.textProperty().addListener((observable, oldValue, newValue) -> checkIfCanNext());
		checkIfCanNext();
	}

	private void setImage() {
		FileChooser choose = new FileChooser();
		choose.setInitialDirectory(new File(System.getProperty("user.dir")));
		choose.getExtensionFilters().setAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		File file = choose.showOpenDialog(null);
		if (file != null) {
			imageView.setImage(View.getImg(file.toURI().toString()));
			imagePath = file.toURI().toString(); //todo make this a relative path
		}
	}

	private void checkIfCanNext() {
		canNextWritable().setValue(/*imageView.getImage() != null && !nameField.getText().isEmpty()))*/ true);
	}
}
