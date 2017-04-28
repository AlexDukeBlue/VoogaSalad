package backend.player;

import backend.cell.Cell;
import backend.grid.ModifiableGameBoard;
import backend.unit.Unit;
import backend.util.ModifiableVoogaObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 3/28/2017.
 */
public class Player extends ModifiableVoogaObject<Player> implements ImmutablePlayer {
	private static final long serialVersionUID = 1L;

	private Team team;
	private final List<ChatMessage> chatLog;

	public Player(String name, String description, String imgPath) {
		super(name, description, imgPath);
		chatLog = new ArrayList<>();
		setTeam(team);
	}

	@Override
	public Player copy() {
		throw new RuntimeException("Can't copy players because that causes all sorts of problems.");
	}

	@Override
	public Optional<Team> getTeam() {
		return Optional.ofNullable(team);
	}

	@Override
	public Player setTeam(Team team) {
		if (Objects.nonNull(this.team) && this.team.containsName(getName())) {
			this.team.removeAll(this);
		}
		this.team = team;
		if (Objects.nonNull(team) && !team.containsName(getName())) {
			team.addAll(this);
		}
		return this;
	}

	@Override
	public Collection<Unit> getOwnedUnits(ModifiableGameBoard grid) {
		return grid.getUnits().parallelStream().filter(e -> e.getOwner().equals(this)).collect(Collectors.toSet());
	}

	@Override
	public Collection<Cell> getVisibleCells() {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Collection<Cell> getExploredCells() {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void receiveMessage(ChatMessage message) {
		chatLog.add(message);
	}

	@Override
	public List<ChatMessage> getChatLog() {
		return chatLog;
	}
}
