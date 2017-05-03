package frontend.factory.wizard.strategies;

import backend.grid.BoundsHandler;
import backend.grid.ModifiableGameBoard;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.GridInstantiationPage;
import frontend.factory.wizard.strategies.wizard_pages.ImageNameDescriptionPage;
import javafx.beans.binding.StringBinding;

/**
 * GridStrategy is WizardStrategy that allows the user to create new Grid
 * objects.
 * 
 * @author Dylan Peters
 *
 */
class GridStrategy extends BaseStrategy<ModifiableGameBoard> {

	private ImageNameDescriptionPage boardNamePage;
	private GridInstantiationPage gridInstantiationPage;

	/**
	 * Creates a new instance of GridStrategy
	 */
	public GridStrategy(Controller controller) {
		super(controller);
		initialize();
	}

	/**
	 * Returns a fully instantiated ModifiableGameboard
	 */
	@Override
	public ModifiableGameBoard finish() {
		return (ModifiableGameBoard) new ModifiableGameBoard("").setName(boardNamePage.getName())
				.setDescription(boardNamePage.getDescriptionBoxText())
				.setImgPath(boardNamePage.getImagePath()).setRows(gridInstantiationPage.getRows())
				.setColumns(gridInstantiationPage.getCols()).setTemplateCell(gridInstantiationPage.getTemplateCell())
				.setBoundsHandler(BoundsHandler.INFINITE_BOUNDS).build();
	}

	private void initialize() {
		boardNamePage = new ImageNameDescriptionPage(getController(), "GridStrategyNameDescription");
		gridInstantiationPage = new GridInstantiationPage(getController(), "GridStrategyInstantiationDescription");
		getPages().addAll(boardNamePage, gridInstantiationPage);
	}

	@Override
	public StringBinding getTitle() {
		return getPolyglot().get("GridStrategyTitle");
	}

}
