package frontend.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import frontend.util.BaseUIManager;
import frontend.wizards.selection_strategies.WizardStrategy;
import frontend.wizards.util.ButtonBar;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * The Wizard class presents a basic API for allowing the user to create new
 * instances of objects while specifying initial information about the objects.
 * The Wizard extends BaseUIManager so that it can be styleable and can change
 * languages with the rest of the program. It is also updatable so that changes
 * in the rest of the program can be reflected immediately in the Wizard pages.
 * 
 * The Wizard uses the Strategy design pattern; it uses WizardStrategy
 * objects to populate the screens that the user interacts with. The Wizard
 * itself simply creates a next, previous, cancel, and finish dialog, and uses
 * the observable pattern to alert any listeners that are waiting for the object
 * to be instantiated.
 * 
 * @author Dylan Peters
 *
 * @param <T>
 *            The object type that the wizard will return after instantiation.
 */
public class Wizard<T> extends BaseUIManager<Region> {
	private static final Collection<String> buttonNames = new ArrayList<>(
			Arrays.asList("Previous", "Cancel", "Next", "Finish"));

	private WizardStrategy<T> selectionStrategy;
	private BorderPane borderPane;
	private ButtonBar buttonBar;
	private Stage stage;

	/**
	 * Creates a new Wizard object using the SelectionStrategy specified.
	 * Creates a new stage for the wizard. Sets all values to default.
	 * 
	 * @param selectionStrategy
	 *            WizardStrategy to use when populating the wizard's
	 *            pages.
	 */
	public Wizard(WizardStrategy<T> selectionStrategy) {
		this(new Stage(), selectionStrategy);
	}

	/**
	 * Creates a new Wizard object using the SelectionStrategy specified and
	 * displaying on the stage provided. Sets all values to default.
	 * 
	 * @param stage
	 *            the Stage on which to display the wizard
	 * @param selectionStrategy
	 *            WizardStrategy to use when populating the wizard's
	 *            pages.
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

	protected void previous() {
		selectionStrategy.previous();
	}

	protected void next() {
		selectionStrategy.next();
	}

	protected void cancel() {
		stage.close();
	}

	protected void finish() {
		setChanged();
		notifyObservers(selectionStrategy.finish());
		// clearChanged();
		stage.close();
	}

	private void initialize(Stage stage, WizardStrategy<T> selectionStrategy) {
		this.stage = stage;
		this.selectionStrategy = selectionStrategy;
		borderPane = new BorderPane();

		buttonBar = new ButtonBar(buttonNames);

		buttonBar.getButton("Previous").disableProperty().bind(selectionStrategy.canPrevious().not());
		buttonBar.getButton("Next").disableProperty().bind(selectionStrategy.canNext().not());
		buttonBar.getButton("Finish").disableProperty().bind(selectionStrategy.canFinish().not());

		buttonBar.getButton("Previous").setOnAction(event -> previous());
		buttonBar.getButton("Next").setOnAction(event -> next());
		buttonBar.getButton("Cancel").setOnAction(event -> cancel());
		buttonBar.getButton("Finish").setOnAction(event -> finish());

		borderPane.setCenter(selectionStrategy.getObject());
		borderPane.setBottom(buttonBar.getObject());

		stage.setScene(new Scene(borderPane));
		stage.show();
	}

}
