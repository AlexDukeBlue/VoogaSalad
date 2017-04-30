package backend.player;

import backend.cell.Cell;
import backend.grid.ModifiableGameBoard;
import backend.unit.Unit;
import backend.util.ModifiableVoogaCollection;
import backend.util.VoogaEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class Team extends ModifiableVoogaCollection<ImmutablePlayer, Team> implements VoogaEntity {
	private static final long serialVersionUID = 1L;

	public transient static final String RED = "#ff0000";
	public transient static final String GREEN = "#00ff00";
	public transient static final String BLUE = "#0000ff";
	public transient static final String YELLOW = "#aaaa00";
	public transient static final String CYAN = "#00ffff";
	public transient static final String MAGENTA = "#ff00ff";
	public transient static final String BLACK = "#000000";
	public transient static final String WHITE = "#ffffff";
	public transient static final List<String> COLORS = Arrays.asList(RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA, BLACK);
	private static final String NO_TEAM_ASSIGNED = "NO_TEAM_ASSIGNED";
	private final String colorString;

	public Team(String name, String description, String colorString, String imgPath, Player... players) {
		this(name, description, colorString, imgPath, Arrays.asList(players));
	}

	public Team(String name, String descripton, String colorString, String imgPath, Collection<? extends Player> players) {
		super(name, descripton, imgPath, players);
		this.colorString = colorString;
	}

	@Override
	public Team addAll(Collection<? extends ImmutablePlayer> players) {
		super.addAll(players);
		players.stream().filter(e -> e.getTeam().isPresent() && !e.getTeam().get().equals(this)).forEach(e -> e.setTeam(this));
		return this;
	}

	@Override
	public Team removeAll(Collection<? extends ImmutablePlayer> players) {
		super.removeAll(players);
		players.stream().filter(e -> e.getTeam().isPresent() && e.getTeam().get().equals(this)).forEach(e -> e.setTeam(null));
		return this;
	}

	public Collection<Unit> getOwnedUnits(ModifiableGameBoard grid) {
		return getAll().parallelStream().map(e -> e.getOwnedUnits(grid)).flatMap(Collection::stream).collect(Collectors.toSet());
	}

	public Collection<Cell> getVisibleCells() {
		return getAll().parallelStream().map(ImmutablePlayer::getVisibleCells).flatMap(Collection::stream).collect(Collectors.toSet());
	}

	public Collection<Cell> getExploredCells() {
		return getAll().parallelStream().map(ImmutablePlayer::getExploredCells).flatMap(Collection::stream).collect(Collectors.toSet());
	}

	@Override
	public Team copy() {
		return new Team(getName(), getDescription(), getColorString(), getImgPath(), getAll().stream().map(ImmutablePlayer::copy).map(Player.class::cast).collect(Collectors.toList()));
	}

	public String getColorString() {
		return colorString;
	}

	@Override
	public boolean equals(Object obj) {
		return Objects.isNull(obj) || obj instanceof Team && ((Team) obj).getName().equals(getName());
	}
}
