package frontend.factory.wizard.wizards.strategies;

import backend.grid.BoundsHandler;
import backend.grid.BoundsHandler.CoordinateMapper;

public class GridBoundsStrategy extends NameScriptBaseStrategy<BoundsHandler>{

	@Override
	public BoundsHandler finish() {
		return new BoundsHandler(getName(), (CoordinateMapper) getScriptEngine(), getDescription(),getImgPath());
	}

}
