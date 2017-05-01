package backend.game_engine;

import backend.player.ImmutablePlayer;
import backend.player.Team;
import backend.util.GameplayState;

import java.io.Serializable;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface ResultQuadPredicate extends Serializable {
	/**
	 * Whatever the lambda expression that determine is set to, it has to return an Enum of Result.
	 *
	 * @param player
	 * @param state
	 * @return
	 */
	Result determine(Team team, GameplayState state);

	enum Result {
		WIN((team, engine) -> engine.handleWin(team)),
		LOSE((team, engine) -> engine.handleLoss(team)),
		TIE((team, engine) -> engine.handleTie()),
		NONE((team, engine) -> doNothing());

		private BiConsumer<Team, GameEngine> toExecute;

		Result(BiConsumer<Team, GameEngine> executeThis) {
			toExecute = executeThis;
		}

		/**
		 * Does literally nothing for the NONE option.
		 */
		private static void doNothing() {
		}

		/**
		 * The lambda expression given to each Result value is accepted. The lambda defers action to the state.
		 *
		 * @param player
		 * @param engine
		 */
		public void accept(Team team, GameEngine engine) {
			toExecute.accept(team, engine);
		}
	}
}
