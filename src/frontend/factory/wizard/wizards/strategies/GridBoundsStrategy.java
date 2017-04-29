package frontend.factory.wizard.wizards.strategies;

import backend.grid.BoundsHandler;
import backend.grid.BoundsHandler.CoordinateMapper;
import javafx.beans.binding.StringBinding;

public class GridBoundsStrategy extends NameScriptBaseStrategy<BoundsHandler> {

	public GridBoundsStrategy() {
		super("GridBoundsNamePageDescription", "GridBoundsScriptingPageDescription");
		setScriptPrompt("GridBounds_Example_");
	}

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
