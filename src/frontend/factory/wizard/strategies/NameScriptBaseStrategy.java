package frontend.factory.wizard.strategies;

import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.strategies.wizard_pages.ScriptingPage;
import util.scripting.VoogaScriptEngine;

/**
 * NameScriptBaseStrategy is an abstract class that starts out a Wizard with a
 * name page and a scripting page. This helps prevent duplicated code between
 * wizards that have similar interfaces, by allowing them to extend this class
 * and add any further pages.
 * 
 * @author Dylan Peters
 *
 * @param <T>
 */
abstract class NameScriptBaseStrategy<T> extends BaseStrategy<T> {

	private ImageNameDescriptionPage namePage;
	private ScriptingPage scriptingPage;

	/**
	 * Creates a new NameScriptBaseStrategy object.
	 * 
	 * @param namePageDescriptionKey
	 *            a string to be used as the key when getting the description of
	 *            the name page from a ResouceBundle.
	 * @param scriptingPageDescriptionKey
	 *            a string to be used as the key when getting the description of
	 *            the name page from a ResouceBundle.
	 */
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
