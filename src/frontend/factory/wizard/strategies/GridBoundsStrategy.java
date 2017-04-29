package frontend.factory.wizard.strategies;

import backend.grid.BoundsHandler;
import backend.grid.BoundsHandler.CoordinateMapper;
import javafx.beans.binding.StringBinding;

/**
 * GridBoundsStrategy is a WizardStrategy that allows the user to generate new
 * GridBounds.
 * 
 * @author Dylan Peters
 *
 */
class GridBoundsStrategy extends NameScriptBaseStrategy<BoundsHandler> {

	/**
	 * Creates a new instance of GridBoundsStrategy
	 */
	public GridBoundsStrategy() {
		super("GridBoundsNamePageDescription", "GridBoundsScriptingPageDescription");
		setScriptPrompt("GridBounds_Example_");
	}

	/**
	 * Returns a fully instantiated instance of BoundsHandler
	 */
	@Override
	public BoundsHandler finish() {
		return new BoundsHandler(getName(), (CoordinateMapper) getScriptEngine(), getDescriptionBoxText(),
				getImgPath());
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("GridBoundsWizardTitle");
	}

}
