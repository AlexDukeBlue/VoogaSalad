package frontend.factory.templatepane;

import controller.Controller;
import frontend.factory.worldview.MinimapPane;
import frontend.interfaces.templatepane.TemplatePaneExternal;

public class TemplatePaneFactory {
	
	public static TemplatePaneExternal newTemplatePane(Controller controller,MinimapPane mapPane){
		return new TemplatePane(controller, mapPane);
	}

}
