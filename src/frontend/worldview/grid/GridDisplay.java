/**
 * Holds a grid to be displayed in the development and player GUI. It can have Sprites added to a particular cell or 
 * have all cells updated after something occurs in the game.
 */
package frontend.worldview.grid;

import frontend.sprites.Sprite;
import frontend.util.BaseUIManager;
import javafx.scene.layout.Region;

/**
 * @author Stone Mathers
 * Created 3/29/2017
 */
public abstract class GridDisplay extends BaseUIManager<Region> {

	
	/**
	 * Place a Sprite in the cell that contains the given coordinates.
	 * 
	 * @param Sprite to be placed in the cell.
	 * @param Coordinates that the mouse is located at.
	 */
	public abstract void placeInCell(Sprite sprite, Coordinates mouseCoord);
	
	/**
	 * Updates all cells in the grid using the given GridDisplay.
	 */
	public abstract void updateCells(GridDisplay grid);
}
