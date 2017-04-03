package backend.game_engine;

import backend.cell.Terrain;
import backend.player.Player;
import backend.unit.UnitInstance;
import backend.util.Event;
import backend.util.GameState;
import backend.util.ImmutableGameState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class DieselEngine implements GameEngine {

    private List<ResultQuadPredicate> currentObjectives;
    private Map<Event, List<BiConsumer<Player, ImmutableGameState>>> turnActions;

    public DieselEngine() {
    }

    @Override
    public String toXML() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addObjective(ResultQuadPredicate winCondition) {
        currentObjectives.add(winCondition);
    }

    @Override
    public void addTrigger(BiConsumer<Player, ImmutableGameState> turnAction, Event when) {
        turnActions.merge(when, new ArrayList<>(Collections.singletonList(turnAction)), (list, t) -> {
            list.addAll(t);
            return list;
        });
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub

    }

    @Override
    public void restart() {
        // TODO Auto-generated method stub

    }

    @Override
    public void quit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void save() {
        // TODO Auto-generated method stub

    }

    @Override
    public void load() {
        // TODO Auto-generated method stub

    }

    @Override
    public void newUnit(UnitInstance newUnit) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newTerrain(Terrain terrain) {
        //TODO Auto-generated method stub
    }
}