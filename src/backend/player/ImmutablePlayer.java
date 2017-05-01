package backend.player;

import backend.game_engine.ResultQuadPredicate.Result;
import backend.util.VoogaEntity;

import java.util.List;
import java.util.Optional;

public interface ImmutablePlayer extends VoogaEntity {
	Optional<Team> getTeam();

	ImmutablePlayer setTeam(Team team);

	List<ChatMessage> getChatLog();

	void receiveMessage(ChatMessage message);
	
	Result getResult();
	
	void setResult(Result result);
}
