package frontend.factory.wizard.strategies;

import backend.unit.properties.ActiveAbility;
import backend.unit.properties.ActiveAbility.AbilityEffect;
import controller.CommunicationController;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.GridPatternPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.strategies.wizard_pages.ScriptingPage;
import javafx.beans.binding.StringBinding;
import javafx.scene.paint.Color;

/**
 * ActiveAbilityStrategy implements the SelectionStrategy interface in order to
 * allow the user to instantiate new Attacks.
 * 
 * @author Dylan Peters
 *
 */
class ActiveAbilityStrategy extends BaseStrategy<ActiveAbility<?>> {

	private ImageNameDescriptionPage namePage;
	private ScriptingPage scriptingPage;
	private GridPatternPage gridPage;
	
	private boolean counter; //Used to do a certain action between pages. Please do not remove
	CommunicationController myController;

	/**
	 * Creates a new instance of ActiveAbilityStrategy
	 * 
	 * @param gameState
	 */
	public ActiveAbilityStrategy(Controller controller) {
		super(controller);
		counter = false; //Initialization
		myController = (CommunicationController) controller;
		initialize();
	}

	/**
	 * Returns a fully instantiated ActiveAbility instance
	 */
	@Override
	public ActiveAbility<?> finish() {
		return scriptingPage.getScriptEngine().isPresent() ? new ActiveAbility<>(namePage.getName(),
				(AbilityEffect<?>) (scriptingPage.getScriptEngine().get()), gridPage.getGridPattern(),
				namePage.getDescriptionLabelBinding().getValueSafe(), namePage.getImagePath()) : null;
	}
	
	@Override
	public void next()
	{
		super.next();
		if(!counter)
		{
			myController.setQuickName(namePage.getName());
			myController.setQuickDescription(namePage.getDescriptionBoxText());
			myController.setQuickImagePath(namePage.getImagePath());
		}
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage(getController(),"ActiveAbilityNameDescription");
		scriptingPage = new ScriptingPage(getController(),"ActiveAbilityScriptingDescription");
		gridPage = new GridPatternPage(getController(), "ActiveAbilityGridPatternDescription", Color.WHITE, Color.GREEN);
		getPages().addAll(namePage, scriptingPage, gridPage);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("ActiveAbilityWizardTitle");
	}

}
