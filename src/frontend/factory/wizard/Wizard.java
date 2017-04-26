package frontend.factory.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import frontend.factory.wizard.wizards.strategies.WizardStrategy;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.ButtonBar;
import frontend.util.BaseUIManager;
import javafx.beans.binding.StringBinding;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import polyglot.PolyglotException;

/**
 * The Wizard class presents a basic API for allowing the user to create new
 * instances of objects while specifying initial information about the objects.
 * The Wizard extends BaseUIManager so that it can be styleable and can change
 * languages with the rest of the program. It is also updatable so that changes
 * in the rest of the program can be reflected immediately in the Wizard pages.
 * 
 * The Wizard uses the Strategy design pattern; it uses WizardStrategy objects
 * to populate the screens that the user interacts with. The Wizard itself
 * simply creates a next, previous, cancel, and finish dialog, and uses the
 * observable pattern to alert any listeners that are waiting for the object to
 * be instantiated.
 * 
 * @author Dylan Peters
 *
 * @param <T>
 *            The object type that the wizard will return after instantiation.
 */
public class Wizard<T> extends BaseUIManager<Region> {
	private static final Collection<String> buttonNames = new ArrayList<>(
			Arrays.asList("Cancel", "Previous", "Next", "Finish"));

	private WizardStrategy<T> selectionStrategy;
	private BorderPane borderPane;
	private Stage stage;
	private Map<String,StringBinding> buttonBindings;

	/**
	 * Creates a new Wizard object using the SelectionStrategy specified.
	 * Creates a new stage for the wizard. Sets all values to default.
	 * 
	 * @param selectionStrategy
	 *            WizardStrategy to use when populating the wizard's pages.
	 */
	Wizard(WizardStrategy<T> selectionStrategy) {
		this(new Stage(), selectionStrategy);
	}

	/**
	 * Creates a new Wizard object using the SelectionStrategy specified and
	 * displaying on the stage provided. Sets all values to default.
	 * 
	 * @param stage
	 *            the Stage on which to display the wizard
	 * @param selectionStrategy
	 *            WizardStrategy to use when populating the wizard's pages.
	 */
	Wizard(Stage stage, WizardStrategy<T> selectionStrategy) {
		initialize(stage, selectionStrategy);
	}

	/**
	 * Returns the object that this wizard uses to display to the user.
	 */
	@Override
	public Region getObject() {
		return borderPane;
	}

	public void show() {
		stage.show();
	}

	public void hide() {
		stage.hide();
	}

	private void previous() {
		selectionStrategy.previous();
	}

	private void next() {
		selectionStrategy.next();
	}

	private void cancel() {
		stage.close();
	}

	private void finish() {
		setChanged();
		notifyObservers(selectionStrategy.finish());
		clearChanged();
		stage.close();
	}

	private void initialize(Stage stage, WizardStrategy<T> selectionStrategy) {
		this.stage = stage;
		this.selectionStrategy = selectionStrategy;
		borderPane = new BorderPane();
		buttonBindings = new HashMap<String,StringBinding>();
		ArrayList<StringBinding> stringBindings = new ArrayList<>();
		buttonNames.stream().forEachOrdered(e -> {
			StringBinding binding = getPolyglot().get(e);
			buttonBindings.put(e,binding);
			stringBindings.add(binding);
		});
		ButtonBar buttonBar = new ButtonBar(stringBindings);
		buttonBar.getButton(buttonBindings.get("Previous")).disableProperty().bind(selectionStrategy.canPrevious().not());
		buttonBar.getButton(buttonBindings.get("Next")).disableProperty().bind(selectionStrategy.canNext().not());
		buttonBar.getButton(buttonBindings.get("Next")).setDefaultButton(true);
		buttonBar.getButton(buttonBindings.get("Finish")).disableProperty().bind(selectionStrategy.canFinish().not());
		buttonBar.getButton(buttonBindings.get("Finish")).setDefaultButton(true);
		buttonBar.getButton(buttonBindings.get("Previous")).setOnAction(event -> previous());
		buttonBar.getButton(buttonBindings.get("Next")).setOnAction(event -> next());
		buttonBar.getButton(buttonBindings.get("Cancel")).setOnAction(event -> cancel());
		buttonBar.getButton(buttonBindings.get("Cancel")).setCancelButton(true);
		buttonBar.getButton(buttonBindings.get("Finish")).setOnAction(event -> finish());

		borderPane.setCenter(selectionStrategy.getObject());
		borderPane.setBottom(buttonBar.getObject());
		
		getPolyglot().setOnLanguageChange(event -> {
			try {
				selectionStrategy.getPolyglot().setLanguage(getPolyglot().getLanguage());
			} catch (PolyglotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		stage.setScene(new Scene(borderPane));
		show();
	}

}