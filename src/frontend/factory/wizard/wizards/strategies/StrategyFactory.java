package frontend.factory.wizard.wizards.strategies;

import java.util.HashMap;
import java.util.Map;

import backend.cell.Terrain;
import backend.player.Team;
import backend.unit.Unit;
import backend.unit.properties.ActiveAbility;
import backend.util.AuthoringGameState;

public class StrategyFactory {
	private static final Map<Class<? extends Object>, Class<? extends WizardStrategy<? extends Object>>> strategyMap;
	static {
		Map<Class<? extends Object>, Class<? extends WizardStrategy<? extends Object>>> placeHolder = new HashMap<>();
		placeHolder.put(ActiveAbility.class, ActiveAbilityStrategy.class);
		placeHolder.put(AuthoringGameState.class, GameStrategy.class);
		placeHolder.put(Team.class, TeamStrategy.class);
		placeHolder.put(Terrain.class, TerrainStrategy.class);
		placeHolder.put(Unit.class, UnitStrategy.class);
		strategyMap = placeHolder;
	}

	public static WizardStrategy<? extends Object> newStrategy(Class<? extends Object> clazz,
			AuthoringGameState gameState) {
		try {
			return strategyMap.get(clazz).getConstructor(gameState.getClass()).newInstance(gameState);
		} catch (Exception e) {
			try {
				System.out.println(strategyMap);
				System.out.println(strategyMap.get(clazz));
				System.out.println(strategyMap.get(clazz).getConstructor());
				System.out.println(strategyMap.get(clazz).getConstructor().newInstance());
				return strategyMap.get(clazz).getConstructor().newInstance();
			} catch (Exception e1) {
				System.out.println("Returning null");
				return null;
			}
		}
	}

}
