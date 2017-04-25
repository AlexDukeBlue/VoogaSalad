package frontend.interfaces.worldview;

import backend.grid.CoordinateTuple;
import backend.grid.ModifiableGameBoard;
import frontend.ClickHandler;
import frontend.util.GameBoardObjectView;
import javafx.scene.control.ScrollPane;
import polyglot.Polyglot;

import java.util.Collection;

public interface GridViewExternal extends GameBoardObjectView {
	ScrollPane getObject();

	void setClickHandler(ClickHandler clickHandler);

	Polyglot getPolyglot();

	void highlightRange(Collection<CoordinateTuple> highlightedCells);

	void resetHighlighting();

	@Override
	ModifiableGameBoard getEntity();
}
