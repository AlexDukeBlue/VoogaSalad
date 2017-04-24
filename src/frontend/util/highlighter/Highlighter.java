/**
 * An interface that visually highlights a Node to differentiate it from others. Most commonly, this is
 * used to visually indicate when the user selects a node. 
 */
package frontend.util.highlighter;

import javafx.scene.Node;

/**
 * @author Stone Mathers
 * Created 4/22/2017
 */
public interface Highlighter {

	
	/**
	 * Takes in a Node and visually distinguishes it (usually as being selected amongst other similar Nodes).
	 * 
	 * @param node Node to be highlighted
	 */
	void highlight(Node node);
	
	/**
	 * Removes the effect applied by highlight to restore the Node to its initial state.
	 * 
	 * @param node Node from which to remove highlight effect.
	 */
	void removeHighlight(Node node);
}
