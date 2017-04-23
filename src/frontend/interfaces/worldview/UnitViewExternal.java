package frontend.interfaces.worldview;

import backend.grid.CoordinateTuple;
import backend.unit.Unit;
import backend.util.HasLocation;
import frontend.util.GameBoardObjectView;
import javafx.scene.layout.Pane;

public interface UnitViewExternal extends GameBoardObjectView {

	Pane getObject();

	String getUnitName();

	Unit getUnit();

	CoordinateTuple getUnitLocation();

	default HasLocation getEntity() {
		return getUnit();
	}
}
