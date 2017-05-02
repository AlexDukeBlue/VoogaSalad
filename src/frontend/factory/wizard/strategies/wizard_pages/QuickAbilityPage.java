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

	private void hardcode()
	{
		System.out.println("got here");
		String[] arrayOne = {getResourceBundle().getString("Enter_Damage"), getResourceBundle().getString("Enter_NumHits")};
		String[] arrayTwo = {getResourceBundle().getString("Enter_Health"), getResourceBundle().getString("Enter_NumHealed")};
		String[] arrayThree = {getResourceBundle().getString("Enter_Health"), getResourceBundle().getString("Enter_NumHealed")};

		typesOfAbilities.put("Attacking", arrayOne);
		typesOfAbilities.put("Defending", arrayTwo);
		typesOfAbilities.put("Healing", arrayThree);
		System.out.println("left here");
	}

	private void changeNames(String type)
	{
		String[] newValues = typesOfAbilities.get(type);
		rows.setName(newValues[0]);
		cols.setName(newValues[1]);
	}

	private void initialize() {
		hardcode();
		vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		ObservableList<String> abilityNames = FXCollections.observableArrayList();
		for (String key : typesOfAbilities.keySet())
		{
			abilityNames.add(key);
		}
		cellShapeChooser = new ComboBox<>(abilityNames);
		cellShapeChooser.setValue(abilityNames.get(0));
		cellShapeChooser.setOnAction(event -> changeNames(cellShapeChooser.getValue()));

		Label cellShape = new Label();
		cellShape.textProperty().bind(getPolyglot().get("Default_Ability_Type"));
		HBox cellShapeBox = new HBox(cellShape, cellShapeChooser);
		
		double midDamage = (maxDamage - minDamage) /2;
		int midNumHits = (maxHits - minHits)/2;

		rows = new SliderInputRow(null, "Enter damage:");
		rows.setMin((int) minDamage);
		rows.setMax((int) maxDamage);
		rows.setValue((int) midDamage);
		cols = new SliderInputRow(null, "Enter num hits");
		cols.setMin(minHits);
		cols.setMax(maxHits);
		cols.setValue(midNumHits);
		
		Button blankButtonOne = new Button();
		blankButtonOne.setVisible(false);
		Button blankButtonTwo = new Button();
		blankButtonTwo.setVisible(false);
		
		Button changeDamage = new Button();
		changeDamage.setText("Edit max/min");
		
		Button changeHits = new Button();
		changeHits.setText("Edit max/min");
		
		HBox damageBox = new HBox();
		damageBox.getChildren().addAll(rows.getNode(), blankButtonOne, changeDamage);
		
		HBox hitBox = new HBox();
		hitBox.getChildren().addAll(cols.getNode(), blankButtonTwo, changeHits);
		
		vbox.getChildren().addAll(cellShapeBox, blankButtonOne, damageBox, blankButtonTwo, hitBox);
		System.out.println("end initialize");
		canNextWritable().setValue(true);
	}
	
	public double getDamage()
	{
		return rows.getValue();
	}
	
	public int numHits()
	{
		return cols.getValue();
	}

	@Override
	public Region getNode() {
		return vbox;
	}

}

