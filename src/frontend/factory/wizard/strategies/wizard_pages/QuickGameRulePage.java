package frontend.factory.wizard.strategies.wizard_pages;

import java.util.HashMap;
import java.util.List;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * The WizardPage for creating and specifying a grid for a game
 * 
 * @author ncp14
 *
 */
public class QuickGameRulePage extends BaseWizardPage {

	private VBox vbox;
	private ComboBox<String> cellShapeChooser;
	private SliderInputRow rows, cols;
	private double numPoints = 0; //TODO Add this to resource file
	//private double maxDamage  = 50; //TODO Add this to resource file
	//private int minHits = 0; //TODO Add this to resource file
	//private int maxHits  = 50; //TODO Add this to resource file
	List<String> typesOfEndConditions;
	HBox hb1,hb2,hb3,hb4;

	/**
	 * Creates a new instance of GridInstantiationPage
	 * 
	 * @param descriptionKey
	 *            a String that can be used as a key to a ResourceBundle to set
	 *            the description of the page
	 */
	public QuickGameRulePage(Controller controller, String descriptionKey) {
		super(controller, descriptionKey);
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
		typesOfEndConditions.add(getResourceBundle().getString("EndTypeOne"));
		typesOfEndConditions.add(getResourceBundle().getString("EndTypeTwo"));
		typesOfEndConditions.add(getResourceBundle().getString("EndTypeThree"));
		typesOfEndConditions.add(getResourceBundle().getString("EndTypeFour"));
	}

	private void changeNames(String type)
	{
		if(type.equals(typesOfEndConditions.add(getResourceBundle().getString("EndTypeOne")))) typeOne();
		else if(type.equals(typesOfEndConditions.add(getResourceBundle().getString("EndTypeTwo")))) typeTwo();
		else if(type.equals(typesOfEndConditions.add(getResourceBundle().getString("EndTypeThree")))) typeThree();
		else if(type.equals(typesOfEndConditions.add(getResourceBundle().getString("EndTypeFour")))) typeFour();
	}
	
	public void typeOne()
	{
		hb1.setVisible(true);
		hb2.setVisible(false);
		hb3.setVisible(false);
	}
	
	public void typeTwo()
	{
		hb1.setVisible(false);
		hb2.setVisible(false);
		hb3.setVisible(false);
	}
	
	public void typeThree()
	{
		hb1.setVisible(false);
		hb2.setVisible(true);
		hb3.setVisible(false);
	}
	
	public void typeFour()
	{
		hb1.setVisible(false);
		hb2.setVisible(false);
		hb3.setVisible(true);
	}

	private void initialize() {
		hardcode();
		vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		ObservableList<String> abilityNames = FXCollections.observableArrayList();
		abilityNames.addAll(typesOfEndConditions);
		cellShapeChooser = new ComboBox<>(abilityNames);
		cellShapeChooser.setValue(abilityNames.get(0));
		cellShapeChooser.setOnAction(event -> changeNames(cellShapeChooser.getValue()));

		Label cellShape = new Label();
		cellShape.textProperty().bind(getPolyglot().get("Default_Game_Type"));
		HBox cellShapeBox = new HBox(cellShape, cellShapeChooser);
		
		Label label1 = new Label(getResourceBundle().getString("Enter_NumPoints"));
		TextField numPointsField = new TextField();
		hb1 = new HBox();
		hb1.getChildren().addAll(label1, numPointsField);
		hb1.setSpacing(10);
		
		Label label3 = new Label(getResourceBundle().getString("Enter_Unit"));
		TextField unitNameField = new TextField();
		hb2 = new HBox();
		hb2.getChildren().addAll(label3, unitNameField);
		hb2.setSpacing(10);
		
		Label label4 = new Label(getResourceBundle().getString("Enter_Terrain"));
		TextField terrainNameField = new TextField();
		hb3 = new HBox();
		hb3.getChildren().addAll(label4, terrainNameField);
		hb3.setSpacing(10);
		
		/**
		Button blankButtonOne = new Button();
		blankButtonOne.setVisible(false);
		Button blankButtonTwo = new Button();
		blankButtonTwo.setVisible(false);
		***/
		
		
		vbox.getChildren().addAll(cellShapeBox, hb1, hb2, hb3);
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

