package frontend.interfaces.worldview;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import frontend.util.GameBoardObjectView;
import javafx.scene.layout.Pane;

public interface UnitViewExternal extends GameBoardObjectView {

	Pane getObject();

	String getUnitName();

	@Override
	Unit getEntity();

	CoordinateTuple getUnitLocation();
}
