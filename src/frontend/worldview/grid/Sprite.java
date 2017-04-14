/**
 * A visual representation of anything that can be placed in the game, such as a playable piece, an immovable object,
 * or terrain. Can also be acted upon with mouse click and drags. A Sprite is composed of a List of SpriteCells so
 * that it can take up multiple Cells on the grid, but still is represented with one image.
 */
package frontend.worldview.grid;

import java.util.List;

import frontend.util.BaseUIManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * @author Stone Mathers
 * Created 3/29/2017
 */
public abstract class Sprite extends BaseUIManager<Region> {

	/**
	 * Determines what is done when a Sprite is clicked.
	 */
	void setOnClick(EventHandler<ActionEvent> event) {
	}
	
	/**
	 * Determines what is done when a Sprite is clicked and dragged.
	 */
	public void setOnDrag(Sprite sprite) {
		ImageView spriteImage = new ImageView(getImage(sprite));
		 spriteImage.setOnDragDetected(new EventHandler <MouseEvent>() {
	            @Override
				public void handle(MouseEvent event) {
	                /* drag was detected, run drag-and-drop gesture*/
	                
	                /* create dragboard */
	                Dragboard db = (Dragboard) Dragboard.getSystemClipboard();
	                
	                /* put an image on dragboard */
	                ClipboardContent content = new ClipboardContent();
	                content.putImage(getImage(sprite));
	                db.setContent(content);
	                event.consume();
	            }
	        });
	}
	
	/**
	 * @return List of SpriteCells composing the Sprite.
	 */
	List<CellView> getSpriteCells() {
		return null;
	}
	
	/**
	 * Adds cell to the Sprite's list of SpriteCells.
	 * 
	 * @param CellView to be added.
	 */
	void addSpriteCell(CellView cell) {
	}
	
	/**
	 * @param index in the list to search for a SpriteCell.
	 * @return SpriteCell at the given index.
	 */
	CellView getSpriteCell(int index) {
		return null;
	}
	
	/**
	 * @return integer size of the list of SpriteCells.
	 */
	int getSize() {
		return 0;
	}
	
	/**
	 * @return Image representing the Sprite.
	 */
	Image getImage(Sprite sprite) {
		return null;
	}
	
	/**
	 * @param Image to be set to represent the Sprite.
	 */
	void setImage(Image image) {
	}
	
	/**
	 * @param String that represents the type of list the Sprite belongs to.
	 */
	void setListType(String type) {
	}
	
	/**
	 * @return String holding the type of list the Sprite belongs to.
	 */
	public abstract String getListType();
	
	/**
	 * @return Image representing the Sprite.
	 */
	String getName() {
		return null;
	}
	
	/**
	 * @param Image to be set to represent the Sprite.
	 */
	void setName(String text) {
	}
}

