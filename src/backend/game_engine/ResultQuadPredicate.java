package backend.game_engine;

import backend.player.ImmutablePlayer;
import backend.player.Player;
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
	Result determine(ImmutablePlayer player, GameplayState state);

	enum Result {
		WIN((player, engine) -> engine.handleWin(player)),
        LOSE((player, engine) -> engine.handleLoss(player)),
        TIE((player, engine) -> engine.handleTie()),
		NONE((player, engine) -> doNothing());

		private BiConsumer<ImmutablePlayer, GameEngine> toExecute;

		Result(BiConsumer<ImmutablePlayer, GameEngine> executeThis) {
			toExecute = executeThis;
		}

		/**
		 * The lambda expression given to each Result value is accepted. The lambda defers action to the state.
		 * 
		 * @param player
		 * @param engine
		 */
		public void accept(ImmutablePlayer player, GameEngine engine) {
			toExecute.accept(player, engine);
		}

		/**
		 * Does literally nothing for the NONE option.
		 */
		private static void doNothing() {
		}
	}
}
