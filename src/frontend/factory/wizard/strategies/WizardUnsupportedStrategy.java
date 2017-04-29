package frontend.factory.wizard.strategies;

import frontend.factory.wizard.strategies.wizard_pages.WizardUnsupportedPage;
import javafx.beans.binding.StringBinding;

/**
 * The WizardUnsupportedStrategy class can be displayed to the user when the
 * wizard the user tried to create can not be created. The
 * WizardUnsupportedStrategy does nothing other than alert the user that the
 * wizard is not supported and allow the user to click cancel.
 * 
 * @author Dylan Peters
 *
 */
class WizardUnsupportedStrategy extends BaseStrategy<Object> {

	private WizardUnsupportedPage page;

	/**
	 * Creates a new instance of WizardUnsupportedStrategy.
	 */
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
