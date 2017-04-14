package frontend.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import frontend.util.BaseUIManager;
import frontend.wizards.selection_strategies.WizardSelectionStrategy;
import frontend.wizards.util.ButtonBar;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Wizard<T> extends BaseUIManager<Region> {
	private static final Collection<String> buttonNames = new ArrayList<>(
			Arrays.asList("Previous", "Cancel", "Next", "Finish"));

	private WizardSelectionStrategy<T> selectionStrategy;
	private BorderPane borderPane;
	private ButtonBar buttonBar;
	private Stage stage;

	public Wizard(WizardSelectionStrategy<T> selectionStrategy) {
		this(new Stage(), selectionStrategy);
	}

	public Wizard(Stage stage, WizardSelectionStrategy<T> selectionStrategy) {
		initialize(stage, selectionStrategy);
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
//		clearChanged();
		stage.close();
	}

	private void initialize(Stage stage, WizardSelectionStrategy<T> selectionStrategy) {
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
		
		// buttonBar.getButton("Next").addEventHandler(MouseEvent.MOUSE_CLICKED,
		// event -> setDefaultButton());
		// buttonBar.getButton("Cancel").addEventFilter(MouseEvent.MOUSE_CLICKED,
		// event -> setDefaultButton());
		// buttonBar.getButton("Finish").addEventFilter(MouseEvent.MOUSE_CLICKED,
		// event -> setDefaultButton());
		// setDefaultButton();
		
		borderPane.setCenter(selectionStrategy.getObject());
		borderPane.setBottom(buttonBar.getObject());

		stage.setScene(new Scene(borderPane));
		stage.show();
	}

//	private void setDefaultButton() {
//		Button finish = buttonBar.getButton("Finish");
//		Button next = buttonBar.getButton("Next");
//		Button cancel = buttonBar.getButton("Cancel");
//		finish.setDefaultButton(!finish.isDisabled());
//		next.setDefaultButton(finish.isDisabled() && !next.isDisabled());
//		cancel.setDefaultButton(finish.isDisabled() && next.isDisabled());
//		System.out.println("asdf");
//	}

	@Override
	public Region getObject() {
		return null;
	}

}
