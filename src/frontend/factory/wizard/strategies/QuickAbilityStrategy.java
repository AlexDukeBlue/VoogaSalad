package frontend.factory.wizard.strategies;

import java.util.Optional;

import backend.grid.GridPattern;
import backend.unit.Unit;
import backend.unit.properties.ActiveAbility;
import backend.unit.properties.Attack;
import backend.unit.properties.ActiveAbility.AbilityEffect;
import controller.CommunicationController;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.GridPatternPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.strategies.wizard_pages.QuickAbilityPage;
import frontend.factory.wizard.strategies.wizard_pages.ScriptingPage;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

/**
 * ActiveAbilityStrategy implements the SelectionStrategy interface in order to
 * allow the user to instantiate new Attacks.
 * 
 * @author ncp14
 *
 */
class QuickAbilityStrategy extends BaseStrategy<ActiveAbility<?>> {

	//private ImageNameDescriptionPage namePage;
	private QuickAbilityPage quickAbility;
	private GridPatternPage gridPage;
	private String name;
	private String description;
	private String imagePath;
	
	CommunicationController myController;

	/**
	 * Creates a new instance of ActiveAbilityStrategy
	 * 
	 * @param gameState
	 */
	public QuickAbilityStrategy(Controller controller) {
		super(controller);
		myController = (CommunicationController) controller;
		initialize();
	}
	
	/**
	 * Returns a fully instantiated ActiveAbility instance
	 */
	@Override
	public ActiveAbility<?> finish() {
		System.out.println("got here");
		name = myController.getQuickName();
		description = myController.getQuickDescription();
		imagePath = myController.getQuickImagePath();
		
		double damage = quickAbility.getDamage();
		int numHits = quickAbility.numHits();
		System.out.println("Name: " + name);
		System.out.println("description: " + description);
		System.out.println("imagePath: " +imagePath);
		
		ActiveAbility<Unit> newUnit = new ActiveAbility<>(name, new Attack(damage, numHits),gridPage.getGridPattern(), description, imagePath);	
		System.out.println("new unit: " + newUnit);
		return newUnit;
		
		/**
		return scriptingPage.getScriptEngine().isPresent() ? new ActiveAbility<>(namePage.getName(),
				(AbilityEffect<?>) (scriptingPage.getScriptEngine().get()), gridPage.getGridPattern(),
				namePage.getDescriptionLabelBinding().getValueSafe(), namePage.getImagePath()) : null;
				***/
	}
	

	private void initialize() {
		quickAbility = new QuickAbilityPage(getController(),"QuickAbilityTypeDescription");
		gridPage = new GridPatternPage(getController(), "QuickGridPatternRangeDescription", Color.WHITE, Color.GREEN);
		getPages().addAll(quickAbility,gridPage);

	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("ActiveAbilityWizardTitle");
	}
	
	private boolean isARangedUnit()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Ranged");
		alert.setHeaderText("Will this ability affect other units in addition to the one it is attached to?");
		alert.setContentText("Select yes if this active ability has a range.");

		ButtonType buttonTypeOne = new ButtonType("Yes");
		ButtonType buttonTypeTwo = new ButtonType("No");

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

		Optional<ButtonType> result = alert.showAndWait();
		return (result.get() == buttonTypeOne);
	}
	
	//Perhaps not needed
		public void setName(String name)
		{
			this.name = name;
		}
		
		public void setDescription(String description)
		{
			this.description = description;
		}
		
		public void setImagePath(String imagePath)
		{
			this.imagePath = imagePath;
		}



	}


	
