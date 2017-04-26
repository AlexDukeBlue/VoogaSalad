package frontend.factory.wizard.wizards.strategies;

import backend.grid.BoundsHandler;
import backend.grid.BoundsHandler.CoordinateMapper;

public class GridBoundsStrategy extends NameScriptBaseStrategy<BoundsHandler>{
	
	public GridBoundsStrategy() {
		setTitle(getPolyglot().get("GridBoundsStrategyTitle"));
		setDescription(getPolyglot().get("GridBoundsStrategyDescription"));
		setScriptPrompt("this is a prompt");
	}

	@Override
	public BoundsHandler finish() {
		return new BoundsHandler(getName(), (CoordinateMapper) getScriptEngine(), getDescription(),getImgPath());
	}

}
