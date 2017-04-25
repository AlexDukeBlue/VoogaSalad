package frontend.factory.wizard.wizards.strategies;

import backend.grid.BoundsHandler;
import backend.grid.BoundsHandler.CoordinateMapper;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ScriptingPage;

public class GridBoundsStrategy extends BaseStrategy<BoundsHandler>{
	
	ImageNameDescriptionPage namePage;
	ScriptingPage scriptingPage;
	
	public GridBoundsStrategy() {
		initialize();
	}

	@Override
	public BoundsHandler finish() {
		return new BoundsHandler(namePage.getName(), (CoordinateMapper) scriptingPage.getScriptEngine().get(), namePage.getDescription());
	}
	
	private void initialize(){
		namePage = new ImageNameDescriptionPage();
		scriptingPage = new ScriptingPage();
		getPages().addAll(namePage,scriptingPage);
	}

}
