package frontend.util.highlighter;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * ShadowHihglighter is an effect tool which uses the DropShadow Effect to
 * highlight a node. It can also easily remove the effect from the Node, thus
 * making it useful for distinguishing Nodes from one another. A common use for
 * this would be visually displaying when a Node is selected.
 * 
 * @author Stone Mathers Created 4/22/2017
 */
public class ShadowHighlighter implements Highlighter {

	private static final Color DEFAULT_COLOR = Color.BLACK;
	private Color myShadowColor;

	/**
	 * Constructs a ShadowHighlighter using the DEFAULT_COLOR.
	 */
	public ShadowHighlighter() {
		this(DEFAULT_COLOR);
	}

	/**
	 * Constructs a ShadowHighlighter using the passed shadowColor.
	 * 
	 * @param shadowColor
	 *            Color used to generate the highlighting effect.
	 */
	public ShadowHighlighter(Color shadowColor) {
		myShadowColor = shadowColor;
	}

	@Override
	public void highlight(Node node) {
		DropShadow ds = new DropShadow(15, myShadowColor);
		ds.setSpread(.5);
		node.setEffect(ds);
	}

	@Override
	public void removeHighlight(Node node) {
		node.setEffect(null);
	}

}
