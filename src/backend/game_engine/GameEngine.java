package backend.game_engine;

import backend.cell.Terrain;
import backend.io.XMLsavable;
import backend.player.Player;
import backend.unit.UnitInstance;

import java.util.function.BiConsumer;

/**
 *
 * @author Alexander Zapata
 * 
 */
public interface GameEngine extends XMLsavable {
    void addObjective(ResultQuadPredicate winCondition);

    void addTrigger(BiConsumer<Player, GameState> turnAction, TurnTrigger when);

    void start();

    void restart();

    void quit();

    void save();

    void load();

    void newUnit(UnitInstance newUnit);

    void newTerrain(Terrain terrain);

    public enum TurnTrigger {
		BEGINNING,
		END
	}
}
