package frontend.factory.wizard.strategies;

import java.util.Optional;

import backend.grid.GridPattern;
import backend.unit.Unit;
import backend.unit.properties.ActiveAbility;
import backend.unit.properties.Attack;
import backend.unit.properties.ActiveAbility.AbilityEffect;
import controller.CommunicationController;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.AbilityCostPage;
import frontend.factory.wizard.strategies.wizard_pages.AttackPage;
import frontend.factory.wizard.strategies.wizard_pages.GridPatternPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.strategies.wizard_pages.QuickAbilityPage;
import frontend.factory.wizard.strategies.wizard_pages.QuickGameRulePage;
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
class QuickGameRuleStrategy extends BaseStrategy<ActiveAbility<?>> {

	//private ImageNameDescriptionPage namePage;
	private QuickGameRulePage quickRule;
	private String name;
	private String description;
	private String imagePath;
	private String soundPath;

	
	CommunicationController myController;

	/**
	 * Creates a new instance of ActiveAbilityStrategy
	 * 
	 * @param gameState
	 */
	public QuickGameRuleStrategy(Controller controller) {
		super(controller);
		myController = (CommunicationController) controller;
		initialize();
	}
	
	/**
	 * Returns a fully instantiated ActiveAbility instance
	 */
	@Override
	public ActiveAbility<?> finish() {
		
		System.out.println("Finished!!");
		name = myController.getQuickName();
		description = myController.getQuickDescription();
		imagePath = myController.getQuickImagePath();
		soundPath = myController.getQuickSoundPath();
		return null;
		
		
		// damage = quickAbility.getDamage();
		//int numHits = quickAbility.numHits();

		//return new ActiveAbility<>(name, new Attack(damage, numHits),gridPage.getGridPattern(), description, imagePath).setSoundPath(soundPath);
		
		/**
		return scriptingPage.getScriptEngine().isPresent() ? new ActiveAbility<>(namePage.getName(),
				(AbilityEffect<?>) (scriptingPage.getScriptEngine().get()), gridPage.getGridPattern(),
				namePage.getDescriptionLabelBinding().getValueSafe(), namePage.getImagePath()) : null;
				***/
	}
	

	private void initialize() {
		quickRule = new QuickGameRulePage(getController(),"QuickGameRuleTypeDescription");
		getPages().clear();
		getPages().addAll(quickRule);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("ActiveAbilityWizardTitle");
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


	
