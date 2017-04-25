package frontend.factory.wizard.wizards.strategies.wizard_pages;

import java.util.Optional;

import frontend.util.ScriptingDialog;
import javafx.scene.layout.Region;
import util.scripting.VoogaScriptEngine;

public class ScriptingPage extends BaseWizardPage {

	private ScriptingDialog dialog;

	public ScriptingPage() {
		dialog = new ScriptingDialog();
		canNextWritable().bind(dialog.hasCompiled());
	}

	public Optional<VoogaScriptEngine> getScriptEngine() {
		return dialog.getScriptEngine();
	}

	@Override
	public Region getObject() {
		return dialog.getObject();
	}

}
