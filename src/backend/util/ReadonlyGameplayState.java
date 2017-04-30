package backend.util;

import backend.game_engine.Resultant;
import backend.grid.GameBoard;
import backend.player.ImmutablePlayer;
import backend.player.Team;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Created by th174 on 4/11/2017.
 */
public interface ReadonlyGameplayState extends VoogaEntity {
	Team getActiveTeam();

	ImmutablePlayer getPlayerByName(String name);

	List<String> getOrderedPlayerNames();

	Team getTeamByName(String teamName);

	Collection<Team> getTeams();

	int getTurnNumber();

	GameBoard getGrid();

	double random();

	Collection<Resultant> getObjectives();

	Map<Event, Collection<Actionable>> getTurnActions();

	Collection<Requirement> getTurnRequirements();

	boolean turnRequirementsSatisfied();

	@Override
	ReadonlyGameplayState copy();

	boolean isAuthoringMode();
	
	void setAuthoringMode(boolean isAuthoringMode);
}
