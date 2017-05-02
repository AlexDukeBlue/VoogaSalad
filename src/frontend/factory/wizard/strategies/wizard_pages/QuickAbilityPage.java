package frontend.factory.wizard.strategies.wizard_pages;

import java.util.HashMap;
import java.util.Map;

import backend.cell.Cell;
import backend.cell.ModifiableCell;
import backend.cell.Terrain;
import backend.grid.Shape;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.util.SliderInputRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * The WizardPage for creating and specifying a grid for a game
 * 
 * @author ncp14
 *
 */
public class QuickAbilityPage extends BaseWizardPage {

	private VBox vbox;
	private ComboBox<String> cellShapeChooser;
	private SliderInputRow rows, cols;
	private double minDamage = 0; //TODO Add this to resource file
	private double maxDamage  = 50; //TODO Add this to resource file
	private int minHits = 0; //TODO Add this to resource file
	private int maxHits  = 50; //TODO Add this to resource file
	Map<String, String[]> typesOfAbilities;

	/**
	 * Creates a new instance of GridInstantiationPage
	 * 
	 * @param descriptionKey
	 *            a String that can be used as a key to a ResourceBundle to set
	 *            the description of the page
	 */
	public QuickAbilityPage(Controller controller, String descriptionKey) {
		super(controller, descriptionKey);
		typesOfAbilities = new HashMap<String, String[]>();
		initialize();
	}
	
	/**
	 * Returns an integer for the number of rows the user wants the new grid to
	 * have.
	 * 
	 * @return an integer for the number of rows the user wants the new grid to
	 *         have.
	 */
	public int getRows() {
		return rows.getValue();
	}

	/**
	 * Returns an integer for the number of columns the user wants the new grid
	 * to have.
	 * 
	 * @return an integer for the number of columns the user wants the new grid
	 *         to have.
	 */
	public int getCols() {
		return cols.getValue();
	}


}
