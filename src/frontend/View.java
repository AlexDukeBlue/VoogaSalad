/**
 * 
 */
package frontend;

import javafx.scene.Node;
import backend.util.ImmutableGameState;

/**
 * @author Stone Mathers, Dylan Peters
 * Created 4/3/2017
 */
public class View extends BaseUIManager {

	private VoogaMenuBar menuBar;
	private WorldView worldView;
	private ToolsPane toolsPane;
	private DetailPane infoPane;
	private TemplatePane tempPane;
	
	
	public View(ImmutableGameState gameState, ClientController controller){
		
	}
	
	
	
	/**
	 * Notifies when GameState changes and displays the new GameState.
	 */
	public void update(){
		
	}
	
	/**
	 * Sends requests to ClientController when the user makes any changes.
	 */
	private void instantiateListeners(){
		
	}

	@Override
	public Object getObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
