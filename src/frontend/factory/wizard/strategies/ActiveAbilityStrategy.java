package frontend.factory.wizard.strategies;

import backend.unit.properties.ActiveAbility;
import backend.unit.properties.ActiveAbility.AbilityEffect;
import backend.util.AuthoringGameState;
import frontend.factory.wizard.strategies.wizard_pages.GridPatternPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.strategies.wizard_pages.ScriptingPage;
import javafx.beans.binding.StringBinding;

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

	/**
	 * Creates a new instance of ActiveAbilityStrategy
	 * 
	 * @param gameState
	 */
	public ActiveAbilityStrategy(AuthoringGameState gameState) {
		initialize(gameState);
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

	private void initialize(AuthoringGameState gameState) {
		namePage = new ImageNameDescriptionPage("ActiveAbilityNameDescription");
		scriptingPage = new ScriptingPage("ActiveAbilityScriptingDescription");
		gridPage = new GridPatternPage(gameState, "ActiveAbilityGridPatternDescription");
		getPages().addAll(namePage, scriptingPage, gridPage);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("ActiveAbilityWizardTitle");
	}

}
