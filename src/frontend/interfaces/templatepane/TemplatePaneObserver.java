package frontend.interfaces.templatepane;

import backend.util.VoogaEntity;

public interface TemplatePaneObserver {

	void didClickVoogaEntity(TemplatePaneExternal templatePane, VoogaEntity entity);

}
