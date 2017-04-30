/**
 *
 */
package frontend.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sun.javafx.collections.UnmodifiableObservableMap;

import controller.Controller;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import util.polyglot.PolyglotException;
import util.polyglot_extended.ObservablePolyglot;

/**
 * BaseUIManager is the base class for almost every front end class in the
 * program. It was designed to be powerful enough to add significant
 * functionality to all classes that extend it, while being flexible enough to
 * allow any UI class to extend it.
 * <p>
 * This class has three key parts that are critical to every UI component in a
 * JavaFX program: language, style, and a Node that is being managed.
 * <p>
 * This class contains an ObjectProperty for both the language and the
 * styleSheet. It supplies getter methods for both of those to allow client code
 * to listen for changes to the langauge or stylesheet, bind their language or
 * stylesheet to this, or change the language or stylesheet. Classes that extend
 * this can extend the getLanguage or getStyleSheet methods to make them return
 * readonly properties to prevent other classes from modifying them.
 * <p>
 * This class implements the NodeManager interface, so that other classes know
 * they can access the UI component that the manager is manipulating and setting
 * up.
 * <p>
 * The ObjectProperty holding the language has been deprecated because we have
 * incorporated the Polyglot utility to allow us to translate into hundreds of
 * languages, instead of having to hand-write a resource bundle for every
 * language.
 *
 * @author Dylan Peters, Timmy Huang
 */
public abstract class BaseUIManager<T extends Node> extends Observable implements NodeManager<T>, UIComponentListener {
	private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();
	private static final Map<String, Media> MEDIA_CACHE = new HashMap<>();
	private static final String LANGUAGE_RESOURCE_POINTER = "resources.languages/LanguagePointer";
	private static final String LANGUAGE_RESOURCE_LIST = "resources.languages/LanguageFileList";
	private static final String DEFAULT_LANGUAGE_KEY = "DefaultLanguageResource";
	private static final String STYLESHEET_RESOURCE_POINTER = "resources.styles/StylePointer";
	private static final String STYLE_RESOURCE_LIST = "resources.styles/StyleFileList";
	private static final String DEFAULT_STYLE_KEY = "DefaultStyleSheet";
	private static final String API_KEY = "AIzaSyB-TQZwz6yDEvQfHTK2JdWNXLa1LfLXQz8";
	private static final Map<String, ObservablePolyglot> POLYGLOT_CACHE = new HashMap<>();
	private static final String DEFAULT_LANGUAGE = "English";

	private final ObjectProperty<String> styleSheet;
	private final Controller controller;
	private final ResourceBundle resources;
	private final String resourcePath;

	/**
	 * Creates a new BaseUIManager. Sets all values for the language and
	 * stylesheet to default. The default language is English.
	 */
	// public BaseUIManager() {
	// this(null);
	// }

	/**
	 * Creates a new BaseUIManager with a reference to the controller passed in.
	 * The getController() method will now return the controller that was passed
	 * as a param.
	 * 
	 * @param controller
	 *            the Controller object that this BaseUIManager will communicate
	 *            with.
	 */
	public BaseUIManager(Controller controller) {
		this.controller = controller;
		if (Objects.nonNull(controller)) {
			this.controller.removeListener(this);
			this.controller.addListener(this);
		}
		styleSheet = new SimpleObjectProperty<>();
		styleSheet.addListener((observable, oldValue, newValue) -> {
			if (getNode() instanceof Parent) {
				((Parent) getNode()).getStylesheets().clear();
				((Parent) getNode()).getStylesheets().add(newValue);
			}
		});
		resourcePath = getClass().getName().replace(".", "/").substring(0,
				getClass().getName().replace(".", "/").lastIndexOf("/")) + "/resources";
		resources = ResourceBundle.getBundle(resourcePath);
	}

	/**
	 * Returns the Controller object that this BaseUIManager is communicating
	 * with.
	 * 
	 * @return Controller object that this BaseUIManager is communicating with.
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * Gets an ObjectProperty containing a String pointing to the stylesheet
	 * that this class uses to style the Parent that it manages. The stylesheet
	 * can be changed in order to change the style of the parent. This method
	 * can be extended in subclasses to make the ObjectProperty that it returns
	 * readonly, so that other classes can listen to but cannot change the
	 * style.
	 *
	 * @return an ObjectProperty containing a String pointing to the stylesheet
	 *         that this class uses to style the Parent it manages.
	 */
	public ObjectProperty<String> getStyleSheet() {
		return styleSheet;
	}

	/**
	 * Tells BaseUIManager to update when the state of the program has changed.
	 * The default behavior is empty. Subclasses can add more behavior by
	 * Overriding the update method.
	 */
	public void update() {
	}

	/**
	 * Generates an ObservablePolyglot object from the ResourceBundle at the
	 * specified path.
	 * 
	 * @param resourcePath
	 *            the filepath to the resourcebundle that the Polyglot will use.
	 * @return ObservablePolyglot object that translates the resourcebundle.
	 */
	public static ObservablePolyglot getPolyglot(String resourcePath) {
		if (!POLYGLOT_CACHE.containsKey(resourcePath)) {
			try {
				POLYGLOT_CACHE.put(resourcePath, new ObservablePolyglot(API_KEY, resourcePath));
				POLYGLOT_CACHE.get(resourcePath).setLanguage(DEFAULT_LANGUAGE);
			} catch (PolyglotException e) {
				throw new Error(e);
			}
		}
		return POLYGLOT_CACHE.get(resourcePath);
	}

	/**
	 * Returns the polyglot instance that this BaseUIManager uses to translate.
	 * 
	 * @return polyglot instance that this BaseUIManager uses to translate.
	 */
	public ObservablePolyglot getPolyglot() {
		return getPolyglot(resourcePath);
	}

	/**
	 * Generates a map whose keys are Strings that are the filepaths of all
	 * ResourceBundles that this class can use for its language, and whose
	 * values are the ResourceBundles themselves.
	 * <p>
	 * This method has been deprecated because the program no longer uses
	 * multiple resourcebundles with different languages, but instead uses a
	 * single resourcebundle in English being translated by the Polyglot.
	 *
	 * @return a map whose keys are Strings that are the filepaths of all
	 *         ResourceBundles that this class can use for its language, and
	 *         whose values are the ResourceBundles themselves.
	 */
	@Deprecated
	protected final UnmodifiableObservableMap<String, ResourceBundle> getPossibleResourceBundleNamesAndResourceBundles() {
		Map<String, ResourceBundle> map = new HashMap<>();
		ResourceBundle bundle = ResourceBundle.getBundle(LANGUAGE_RESOURCE_LIST);
		for (String key : bundle.keySet()) {
			map.put(key, ResourceBundle.getBundle(bundle.getString(key)));
		}
		return (UnmodifiableObservableMap<String, ResourceBundle>) FXCollections
				.unmodifiableObservableMap(FXCollections.observableMap(map));
	}

	/**
	 * Generates a map whose keys are Strings that are the names of all the
	 * possible stylesheets that this class can use, and whose values are the
	 * stylesheets themselves.
	 *
	 * @return a map whose keys are Strings that are the names of all the
	 *         possible stylesheets that this class can use, and whose values
	 *         are the stylesheets themselves.
	 */
	protected final UnmodifiableObservableMap<String, String> getPossibleStyleSheetNamesAndFileNames() {
		Map<String, String> map = new HashMap<>();
		ResourceBundle fileBundle = ResourceBundle.getBundle(STYLE_RESOURCE_LIST);
		for (String key : fileBundle.keySet()) {
			map.put(key, fileBundle.getString(key));
		}
		return (UnmodifiableObservableMap<String, String>) FXCollections
				.unmodifiableObservableMap(FXCollections.observableMap(map));
	}

	/**
	 * Creates a ResourceBundle that this class uses by default. The default
	 * ResourceBundle is specified in the bundle pointed to by the
	 * LANGUAGE_RESOURCE_POINTER field.
	 * <p>
	 * This method has been deprecated because the program no longer uses
	 * multiple resourcebundles with different languages, but instead uses a
	 * single resourcebundle in English being translated by the Polyglot.
	 *
	 * @return a ResourceBundle that this class uses by default
	 */
	@Deprecated
	protected ResourceBundle createDefaultResourceBundle() {
		return ResourceBundle
				.getBundle(ResourceBundle.getBundle(LANGUAGE_RESOURCE_POINTER).getString(DEFAULT_LANGUAGE_KEY));
	}

	/**
	 * Creates a String pointing to a stylesheet that this class uses by
	 * default. The default stylesheet is specified in the bundle pointed to by
	 * the STYLESHEET_RESOURCE_POINTER field.
	 *
	 * @return a ResourceBundle that this class uses by default
	 */
	protected String createDefaultStyleSheet() {
		return ResourceBundle.getBundle(STYLESHEET_RESOURCE_POINTER).getString(DEFAULT_STYLE_KEY);
	}

	/**
	 * Returns the ResourceBundle that the BaseUIManager uses to access Strings
	 * and Objects.
	 * 
	 * @return the ResourceBundle that the BaseUIManager uses to access Strings
	 *         and Objects.
	 */
	protected ResourceBundle getResourceBundle() {
		return resources;
	}

	/**
	 * Returns an image from a cache. This prevents the program from slowing
	 * down if the same image is being loaded multiple times.
	 * 
	 * @param imgPath
	 *            filepath to the image
	 * @return Image at specified path.
	 */
	public final Image getImg(String imgPath) {
		if (!IMAGE_CACHE.containsKey(imgPath)) {
			try {
				IMAGE_CACHE.put(imgPath, new Image(new FileInputStream(imgPath)));
				if (getController() != null)
					getController().sendFile(imgPath);
			} catch (Exception e) {
				System.err.println("Error opening image: " + imgPath + "\t" + e.toString());
			}
		}
		return IMAGE_CACHE.getOrDefault(imgPath, IMAGE_CACHE.get(""));
	}

	/**
	 * Plays the sound file at the specified path.
	 * 
	 * @param mediaPath
	 *            the filepath to the sound to play.
	 */
	public final void playMedia(String mediaPath) {
		if (!MEDIA_CACHE.containsKey(mediaPath)) {
			try {
				MEDIA_CACHE.put(mediaPath, new Media(Paths.get(mediaPath).toUri().toString()));
				getController().sendFile(mediaPath);
			} catch (Exception e) {
				System.err.println("Error opening media: " + mediaPath + "\t" + e.toString());
			}
		}
		if (MEDIA_CACHE.containsKey(mediaPath)) {
			new MediaPlayer(MEDIA_CACHE.get(mediaPath)).play();
		}
	}

	/**
	 * Returns the filepaths to all ResourceBundles that have been accessed by
	 * subclasses of the BaseUIManager.
	 * 
	 * @return the filepaths to all ResourceBundles that have been accessed by
	 *         subclasses of the BaseUIManager.
	 */
	public static Collection<String> getResourcePaths() {
		return Stream.of(IMAGE_CACHE.keySet(), MEDIA_CACHE.keySet()).flatMap(Collection::stream)
				.collect(Collectors.toSet());
	}

	static {
		try {
			IMAGE_CACHE.put("", new Image(new FileInputStream("resources/images/transparent.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
