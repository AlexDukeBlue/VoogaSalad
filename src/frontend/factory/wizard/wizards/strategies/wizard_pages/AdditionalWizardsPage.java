package frontend.factory.wizard.wizards.strategies.wizard_pages;

import java.util.Collection;
import java.util.stream.Collectors;

import frontend.factory.wizard.Wizard;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.AdditionalWizardRow;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.NumericInputRow;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AdditionalWizardsPage<T> extends BaseWizardPage {

	private VBox vbox;
	private NumericInputRow numWizardRow;
	private ObservableList<AdditionalWizardRow<T>> wizardRows;

	public AdditionalWizardsPage(String title, String description, Class<? extends Wizard<T>> clazz) {
		super(title, description);
		initialize(clazz);
	}

	@Override
	public Region getObject() {
		return vbox;
	}

	public Collection<T> getObjects() {
		return wizardRows.stream().map(row -> row.getObjectProperty().getValue()).collect(Collectors.toList());
	}

	private void initialize(Class<? extends Wizard<T>> clazz) {
		vbox = new VBox();
		wizardRows = FXCollections.observableArrayList();
		wizardRows.addListener((ListChangeListener<AdditionalWizardRow<T>>) listChange -> {
			listChange.next();
			listChange.getAddedSubList().stream().forEach(row -> {
				vbox.getChildren().add(row.getObject());
				row.getObjectProperty().addListener(change -> checkCanNext());
			});
			listChange.getRemoved().stream().forEach(row -> {
				vbox.getChildren().remove(row.getObject());
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
					wizardRows.add(new AdditionalWizardRow<>( getPolyglot().get("Description") , wizard));
				}
			} catch (Exception e) {
				notifyUser(e);
			}
			while (wizardRows.size() > numRows) {
				wizardRows.remove(wizardRows.get(wizardRows.size() - 1));
			}
		});
		vbox.getChildren().addAll(numWizardRow.getObject());
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
		//alert.setTitle("Error");
		alert.titleProperty().bind(getPolyglot().get("Error"));
		//alert.setHeaderText("Could not instantiate proper wizards.");
		alert.headerTextProperty().bind(getPolyglot().get("No_Instantiate_Wizard_Message"));
		//alert.setContentText("Try entering a smaller number.");
		alert.contentTextProperty().bind(getPolyglot().get("No_Instantiate_Wizard_Content"));
		alert.showAndWait();
	}

}
