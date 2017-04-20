package frontend.factory.templatepane;

import controller.Controller;
import frontend.interfaces.templatepane.TemplatePaneExternal;

public class TemplatePaneFactory {
	
	public static TemplatePaneExternal newTemplatePane(Controller controller){
		return new TemplatePane(controller);
	}

}
