/**
 *
 */
package frontend.util;

import com.sun.javafx.collections.UnmodifiableObservableMap;
import controller.Controller;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * SlogoBaseUIManager is the base class for every front end class in the Slogo
 * program. It was designed to be powerful enough to add significant
 * functionality to all classes that extend it, while being flexible enough to
 * allow any UI class to extend it.
 * <p>
 * This class has three key parts that are critical to every UI component in a
 * JavaFX program: language, style, and an Object that is being managed.
 * <p>
 * This class contains an ObjectProperty for both the language and the
 * styleSheet. It supplies getter methods for both of those to allow client code
 * to listen for changes to the langauge or stylesheet, bind their language or
 * stylesheet to this, or change the language or stylesheet. Classes that extend
 * this can extend the getLanguage or getStyleSheet methods to make them return
 * readonly properties to prevent other classes from modifying them.
 * <p>
 * This class implements the ObjectManager interface, with the object being
 * managed being a Parent. This allows any class that extends this to have its
 * object be used by client code to be the root of a Scene.
 *
 * @author Dylan Peters
 */
public abstract class BaseUIManager<T extends Node> extends Observable implements ObjectManager<T>, Updatable {
	private static final String LANGUAGE_RESOURCE_POINTER = "resources.languages/LanguagePointer";
	private static final String LANGUAGE_RESOURCE_LIST = "resources.languages/LanguageFileList";
	private static final String DEFAULT_LANGUAGE_KEY = "DefaultLanguageResource";
	private static final String STYLESHEET_RESOURCE_POINTER = "resources.styles/StylePointer";
	private static final String STYLE_RESOURCE_LIST = "resources.styles/StyleFileList";
	private static final String DEFAULT_STYLE_KEY = "DefaultStyleSheet";

	private ObjectProperty<ResourceBundle> language;
	private ObjectProperty<String> styleSheet;
	private Controller controller;

	/**
	 * Creates a new SlogoBaseUIManager. Sets all values for the language and
	 * stylesheet to default. The default language is English.
	 * Yo Dylan wrong project lmao
	 */
	public BaseUIManager() {
		this(null);
	}

	public BaseUIManager(Controller controller) {
		language = new SimpleObjectProperty<>();
		language.setValue(createDefaultResourceBundle());
		styleSheet = new SimpleObjectProperty<>();
		styleSheet.addListener((observable, oldValue, newValue) -> {
			if (getObject() instanceof Parent) {
				((Parent) getObject()).getStylesheets().clear();
				((Parent) getObject()).getStylesheets().add(newValue);
			}
		});
//		styleSheet.setValue(ResourceBundle.getBundle(STYLESHEET_RESOURCE_POINTER).getString(DEFAULT_STYLE_KEY));
		setController(controller);
	}

	public void setController(Controller controller) {
		this.controller = controller;
		if (this.controller != null) {
			this.controller.removeFromUpdated(this); // prevent this from
			// getting updated twice
			this.controller.addToUpdated(this);
		}
	}

	public Controller getController() {
		return controller;
	}

	/**
	 * Gets an ObjectProperty containing the ResourceBundle that this class uses
	 * to populate any text that the user sees. The language bundle can be
	 * changed in order to change the language in which text displays. This
	 * method can be extended in subclasses to make the ObjectProperty that it
	 * returns readonly, so that other classes can listen to but cannot change
	 * the language.
	 *
	 * @return an ObjectProperty containing the ResourceBundle that this class
	 * uses to populate text that the user sees
	 */
	public ObjectProperty<ResourceBundle> getLanguage() {
		return language;
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
	 * that this class uses to style the Parent it manages.
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
	 * Generates a map whose keys are Strings that are the filepaths of all
	 * ResourceBundles that this class can use for its language, and whose
	 * values are the ResourceBundles themselves.
	 *
	 * @return a map whose keys are Strings that are the filepaths of all
	 * ResourceBundles that this class can use for its language, and
	 * whose values are the ResourceBundles themselves.
	 */
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
	 * possible stylesheets that this class can use, and whose values
	 * are the stylesheets themselves.
	 */
	protected final UnmodifiableObservableMap<String, String> getPossibleStyleSheetNamesAndFileNames() {
		Map<String, String> map = new HashMap<>();
		ResourceBundle fileBundle = ResourceBundle.getBundle(STYLE_RESOURCE_LIST);
		for (String key : fileBundle.keySet()) {
			map.put(getLanguage().getValue().getString(key), fileBundle.getString(key));
		}
		return (UnmodifiableObservableMap<String, String>) FXCollections
				.unmodifiableObservableMap(FXCollections.observableMap(map));
	}

	/**
	 * Creates a ResourceBundle that this class uses by default. The default
	 * ResourceBundle is specified in the bundle pointed to by the
	 * LANGUAGE_RESOURCE_POINTER field.
	 *
	 * @return a ResourceBundle that this class uses by default
	 */
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

}
