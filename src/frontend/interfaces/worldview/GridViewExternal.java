package frontend.interfaces.worldview;

import backend.grid.CoordinateTuple;
import frontend.ClickHandler;
import javafx.scene.control.ScrollPane;
import polyglot.Polyglot;

import java.util.Collection;

public interface GridViewExternal {
	ScrollPane getObject();

	void setClickHandler(ClickHandler clickHandler);

	Polyglot getPolyglot();

	void highlightRange(Collection<CoordinateTuple> highlightedCells);

	void resetHighlighting();
}
