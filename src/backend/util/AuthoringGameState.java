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

	private Collection<Actionable> availableTurnActions;
	private Collection<Requirement> availableTurnRequirements;
	private Collection<Resultant> availableObjectives;

	public AuthoringGameState(String name) {
		super(name, null, "", "");
		this.setAuthoringMode(true);
		availableTurnActions = new HashSet<Actionable>();
		availableTurnRequirements = new ArrayList<>();
		availableObjectives = new ArrayList<>();
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
	public AuthoringGameState addObjectives(Resultant... objectives) {
		return (AuthoringGameState) super.addObjectives(objectives);
	}

	@Override
	public AuthoringGameState addObjectives(Collection<Resultant> objectives) {
		return (AuthoringGameState) super.addObjectives(objectives);
	}

	/**
	 * Adds the passed Resultants to the AuthoringGameState's Collection of available Resultants.
	 * <p>
	 * Available Resultants differ from active objective (which are added with addObjectives()) in that
	 * they may or may not be active. This is for the sake of UX, so that the user can activate and
	 * deactivate Resultants without deleting them and having to remake them.
	 *
	 * @param objectives Resultants to be added to the available Resultant Collection
	 * @return This AuthoringGameState
	 * @author Stone Mathers
	 */
	public AuthoringGameState addAvailableObjectives(Resultant... objectives) {
		availableObjectives.addAll(Arrays.asList(objectives));
		return this;
	}

	@Override
	public AuthoringGameState addTurnActions(Collection<Actionable> actions) {
		return (AuthoringGameState) super.addTurnActions(actions);
	}

	@Override
	public AuthoringGameState addTurnActions(Actionable... actions) {
		return (AuthoringGameState) super.addTurnActions(actions);
	}

	/**
	 * Maps the passed Event to the passed Actionables in the AuthoringGameState's Map of available Actionables.
	 * This is done without replacing the Actionables that the Event already maps to.
	 * <p>
	 * Available Actionables differ from active Actionables (which are added with addTurnActions()) in that
	 * they may or may not be active. This is for the sake of UX, so that the user can activate and
	 * deactivate Actionables without deleting them and having to remake them.
	 *
	 * @param event   Event that triggers the passed Actionables
	 * @param actions Actionables triggered by the passed Event
	 * @return This AuthoringGameState
	 * @author Stone Mathers
	 */
	public AuthoringGameState addAvailableTurnActions(Actionable... actions) {
		availableTurnActions.addAll(Arrays.asList(actions));
		return this;
	}

	@Override
	public AuthoringGameState addTurnRequirements(Collection<Requirement> turnRequirements) {
		return (AuthoringGameState) super.addTurnRequirements(turnRequirements);
	}

	@Override
	public AuthoringGameState addTurnRequirements(Requirement... turnRequirements) {
		return (AuthoringGameState) super.addTurnRequirements(turnRequirements);
	}

	/**
	 * Adds the passed Requirements to the AuthoringGameState's Collection of available Requirements.
	 * <p>
	 * Available Requirements differ from active Requirements (which are added with addTurnRequirements()) in that
	 * they may or may not be active. This is for the sake of UX, so that the user can activate and
	 * deactivate Requirements without deleting them and having to remake them.
	 *
	 * @param turnRequirements Requirements to be added to the available Requirement Collection
	 * @return This AuthoringGameState
	 * @author Stone Mathers
	 */
	public AuthoringGameState addAvailableTurnRequirements(Requirement... turnRequirements) {
		availableTurnRequirements.addAll(Arrays.asList(turnRequirements));
		return this;
	}

	public AuthoringGameState removeTeamByName(String name) {
		getTemplateByCategory(TEAM).removeAll(name);
		return this;
	}

	@Override
	public AuthoringGameState removeObjectives(Resultant... objectives) {
		return (AuthoringGameState) super.removeObjectives(objectives);
	}

	@Override
	public AuthoringGameState removeObjectives(Collection<Resultant> objectives) {
		return (AuthoringGameState) super.removeObjectives(objectives);
	}

	/**
	 * Removes the passed Resultants from the AuthoringGameState's Collection of available Resultants.
	 * <p>
	 * Available Resultants differ from active turn requirements (which are removed with removeObjectives()) in that
	 * they may or may not be active. This is for the sake of UX, so that the user can activate and
	 * deactivate Resultants without deleting them and having to remake them.
	 *
	 * @param objectives Resultants to be removed from the available Resultant Collection
	 * @return This AuthoringGameState
	 * @author Stone Mathers
	 */
	public AuthoringGameState removeAvailableObjectives(Resultant... objectives) {
		availableObjectives.removeAll(Arrays.asList(objectives));
		return removeObjectives(objectives);
	}

	@Override
	public AuthoringGameState removeTurnActions(Actionable... actions) {
		return (AuthoringGameState) super.removeTurnActions(actions);
	}

	@Override
	public AuthoringGameState removeTurnActions(Event event, Actionable... actions) {
		return (AuthoringGameState) super.removeTurnActions(event, actions);
	}

	/**
	 * Removes the passed Actionables the map to the given Event from the AuthoringGameState's Map of available turn actions.
	 * <p>
	 * Available Actionables differ from active Actionables (which are removed to with removeTurnActions()) in that
	 * they may or may not be active. This is for the sake of UX, so that the user can activate and
	 * deactivate Actionables without deleting them and having to remake them.
	 *
	 * @param event   Event that triggers the passed Actionables
	 * @param actions Actionables triggered by the passed Event
	 * @return This AuthoringGameState
	 * @author Stone Mathers
	 */
	public AuthoringGameState removeAvailableTurnActions(Actionable... actions) {
		availableTurnActions.removeIf(Arrays.asList(actions)::contains);
		return removeTurnActions(actions);
	}

	@Override
	public AuthoringGameState removeTurnRequirements(Collection<Requirement> turnRequirements) {
		return (AuthoringGameState) super.removeTurnRequirements(turnRequirements);
	}

	@Override
	public AuthoringGameState removeTurnRequirements(Requirement... turnRequirements) {
		return (AuthoringGameState) super.removeTurnRequirements(turnRequirements);
	}

	/**
	 * Removes the passed Requirements from the AuthoringGameState's Collection of available turn requirements.
	 * <p>
	 * Available Requirements differ from active Requirements (which are removed with removeTurnRequirements()) in that
	 * they may or may not be active. This is for the sake of UX, so that the user can activate and
	 * deactivate Requirements without deleting them and having to remake them.
	 *
	 * @param turnRequirements Requirements to be removed from the available Requirement Collection
	 * @return This AuthoringGameState
	 * @author Stone Mathers
	 */
	public AuthoringGameState removeAvailableTurnRequirements(Requirement... turnRequirements) {
		availableTurnRequirements.removeAll(Arrays.asList(turnRequirements));
		return removeTurnRequirements(turnRequirements);
	}

	public Collection<Actionable> getAvailableTurnActions() {
		return availableTurnActions;
	}

	public Collection<Requirement> getAvailableTurnRequirements() {
		return availableTurnRequirements;
	}

	/**
	 * @return Collection<Resultant> of available Resultants
	 */
	public Collection<Resultant> getAvailableObjectives() {
		return availableObjectives;
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
