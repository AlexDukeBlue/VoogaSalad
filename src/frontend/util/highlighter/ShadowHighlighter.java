/**
 * 
 */
package frontend.util.highlighter;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * @author Stone Mathers
 * Created 4/22/2017
 */
public class ShadowHighlighter implements Highlighter {

	private static final Color DEFAULT_COLOR = Color.STEELBLUE;
	
	private Color myShadowColor;
	
	public ShadowHighlighter(){
		this(DEFAULT_COLOR);
	}
	
	public ShadowHighlighter(Color shadowColor){
		myShadowColor = shadowColor;
	}
	
	@Override
	public void highlight(Node node) {
		DropShadow ds = new DropShadow();
		ds.setColor(myShadowColor);		
		node.setEffect(ds);	
	}

	@Override
	public void removeHighlight(Node node) {
		node.setEffect(null);
	}

}
