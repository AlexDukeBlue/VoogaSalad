package backend.player;

import backend.util.VoogaInstance;
import backend.util.GameState;
import backend.unit.UnitInstance;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Alex
 *
 * @author Created by th174 on 3/28/2017.
 */
public class Player extends VoogaInstance {
    private Team team;

    public Player(String name, String description, String imgPath, GameState currentGame) {
        this(name, new Team(name + "'s Team", description, imgPath), description, imgPath, currentGame);
    }

    public Player(String name, Team team, String description, String imgPath, GameState currentGame) {
        super(name, description, imgPath, currentGame);
        team.add(this);
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public Collection<UnitInstance> getOwnedUnits() {
        return getGameState().getGrid().getUnits().parallelStream().filter(e -> e.getOwner().equals(this)).collect(Collectors.toSet());
    }
}
