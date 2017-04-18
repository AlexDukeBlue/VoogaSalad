package backend.player;

import backend.util.ModifiableVoogaCollection;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class Team extends ModifiableVoogaCollection<ImmutablePlayer, Team> {
	public Team(String name, String description, String imgPath, Player... players) {
		super(name, description, imgPath, players);
	}

	public Team(String name, String description, String imgPath, Collection<? extends Player> gameObjects) {
		super(name, description, imgPath, gameObjects);
	}

	@Override
	public Team addAll(Collection<? extends ImmutablePlayer> players) {
		players.forEach(e -> e.setTeam(this));
		return super.addAll(players);
	}

	@Override
	public Team removeAll(Collection<? extends ImmutablePlayer> players) {
		players.forEach(e -> e.setTeam(null));
		return super.removeAll(players);
	}

	@Override
	public Team copy() {
		return new Team(getName(), getDescription(), getImgPath(), getAll().stream().map(ImmutablePlayer::copy).map(Player.class::cast).collect(Collectors.toList()));
	}
}
