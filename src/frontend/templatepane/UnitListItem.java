/**
 * UnitListItems are visual items that display a Unit that is available to build with in the 
 * development environment.
 */
package frontend.templatepane;

import frontend.sprites.Sprite;
import javafx.scene.Node;

/**
 * @author Stone Mathers
 * Created 3/30/2017
 */
public class UnitListItem implements SpriteListItem {

	Sprite mySprite;
	
	public UnitListItem(Sprite sprite){
		mySprite = sprite;
	}
	
	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public Node getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sprite getSprite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSprite(Sprite sprite) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

}
