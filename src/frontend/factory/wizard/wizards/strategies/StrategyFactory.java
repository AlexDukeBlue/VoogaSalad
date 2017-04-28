package frontend.factory.wizard.wizards.strategies;

import backend.util.AuthoringGameState;

import java.util.HashMap;
import java.util.Map;

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

	public static WizardStrategy<?> newStrategy(String categoryName, AuthoringGameState gameState) {
		categoryName = categoryName.replaceAll(" ", "").toLowerCase().replaceAll("ies$", "y").replaceAll("s$", "");
		try {
			return STRATEGY_MAP.get(categoryName).getConstructor(gameState.getClass()).newInstance(gameState);
		} catch (Exception e) {
			try {
				return STRATEGY_MAP.get(categoryName).getConstructor().newInstance();
			} catch (Exception e1) {
				e1.printStackTrace();
				return new WizardUnsupportedStrategy();
			}
		}
	}

}
