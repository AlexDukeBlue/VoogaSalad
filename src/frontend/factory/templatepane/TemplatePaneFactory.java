package frontend.factory.templatepane;

import controller.Controller;
import frontend.ComponentClickHandler;
import frontend.interfaces.templatepane.TemplatePaneExternal;

public class TemplatePaneFactory {
	
	public static TemplatePaneExternal newTemplatePane(Controller controller, ComponentClickHandler clickHandler){
		return new TemplatePane(controller, clickHandler);
	}

}
