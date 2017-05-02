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
	
