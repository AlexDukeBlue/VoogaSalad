package backend.player;

import backend.util.ModifiableVoogaObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Created by th174 on 3/28/2017.
 */
public class Player extends ModifiableVoogaObject<Player> implements ImmutablePlayer {
	private Team team;
	private final List<ChatMessage> chatLog;

	public Player(String name, Team team, String description, String imgPath) {
		super(name, description, imgPath);
		chatLog = new ArrayList<>();
		setTeam(team);
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
	public void receiveMessage(ChatMessage message) {
		chatLog.add(message);
	}

	@Override
	public List<ChatMessage> getChatLog() {
		return chatLog;
	}
}
