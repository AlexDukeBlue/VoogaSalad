package frontend.factory.wizard.strategies;

import backend.util.Actionable;
import controller.Controller;
import javafx.beans.binding.StringBinding;

/**
 * TurnActionStrategy is a WizardStrategy that gathers information from the user
 * required to instantiate a new TurnAction.
 * 
 * @author Dylan Peters
 *
 */
class TurnActionStrategy extends NameScriptBaseStrategy<Actionable> {

	/**
	 * Creates a new instance of TurnActionStrategy.
	 */
	public TurnActionStrategy(Controller controller) {
		super(controller, "TurnActionNamePageDescription", "TurnActionScriptingPageDescription");
		setScriptPrompt("TurnAction_Example_");
	}

	/**
	 * Returns a fully instantiated Actionable object.
	 */
	@Override
	public Actionable finish() {
		return new Actionable(getScriptEngine(), getName(), getDescriptionBoxText(), getImgPath());
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("TurnActionWizardTitle");
	}

}
