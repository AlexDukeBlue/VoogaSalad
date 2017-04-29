package frontend.factory.wizard.strategies.wizard_pages;

import java.util.Optional;

import frontend.util.ScriptingDialog;
import javafx.scene.layout.Region;
import util.scripting.VoogaScriptEngine;

/**
 * A WizardPage that allows the user to script in different scripting languages.
 * Does not allow the user to advance to the next page until he/she has created
 * a script that compiles.
 * 
 * @author Timmy Huang
 *
 */
public class ScriptingPage extends BaseWizardPage {

	private ScriptingDialog dialog;

	/**
	 * Creates a new instance of ScriptingPage.
	 * 
	 * @param descriptionKey
	 *            a String that can be used as a key to a ResourceBundle to set
	 *            the description of the page
	 */
	public ScriptingPage(String descriptionKey) {
		super(descriptionKey);
		dialog = new ScriptingDialog();
		canNextWritable().bind(dialog.hasCompiled());
	}

	public Optional<VoogaScriptEngine> getScriptEngine() {
		return dialog.getScriptEngine();
	}

	public void setPrompt(String strat) {
		dialog.setPrompt(strat);
	}

	public ScriptingDialog getDialog() {
		return dialog;
	}

	@Override
	public Region getNode() {
		return dialog.getNode();
	}

}
