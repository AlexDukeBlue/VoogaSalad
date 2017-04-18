/**
 * SpriteListItems are visual items that display a Sprite that is available to build with in the 
 * development environment.
 */
package frontend.templatepane;

import frontend.worldview.grid.Sprite;

/**
 * @author Stone Mathers
 * Created 3/29/2017
 *
 */
public interface SpriteListItem extends ListItem {
	
	/**
	 * @return Sprite contained within the SpriteListItem.
	 */
	Sprite getSprite();
	
	/**
	 * Sets the Sprite to be held.
	 * 
	 * @param Sprite to be held.
	 */
	void setSprite(Sprite sprite);
	
	/**
	 * @return String representing the name of the Sprite.
	 */
	String getName();
	
	/**
	 * @param String that holds the name of the Sprite.
	 */
	void setName(String name);
	
}
