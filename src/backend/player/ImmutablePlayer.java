package backend.player;

import backend.util.VoogaEntity;

import java.util.List;

public interface ImmutablePlayer extends VoogaEntity {
	Team getTeam();

	ImmutablePlayer setTeam(Team team);

	List<ChatMessage> getChatLog();

	void receiveMessage(ChatMessage message);
}
