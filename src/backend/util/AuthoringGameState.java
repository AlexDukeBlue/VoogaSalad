package backend.util;

import backend.game_engine.Resultant;
import backend.grid.GameBoard;
import backend.grid.ModifiableGameBoard;
import backend.player.ImmutablePlayer;
import backend.player.Player;
import backend.player.Team;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthoringGameState extends GameplayState implements VoogaEntity, ReadonlyGameplayState {
	private static final long serialVersionUID = 1L;

	public AuthoringGameState(String name) {
		super(name, null, "", "");
		this.setAuthoringMode(true);
	}

	@Override
	public Player getPlayerByName(String name) {
		return (Player) super.getPlayerByName(name);
	}

	@Override
	public Team getActiveTeam() {
		return super.getActiveTeam();
	}

	@Override
	public AuthoringGameState addPlayer(ImmutablePlayer newPlayer) {
		return (AuthoringGameState) super.addPlayer(newPlayer);
	}

	@Override
	public AuthoringGameState setDescription(String description) {
		return (AuthoringGameState) super.setDescription(description);
	}

	@Override
	public AuthoringGameState setImgPath(String imgPath) {
		return (AuthoringGameState) super.setImgPath(imgPath);
	}

	@Override
	public AuthoringGameState setName(String name) {
		return (AuthoringGameState) super.setName(name);
	}

	@Override
	public ModifiableGameBoard getGrid() {
		return (ModifiableGameBoard) super.getGrid();
	}

	@Override
	public AuthoringGameState setGrid(GameBoard grid) {
		return (AuthoringGameState) super.setGrid(grid);
	}

	public AuthoringGameState addTeam(Team team) {
		getTemplateByCategory(TEAM).addAll(team);
		return this;
	}

	public void setTeams(Collection<Team> teams) {
		getTeams().stream().forEach(team -> removeTeamByName(team.getName()));
		teams.forEach(team -> addTeam(team));
	}

	@Override
	public AuthoringGameState addActiveObjectives(Resultant... objectives) {
		return (AuthoringGameState) super.addActiveObjectives(objectives);
	}

	@Override
	public AuthoringGameState addActiveObjectives(Collection<Resultant> objectives) {
		return (AuthoringGameState) super.addActiveObjectives(objectives);
	}

	@Override
	public AuthoringGameState addActiveTurnActions(Collection<Actionable> actions) {
		return (AuthoringGameState) super.addActiveTurnActions(actions);
	}

	@Override
	public AuthoringGameState addActiveTurnActions(Actionable... actions) {
		return (AuthoringGameState) super.addActiveTurnActions(actions);
	}

	@Override
	public AuthoringGameState addActiveTurnRequirements(Collection<Requirement> turnRequirements) {
		return (AuthoringGameState) super.addActiveTurnRequirements(turnRequirements);
	}

	@Override
	public AuthoringGameState addActiveTurnRequirements(Requirement... turnRequirements) {
		return (AuthoringGameState) super.addActiveTurnRequirements(turnRequirements);
	}

	public AuthoringGameState removeTeamByName(String name) {
		getTemplateByCategory(TEAM).removeAll(name);
		return this;
	}

	@Override
	public AuthoringGameState removeActiveObjectives(Resultant... objectives) {
		return (AuthoringGameState) super.removeActiveObjectives(objectives);
	}

	@Override
	public AuthoringGameState removeActiveObjectives(Collection<Resultant> objectives) {
		return (AuthoringGameState) super.removeActiveObjectives(objectives);
	}

	@Override
	public AuthoringGameState removeTurnActions(Actionable... actions) {
		return (AuthoringGameState) super.removeTurnActions(actions);
	}

	@Override
	public AuthoringGameState removeActiveTurnActions(Actionable... actions) {
		return (AuthoringGameState) super.removeActiveTurnActions(actions);
	}

	@Override
	public AuthoringGameState removeActiveTurnRequirements(Collection<Requirement> turnRequirements) {
		return (AuthoringGameState) super.removeActiveTurnRequirements(turnRequirements);
	}

	@Override
	public AuthoringGameState removeActiveTurnRequirements(Requirement... turnRequirements) {
		return (AuthoringGameState) super.removeActiveTurnRequirements(turnRequirements);
	}

	//Name can be Terrain, OffensiveModifier, DefensiveModifier, Cell, CellTriggeredEffect, UnitTriggeredEffect, ActiveAbility, Unit, UnitStat, GridPattern, GameBoard
	//Case and space character insensitive
	//Plural singular insensitive
	public ModifiableVoogaCollection<VoogaEntity, ? extends ModifiableVoogaCollection> getTemplateByCategory(String categoryName) {
		return getTemplates().get(categoryName.replaceAll(" ", "").toLowerCase().replaceAll("ies$", "y").replaceAll("s$", ""));
	}

	public VoogaEntity getTemplateByName(String name) {
		try {
			return getTemplates().values().stream().flatMap(ImmutableVoogaCollection::stream).filter(e -> e.getName().equals(name)).findAny().orElseThrow(() -> new RuntimeException("Template not found"));
		} catch (RuntimeException e) {
			System.out.println(name);
			System.out.println(getTemplates().values().stream().flatMap(ImmutableVoogaCollection::stream).collect(Collectors.toList()));
			throw e;
		}
	}

	public AuthoringGameState removeTemplateByName(String name) {
		getTemplates().get(getTemplates().entrySet().parallelStream().filter(e -> e.getValue().containsName(name)).map(Map.Entry::getKey).findAny().orElse("")).removeAll(name);
		return this;
	}

}
