package frontend.templatepane.interfaces;

import backend.util.VoogaEntity;

public interface TemplatePaneObserver {

	void didClickVoogaEntity(TemplatePaneExternal templatePane, VoogaEntity entity);

}
