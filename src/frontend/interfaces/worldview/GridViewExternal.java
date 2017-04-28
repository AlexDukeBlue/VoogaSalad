package frontend.interfaces.worldview;

import backend.grid.CoordinateTuple;
import backend.grid.GameBoard;
import frontend.ClickHandler;
import frontend.util.GameBoardObjectView;
import javafx.scene.control.ScrollPane;
import util.polyglot.Polyglot;

import java.util.Collection;

public interface GridViewExternal extends GameBoardObjectView {
	ScrollPane getNode();

	void setClickHandler(ClickHandler clickHandler);

	Polyglot getPolyglot();

	void highlightRange(Collection<CoordinateTuple> highlightedCells);

	void resetHighlighting();

	@Override
	GameBoard getEntity();
}
