package backend.player;

import backend.util.ModifiableVoogaCollection;
import backend.util.VoogaEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class Team extends ModifiableVoogaCollection<ImmutablePlayer, Team> implements VoogaEntity {
	public static final String RED = "#ff0000";
	public static final String GREEN = "#00ff00";
	public static final String BLUE = "#0000ff";
	public static final String YELLOW = "#ffff00";
	public static final String CYAN = "#00ffff";
	public static final String MAGENTA = "#ff00ff";
	public static final String WHITE = "#ffffff";
	public static final String BLACK = "#000000";
	private String colorString;

	public Team(String name, String description, String imgPath, Player... players) {
		this(name, description, imgPath, Arrays.asList(players));
	}

	public Team(String name, String description, String imgPath, Collection<? extends Player> players) {
		this(name, description, "", imgPath, players);
	}

	public Team(String name, String descripton, String colorString, String imgPath, Collection<? extends Player> players) {
		super(name, descripton, imgPath, players);
		this.colorString = colorString;
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

	public String getColorString() {
		return colorString;
	}

	public void setColorString(String color) {
		colorString = color;
	}
}
