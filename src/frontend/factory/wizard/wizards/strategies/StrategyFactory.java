package frontend.factory.wizard.wizards.strategies;

import backend.util.AuthoringGameState;

import java.util.HashMap;
import java.util.Map;

public class StrategyFactory {
	private static final Map<String, Class<? extends WizardStrategy<?>>> strategyMap = new HashMap<>();

	static {
		strategyMap.put("activeability", ActiveAbilityStrategy.class);
		strategyMap.put("gamestate", GameStrategy.class);
		strategyMap.put("team", TeamStrategy.class);
		strategyMap.put("terrain", TerrainStrategy.class);
		strategyMap.put("unit", UnitStrategy.class);
	}

	public static WizardStrategy<?> newStrategy(String categoryName, AuthoringGameState gameState) {
		categoryName = categoryName.replaceAll(" ", "").toLowerCase().replaceAll("ies$", "y").replaceAll("s$", "");
		try {
			return strategyMap.get(categoryName).getConstructor(gameState.getClass()).newInstance(gameState);
		} catch (Exception e) {
			try {
				System.out.println(strategyMap);
				System.out.println(strategyMap.get(categoryName));
				System.out.println(strategyMap.get(categoryName).getConstructor());
				System.out.println(strategyMap.get(categoryName).getConstructor().newInstance());
				return strategyMap.get(categoryName).getConstructor().newInstance();
			} catch (Exception e1) {
				throw new Error("Wizard not supported: " + categoryName, e1);
			}
		}
	}

}
