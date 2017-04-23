package frontend.factory.templatepane;

import controller.Controller;
import frontend.ClickHandler;
import frontend.interfaces.templatepane.TemplatePaneExternal;

public class TemplatePaneFactory {
	
	public static TemplatePaneExternal newTemplatePane(Controller controller, ClickHandler clickHandler){
		return new TemplatePane(controller, clickHandler);
	}

}
