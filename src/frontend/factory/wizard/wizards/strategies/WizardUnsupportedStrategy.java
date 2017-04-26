package frontend.factory.wizard.wizards.strategies;

import frontend.factory.wizard.wizards.strategies.wizard_pages.WizardUnsupportedPage;

public class WizardUnsupportedStrategy extends BaseStrategy<Object> {
	
	private WizardUnsupportedPage page;
	
	public WizardUnsupportedStrategy(){
		page = new WizardUnsupportedPage();
		getPages().add(page);
	}

	@Override
	public Object finish() {
		return null;
	}

}
