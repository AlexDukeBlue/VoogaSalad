package frontend.factory.templatepane;

import controller.Controller;
import frontend.ClickHandler;
import frontend.interfaces.templatepane.TemplatePaneExternal;

public class TemplatePaneFactory {
	public static final String[] TEMPLATE_CATEGORIES = new String[] { "Units", "Terrains", "ActiveAbilities",
			"UnitTriggeredEffects", "CellTriggeredEffects", "OffensiveModifiers", "DefensiveModifiers",
			"BoundsHandlers" };

	public static TemplatePaneExternal newTemplatePane(Controller controller, ClickHandler clickHandler) {
		return new TemplatePane(controller, clickHandler, TEMPLATE_CATEGORIES);
	}

}
