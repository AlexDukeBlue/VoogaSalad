package backend.player;

import backend.cell.Cell;
import backend.grid.ModifiableGameBoard;
import backend.unit.Unit;
import backend.util.ModifiableVoogaObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Alex
 *
 * @author Created by th174 on 3/28/2017.
 */
public class Player extends ModifiableVoogaObject<Player> implements ImmutablePlayer {
	private Team team;
	private ObservableList<ChatMessage> chatLog;

	public Player(String name, String description, String imgPath) {
		this(name, new Team(name + "'s Team", "", imgPath), description, imgPath);
		getTeam().addAll(this);
	}

	public Player(String name, Team team, String description, String imgPath) {
		super(name, description, imgPath);
		chatLog = FXCollections.observableArrayList();
		this.team = team;
	}

	@Override
	public Player copy() {
		return new Player(getName(), getTeam(), getDescription(), getImgPath());
	}

	@Override
	public Team getTeam() {
		return team;
	}

	@Override
	public Collection<Unit> getOwnedUnits(ModifiableGameBoard grid) {
		return grid.getUnits().parallelStream().filter(e -> e.getOwner().equals(this)).collect(Collectors.toSet());
	}

	@Override
	public Collection<Cell> getVisibleCells() {
		return null;
	}

	@Override
	public Collection<Cell> getExploredCells() {
		return null;
	}

	public Player setTeam(Team team) {
		this.team = team;
		return this;
	}

	@Override
	public void receiveMessage(ChatMessage message) {
		chatLog.add(message);
	}

	@Override
	public ObservableList<ChatMessage> getChatLog() {
		return chatLog;
	}
}
