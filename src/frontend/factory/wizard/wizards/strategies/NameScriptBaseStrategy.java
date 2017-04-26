package frontend.factory.wizard.wizards.strategies;

import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ScriptingPage;
import util.scripting.VoogaScriptEngine;

public abstract class NameScriptBaseStrategy<T> extends BaseStrategy<T> {

	private ImageNameDescriptionPage namePage;
	private ScriptingPage scriptingPage;

	public NameScriptBaseStrategy() {
		initialize();
	}

	protected String getName() {
		return namePage.getName();
	}

	protected String getDescription() {
		return namePage.getDescription().getValue();
	}
	
	protected String getImgPath(){
		return namePage.getImagePath();
	}

	protected VoogaScriptEngine getScriptEngine() {
		return scriptingPage.getScriptEngine().isPresent() ? scriptingPage.getScriptEngine().get() : null;
	}

	private void initialize() {
		namePage = new ImageNameDescriptionPage();
		scriptingPage = new ScriptingPage();
		getPages().addAll(namePage, scriptingPage);
	}

}
