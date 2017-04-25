package frontend.factory.wizard.wizards.strategies;

import backend.unit.Unit;
import backend.unit.properties.ActiveAbility;
import backend.unit.properties.ActiveAbility.AbilityEffect;
import backend.util.AuthoringGameState;
import frontend.factory.wizard.wizards.strategies.wizard_pages.GridPatternPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ScriptingPage;

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

	public ActiveAbilityStrategy(AuthoringGameState gameState) {
		initialize(gameState);
	}

	@Override
	public ActiveAbility<?> finish() {
		return scriptingPage.getScriptEngine().isPresent() ? new ActiveAbility<>(namePage.getName(), (AbilityEffect<?>)(scriptingPage.getScriptEngine().get()),gridPage.getGridPattern(), namePage.getDescription(), namePage.getImagePath()) : null;
	}
	
	private void initialize(AuthoringGameState gameState){
		namePage = new ImageNameDescriptionPage();
		scriptingPage = new ScriptingPage();
		gridPage = new GridPatternPage(gameState);
		getPages().addAll(namePage,scriptingPage,gridPage);
	}

}
