/**
 * Visual unit that contains all CollapsibleLists and ListItems representing the Sprites available to build with.
 */
package frontend.templatepane;

import java.util.HashMap;
import java.util.Map;

import frontend.util.ObjectManager;
import frontend.worldview.grid.Sprite;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 * @author Stone Mathers
 * Created 3/30/17
 */
public class SpriteMenu implements ObjectManager<Node> {

	private Group myGroup = new Group();
	private Map<String, CollapsibleList> myLists;
	
	public SpriteMenu(){
		myLists = new HashMap<String, CollapsibleList>();
	}
	
	/**
	 
	 */
	/**
	 * Takes in a Sprite and returns the CollapsibleList that it belongs to. A RuntimeException
	 * is thrown if no such CollapsibleList exists.
	 * 
	 * @param Sprite that is to be placed in an existing CollapsibleList.
	 * @return String representing CollapsibleList that the given Sprite belongs to.
	 * @throws RuntimeException if no such list is found.
	 */
	public CollapsibleList getListForSprite(Sprite sprite) throws RuntimeException{
		return myLists.get(sprite.getListType());
	}
	
	@Override
	public Node getObject() {
		return myGroup;
	}

}
