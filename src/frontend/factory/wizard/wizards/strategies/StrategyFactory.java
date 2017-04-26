package frontend.factory.wizard.wizards.strategies;

import backend.util.AuthoringGameState;

import java.util.HashMap;
import java.util.Map;

public class StrategyFactory {
	private static final Map<String, Class<? extends WizardStrategy<?>>> strategyMap = new HashMap<>();

	static {
		strategyMap.put("gamestate", GameStrategy.class);
		strategyMap.put("grid", GridStrategy.class);
		strategyMap.put("team", TeamStrategy.class);
		strategyMap.put("terrain", TerrainStrategy.class);
		strategyMap.put("unit", UnitStrategy.class);
		strategyMap.put("activeability", ActiveAbilityStrategy.class);
		strategyMap.put("unittriggeredeffect", TriggeredEffectStrategy.class);
		strategyMap.put("celltriggeredeffect", TriggeredEffectStrategy.class);
		strategyMap.put("offensivemodifier", InteractionModifierStrategy.class);
		strategyMap.put("defensivemodifier", InteractionModifierStrategy.class);
		strategyMap.put("turnrequirement", TurnRequirementStrategy.class);
		strategyMap.put("turnaction", TurnActionStrategy.class);
		strategyMap.put("endcondition", EndConditionStrategy.class);
		strategyMap.put("boundshandler", GridBoundsStrategy.class);
	}

	public static WizardStrategy<?> newStrategy(String categoryName, AuthoringGameState gameState) {
		categoryName = categoryName.replaceAll(" ", "").toLowerCase().replaceAll("ies$", "y").replaceAll("s$", "");
		try {
			return strategyMap.get(categoryName).getConstructor(gameState.getClass()).newInstance(gameState);
		} catch (Exception e) {
			try {
				return strategyMap.get(categoryName).getConstructor().newInstance();
			} catch (Exception e1) {
				return new WizardUnsupportedStrategy();
			}
		}
	}

}
