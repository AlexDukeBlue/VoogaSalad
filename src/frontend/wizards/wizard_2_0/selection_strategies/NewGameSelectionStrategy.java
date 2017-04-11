package frontend.wizards.wizard_2_0.selection_strategies;

import backend.cell.Cell;
import backend.cell.ModifiableCell;
import backend.grid.BoundsHandler;
import backend.grid.ModifiableGameBoard;
import backend.util.GameState;
import frontend.wizards.wizard_2_0.wizard_pages.GridInstantiationPage;
import frontend.wizards.wizard_2_0.wizard_pages.ImageNameDescriptionPage;

public class NewGameSelectionStrategy extends BaseSelectionStrategy<GameState> {

	private ImageNameDescriptionPage imageNameDescriptionPage;
	private GridInstantiationPage gridInstantiationPage;
	
	public NewGameSelectionStrategy(){
		initialize();
	}
	
	@Override
	public GameState finish() {
//		ModifiableCell cell = new ModifiableCell("asdf");
//		Cell template = ModifiableCell.BASIC_SQUARE_FLAT;
//		ModifiableGameBoard board = new ModifiableGameBoard("testBoard", template, 5, 5, BoundsHandler.TOROIDAL_BOUNDS, "", "").copy();
//		GameState gameState = new GameState(board);
		return new GameState();
	}
	
	private void initialize(){
		imageNameDescriptionPage = new ImageNameDescriptionPage();
		gridInstantiationPage = new GridInstantiationPage();
		getPages().addAll(imageNameDescriptionPage);//,gridInstantiationPage);
	}

}
