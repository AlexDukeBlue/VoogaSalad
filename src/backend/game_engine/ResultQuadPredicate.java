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
		WIN((player, engine) -> engine.handleWin(player)),
        LOSE((player, engine) -> engine.handleLoss(player)),
        TIE((player, engine) -> engine.handleTie()),
		NONE((player, engine) -> doNothing());

		private BiConsumer<ImmutablePlayer, GameEngine> toExecute;

		Result(BiConsumer<ImmutablePlayer, GameEngine> executeThis) {
			toExecute = executeThis;
		}

		public void accept(ImmutablePlayer player, GameEngine engine) {
			toExecute.accept(player, engine);
		}

		private static void doNothing() {
		}
	}
}
