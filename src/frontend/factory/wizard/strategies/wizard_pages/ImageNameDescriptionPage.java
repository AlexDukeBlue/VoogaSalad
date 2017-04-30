package frontend.factory.wizard.strategies.wizard_pages;

import java.io.File;
import java.nio.file.Paths;

import controller.Controller;
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

/**
 * The WizardPage for creating any object that needs to include an image, name,
 * and/or description
 *
 * @author Andreas
 */
public class ImageNameDescriptionPage extends BaseWizardPage {

	private HBox hbox;
	private VBox imageBox;
	private Button uploadButton, soundButton;
	private ImageView imageView, soundView;
	private String imagePath, soundPath;
	private TextField nameField;
	private TextArea descriptionField;

	/**
	 * Creates a new instance of ImageNameDescriptionPage.
	 * 
	 * @param descriptionKey
	 *            a String that can be used as a key to a ResourceBundle to set
	 *            the description of the page
	 */
	public ImageNameDescriptionPage(Controller controller, String descriptionKey, Boolean sound) {
		super(controller, descriptionKey);
		initialize(sound);
	}
	
	public ImageNameDescriptionPage(Controller controller, String descriptionKey){
		this(controller, descriptionKey, false);
	}

	@Override
	public Region getNode() {
		return hbox;
	}

	public Image getImage() {
		return imageView.getImage();
	}
	
	public String getSoundPath(){
		return soundPath;
	}

	/**
	 * Returns the name that the user input into the WizardPage for the
	 * VoogaEntity to have.
	 * 
	 * @return the name that the user input into the WizardPage for the
	 *         VoogaEntity to have.
	 */
	public String getName() {
		return nameField.getText();
	}

	/**
	 * Returns the description that the user input into the WizardPage for the
	 * VoogaEntity to have.
	 * 
	 * @return the description that the user input into the WizardPage for the
	 *         VoogaEntity to have.
	 */
	public String getDescriptionBoxText() {
		return descriptionField.getText();
	}

	/**
	 * Returns the imagePath that the user input into the WizardPage for the
	 * VoogaEntity to have.
	 * 
	 * @return the imagePath that the user input into the WizardPage for the
	 *         VoogaEntity to have.
	 */
	public String getImagePath() {
		return imagePath;
	}

	private void initialize(Boolean sound) {
		imagePath = "";
		hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(Double.parseDouble(getResourceBundle().getString("DEFAULT_INSETS"))));
		hbox.setSpacing(Double.parseDouble(getResourceBundle().getString("DEFAULT_SPACING")));
		uploadButton = new Button();
		uploadButton.textProperty().bind(getPolyglot().get("Upload_Image"));
		imageView = new ImageView();
		nameField = new TextField();
		nameField.promptTextProperty().bind(getPolyglot().get("Name"));
		descriptionField = new TextArea();
		descriptionField.promptTextProperty().bind(getPolyglot().get("Description"));
		imageBox = new VBox();
		imageBox.getChildren().addAll(imageView, uploadButton);
		if (sound){
			initSound();
		}
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

	private void initSound() {
		soundButton = new Button();
		soundButton.textProperty().bind(getPolyglot().get("Upload_Sound"));
		imageBox.getChildren().add(soundButton);
		soundButton.setOnAction(e -> setSound());
		
	}

	private void setSound() {
		FileChooser choose = new FileChooser();
		choose.setInitialDirectory(new File(System.getProperty("user.dir")));
		choose.getExtensionFilters().setAll(new ExtensionFilter("Sound Files", "*.mp3", "*.wav"));
		File file = choose.showOpenDialog(null);
		if (file != null){
			String soundViewPath = "img/sound-button-png-image-87151.png";
			soundView = new ImageView(getImg(soundViewPath));
			soundView.fitWidthProperty().bind(imageBox.widthProperty());
			soundView.fitHeightProperty().bind(imageView.fitWidthProperty());
			imageBox.getChildren().add(soundView);
			
			soundPath = Paths.get(System.getProperty("user.dir")).relativize(Paths.get(file.getPath())).toString();
			soundView.setOnMouseClicked(e -> playMedia(soundPath));
		}
	}

	private void setImage() {
		FileChooser choose = new FileChooser();
		choose.setInitialDirectory(new File(System.getProperty("user.dir")));
		choose.getExtensionFilters().setAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		File file = choose.showOpenDialog(null);
		if (file != null) {
			imagePath = Paths.get(System.getProperty("user.dir")).relativize(Paths.get(file.getPath())).toString();
			imageView.setImage(getImg(imagePath));
		}
	}

	private void checkIfCanNext() {
		canNextWritable().setValue(true);
	}
}
