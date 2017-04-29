package frontend.factory.wizard.wizards.strategies;

import frontend.factory.wizard.wizards.strategies.wizard_pages.WizardUnsupportedPage;
import javafx.beans.binding.StringBinding;

public class WizardUnsupportedStrategy extends BaseStrategy<Object> {

	private WizardUnsupportedPage page;

	public WizardUnsupportedStrategy() {
		page = new WizardUnsupportedPage();
		getPages().add(page);
	}

	@Override
	public Object finish() {
		return null;
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("WizardUnsupportedTitle");
	}

}
