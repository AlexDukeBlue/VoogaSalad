package frontend.factory.wizard.wizards.strategies;

import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ScriptingPage;
import util.scripting.VoogaScriptEngine;

public abstract class NameScriptBaseStrategy<T> extends BaseStrategy<T> {

	private ImageNameDescriptionPage namePage;
	private ScriptingPage scriptingPage;

	public NameScriptBaseStrategy(String namePageDescriptionKey, String scriptingPageDescriptionKey) {
		initialize(namePageDescriptionKey, scriptingPageDescriptionKey);
	}

	protected String getName() {
		return namePage.getName();
	}

	protected String getDescriptionBoxText() {
		return namePage.getDescriptionBoxText();
	}

	protected String getImgPath() {
		return namePage.getImagePath();
	}

	protected void setScriptPrompt(String strat) {
		scriptingPage.setPrompt(strat);
	}

	protected VoogaScriptEngine getScriptEngine() {
		return scriptingPage.getScriptEngine().isPresent() ? scriptingPage.getScriptEngine().get() : null;
	}

	public ScriptingPage getPage() {
		return scriptingPage;
	}

	private void initialize(String namePageDescriptionKey, String scriptingPageDescriptionKey) {
		namePage = new ImageNameDescriptionPage(namePageDescriptionKey);
		scriptingPage = new ScriptingPage(scriptingPageDescriptionKey);
		getPages().addAll(namePage, scriptingPage);
	}

}
