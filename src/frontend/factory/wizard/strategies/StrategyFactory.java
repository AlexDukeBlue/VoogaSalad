package frontend.factory.wizard.strategies;

import java.util.HashMap;
import java.util.Map;

import controller.Controller;

/**
 * StrategyFactory allows client code to create new WizardStrategies simply by
 * passing a string specifying the type of wizard it wants. This hides the
 * implementation of what each strategy needs and creates a uniform method of
 * instantiation.
 * 
 * @author Dylan Peters
 *
 */
public class StrategyFactory {
	private static final Map<String, Class<? extends WizardStrategy<?>>> STRATEGY_MAP = new HashMap<>();

	static {
		STRATEGY_MAP.put("gamestate", GameStrategy.class);
		STRATEGY_MAP.put("grid", GridStrategy.class);
		STRATEGY_MAP.put("team", TeamStrategy.class);
		STRATEGY_MAP.put("terrain", TerrainStrategy.class);
		STRATEGY_MAP.put("unit", UnitStrategy.class);
		STRATEGY_MAP.put("activeability", ActiveAbilityStrategy.class);
		STRATEGY_MAP.put("unittriggeredeffect", TriggeredEffectStrategy.class);
		STRATEGY_MAP.put("celltriggeredeffect", TriggeredEffectStrategy.class);
		STRATEGY_MAP.put("offensivemodifier", InteractionModifierStrategy.class);
		STRATEGY_MAP.put("defensivemodifier", InteractionModifierStrategy.class);
		STRATEGY_MAP.put("turnrequirement", TurnRequirementStrategy.class);
		STRATEGY_MAP.put("turnaction", TurnActionStrategy.class);
		STRATEGY_MAP.put("endcondition", EndConditionStrategy.class);
		STRATEGY_MAP.put("boundshandler", GridBoundsStrategy.class);
	}

	/**
	 * Creates a new WizardStrategy of the type specified by categoryName. If
	 * the wizard type specified is not supported, it returns a
	 * WizardNotSupportedStrategy that simply allows the user to exit.
	 * 
	 * @param categoryName
	 *            the name of the wizard type that the user would like to
	 *            create.
	 * @param gameState
	 *            a GameState that the wizard will use to populate certain
	 *            fields
	 * @return a WizardStrategy that has all the components necessary to get
	 *         information from the user about the object to be created.
	 */
	public static WizardStrategy<?> newStrategy(String categoryName, Controller controller) {
		categoryName = categoryName.replaceAll(" ", "").toLowerCase().replaceAll("ies$", "y").replaceAll("s$", "");
		try {
			return STRATEGY_MAP.get(categoryName).getConstructor(Controller.class).newInstance(controller);
		} catch (Exception e) {
			try {
				return STRATEGY_MAP.get(categoryName).getConstructor().newInstance();
			} catch (Exception e1) {
				e.printStackTrace();
				e1.printStackTrace();
				return new WizardUnsupportedStrategy(controller);
			}
		}
	}

}
