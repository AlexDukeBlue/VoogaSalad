package frontend.factory.wizard.strategies;

import backend.unit.properties.ActiveAbility;
import backend.unit.properties.ActiveAbility.AbilityEffect;
import backend.unit.properties.Attack;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.AttackPage;
import frontend.factory.wizard.strategies.wizard_pages.GridPatternPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.strategies.wizard_pages.ScriptingPage;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
	private AttackPage attackPage;
	private GridPatternPage gridPage;

	/**
	 * Creates a new instance of ActiveAbilityStrategy
	 * 
	 * @param gameState
	 */
	public ActiveAbilityStrategy(Controller controller) {
		super(controller);
		initialize();
	}

	/**
	 * Returns a fully instantiated ActiveAbility instance
	 */
	@Override
	public ActiveAbility<?> finish() {
		if (getPages().contains(attackPage)) {
			System.out.println(new ActiveAbility<>(namePage.getName(),
					new Attack(attackPage.getDamage(), attackPage.getNumHits(), attackPage.getModifiers()),
					gridPage.getGridPattern(), namePage.getDescriptionLabelBinding().getValueSafe(),
					namePage.getImagePath()).setSoundPath(namePage.getSoundPath()));
			return new ActiveAbility<>(namePage.getName(),
					new Attack(attackPage.getDamage(), attackPage.getNumHits(), attackPage.getModifiers()),
					gridPage.getGridPattern(), namePage.getDescriptionLabelBinding().getValueSafe(),
					namePage.getImagePath()).setSoundPath(namePage.getSoundPath());
		} else {
			return scriptingPage.getScriptEngine()
					.isPresent()
							? new ActiveAbility<>(namePage.getName(),
									(AbilityEffect<?>) (scriptingPage.getScriptEngine().get()),
									gridPage.getGridPattern(),
									namePage.getDescriptionLabelBinding().getValueSafe(), namePage.getImagePath())
											.setSoundPath(namePage.getSoundPath())
							: null;
		}
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage(getController(), "ActiveAbilityNameDescription", true);
		scriptingPage = new ScriptingPage(getController(), "ActiveAbilityScriptingDescription");
		attackPage = new AttackPage(getController(), "ActiveAbilityAttackDescription");
		gridPage = new GridPatternPage(getController(), "ActiveAbilityGridPatternDescription", Color.WHITE,
				Color.GREEN);
		getPages().addAll(namePage, getIsAttack() ? attackPage : scriptingPage, gridPage);
	}

	private boolean getIsAttack() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.getButtonTypes().add(ButtonType.YES);
		alert.getButtonTypes().add(ButtonType.NO);
		alert.getButtonTypes().remove(ButtonType.OK);
		alert.titleProperty().bind(getPolyglot().get("IsAttackPrompt"));
		alert.headerTextProperty().bind(getPolyglot().get("IsAttackHeader"));
		return alert.showAndWait().get().equals(ButtonType.YES);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("ActiveAbilityWizardTitle");
	}

}
