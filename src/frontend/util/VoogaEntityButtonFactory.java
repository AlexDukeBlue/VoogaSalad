package frontend.util;

import backend.unit.properties.InteractionModifier;
import backend.util.Ability;
import backend.util.PassiveAbility;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.factory.abilitypane.AbilityButton;
import frontend.factory.templatepane.TemplateButton;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Created by th174 on 4/21/17.
 */
public class VoogaEntityButtonFactory {
	private static final Collection<Class<? extends VoogaEntity>> INACTIVE_TEMPLATES = Arrays.asList(PassiveAbility.class, InteractionModifier.class);

	public static VoogaEntityButton createVoogaEntityButton(VoogaEntity entity, int size, Controller controller, ClickHandler clickHandler) {
		return new VoogaEntityButton(entity, size, controller, clickHandler);
	}

	public static VoogaEntityButton createVoogaEntityButton(VoogaEntity entity, String templateCategory, int size, Controller controller, ClickHandler clickHandler) {
		if (INACTIVE_TEMPLATES.stream().anyMatch(e -> e.isInstance(entity))) {
			return createVoogaEntityButton(entity, size, controller, clickHandler);
		}
		return new TemplateButton(entity, templateCategory, size, controller, clickHandler);
	}

	public static VoogaEntityButton createVoogaEntityButton(VoogaEntity unit, Ability entity, int size, Controller controller, ClickHandler clickHandlerr) {
		return new AbilityButton(unit, entity, size, controller, clickHandlerr);
	}
}
