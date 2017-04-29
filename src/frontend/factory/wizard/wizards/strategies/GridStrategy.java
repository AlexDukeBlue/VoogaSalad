package frontend.factory.wizard.wizards.strategies;

import backend.grid.BoundsHandler;
import backend.grid.ModifiableGameBoard;
import frontend.factory.wizard.wizards.strategies.wizard_pages.GridInstantiationPage;
import frontend.factory.wizard.wizards.strategies.wizard_pages.ImageNameDescriptionPage;
import javafx.beans.binding.StringBinding;

public class GridStrategy extends BaseStrategy<ModifiableGameBoard> {

	private ImageNameDescriptionPage boardNamePage;
	private GridInstantiationPage gridInstantiationPage;

	public GridStrategy() {
		initialize();
	}

	@Override
	public ModifiableGameBoard finish() {
		return (ModifiableGameBoard) new ModifiableGameBoard("").setName(boardNamePage.getName())
				.setDescription(boardNamePage.getDescription().getValueSafe()).setImgPath(boardNamePage.getImagePath())
				.setRows(gridInstantiationPage.getRows()).setColumns(gridInstantiationPage.getCols())
				.setTemplateCell(gridInstantiationPage.getTemplateCell())
				.setBoundsHandler(BoundsHandler.INFINITE_BOUNDS).build();
	}

	private void initialize() {
		boardNamePage = new ImageNameDescriptionPage("GridStrategyNameDescription");
		gridInstantiationPage = new GridInstantiationPage("GridStrategyInstantiationDescription");
		getPages().addAll(boardNamePage, gridInstantiationPage);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("GridStrategyDescription");
	}

}
