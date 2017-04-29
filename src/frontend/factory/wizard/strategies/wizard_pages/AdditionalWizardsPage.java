package frontend.factory.wizard.strategies.wizard_pages;

import java.util.Collection;
import java.util.stream.Collectors;

import controller.Controller;
import frontend.factory.wizard.Wizard;
import frontend.factory.wizard.strategies.wizard_pages.util.AdditionalWizardRow;
import frontend.factory.wizard.strategies.wizard_pages.util.NumericInputRow;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * AdditionalWizardsPage is a WizardPage that allows the user to create a number
 * of objects by launching other wizards. The user specifies what object to
 * create and then can launch an arbitrary number of other wizards to create
 * those objects. This allows a single wizard to spawn new sub-wizards.
 * 
 * @author Dylan Peters
 *
 * @param <T>
 *            The type of object that the new wizards will create
 */
public class AdditionalWizardsPage<T> extends BaseWizardPage {

	private VBox vbox;
	private NumericInputRow numWizardRow;
	private ObservableList<AdditionalWizardRow<T>> wizardRows;

	/**
	 * Creates a new instance of AdditionalWizardsPage. Sets all values to
	 * default.
	 * 
	 * @param descriptionKey
	 *            a String that can be used as a key to a ResourceBundle to set
	 *            the description of the page
	 * @param clazz
	 *            the class of wizard that this class will create
	 */
	public AdditionalWizardsPage(Controller controller, String descriptionKey, Class<? extends Wizard<T>> clazz) {
		super(controller, descriptionKey);
		initialize(clazz);
	}

	@Override
	public Region getNode() {
		return vbox;
	}

	/**
	 * Returns a collection of the objects that have been created by the new
	 * wizards spawned by this wizard page.
	 * 
	 * @return a collection of the objects of type T that have been created by
	 *         the new wizards spawned by this wizard page.
	 */
	public Collection<T> getObjects() {
		return wizardRows.stream().map(row -> row.getObjectProperty().getValue()).collect(Collectors.toList());
	}

	private void initialize(Class<? extends Wizard<T>> clazz) {
		vbox = new VBox();
		wizardRows = FXCollections.observableArrayList();
		wizardRows.addListener((ListChangeListener<AdditionalWizardRow<T>>) listChange -> {
			listChange.next();
			listChange.getAddedSubList().stream().forEach(row -> {
				vbox.getChildren().add(row.getNode());
				row.getObjectProperty().addListener(change -> checkCanNext());
			});
			listChange.getRemoved().stream().forEach(row -> {
				vbox.getChildren().remove(row.getNode());
			});
			checkCanNext();
		});
		numWizardRow = new NumericInputRow(null, getPolyglot().get("Number_of_Items"), getPolyglot().get("Items"));
		numWizardRow.addObserver((observable, object) -> {
			int numRows = (Integer) object;
			try {
				while (wizardRows.size() < numRows) {
					Wizard<T> wizard = clazz.newInstance();
					wizard.hide();
					wizardRows.add(new AdditionalWizardRow<>(getPolyglot().get("Description"), wizard));
				}
			} catch (Exception e) {
				notifyUser(e);
			}
			while (wizardRows.size() > numRows) {
				wizardRows.remove(wizardRows.get(wizardRows.size() - 1));
			}
		});
		vbox.getChildren().addAll(numWizardRow.getNode());
		checkCanNext();
	}

	private void checkCanNext() {
		canNextWritable().setValue(true);
		wizardRows.stream().forEach(row -> {
			if (row.getObjectProperty().getValue() == null) {
				canNextWritable().setValue(false);
			}
		});
	}

	private void notifyUser(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.titleProperty().bind(getPolyglot().get("Error"));
		alert.headerTextProperty().bind(getPolyglot().get("No_Instantiate_Wizard_Message"));
		alert.contentTextProperty().bind(getPolyglot().get("No_Instantiate_Wizard_Content"));
		alert.showAndWait();
	}

}
