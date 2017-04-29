package frontend.factory.wizard.wizards.strategies.wizard_pages;

import java.util.Collection;
import java.util.stream.Collectors;

import backend.util.AuthoringGameState;
import frontend.factory.wizard.Wizard;
import frontend.factory.wizard.WizardFactory;
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

	public AdditionalWizardsPage(String descriptionKey, String wizardType, AuthoringGameState gameState) {
		super(descriptionKey);
		initialize(wizardType,gameState);
	}

	@Override
	public Region getNode() {
		return vbox;
	}

	public Collection<T> getObjects() {
		return wizardRows.stream().map(row -> row.getObjectProperty().getValue()).collect(Collectors.toList());
	}

	private void initialize(String wizardType,AuthoringGameState gameState) {
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
				while (wizardRows.size() < numRows) {
					Wizard<T> wizard = (Wizard<T>) WizardFactory.newWizard(wizardType, gameState);
					wizard.hide();
					wizardRows.add(new AdditionalWizardRow<>( getPolyglot().get("Description") , wizard));
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
