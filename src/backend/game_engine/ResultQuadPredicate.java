package backend.game_engine;

import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.util.GameplayState;

import java.io.Serializable;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface ResultQuadPredicate extends Serializable {
	Result determine(ImmutablePlayer player, GameplayState state);

	enum Result {
		//        WIN((player, gameState) -> state.handleWin(player)),
//        LOSE((player, gameState) -> state.handleLoss(player)),
//        TIE((player, gameState) -> state.handleTie()),
		NONE((player, gameState) -> doNothing());

		private BiConsumer<ImmutablePlayer, GameplayState> toExecute;

		Result(BiConsumer<ImmutablePlayer, GameplayState> executeThis) {
			toExecute = executeThis;
		}

		public void accept(ImmutablePlayer player, GameplayState state) {
			toExecute.accept(player, state);
		}

		private static void doNothing() {
		}
	}
}
