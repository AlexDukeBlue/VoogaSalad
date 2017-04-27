package backend.player;

import backend.cell.Cell;
import backend.grid.ModifiableGameBoard;
import backend.unit.Unit;
import backend.util.ModifiableVoogaCollection;
import backend.util.VoogaEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class Team extends ModifiableVoogaCollection<ImmutablePlayer, Team> implements VoogaEntity {
	public transient static final String RED = "#ff0000";
	public transient static final String GREEN = "#00ff00";
	public transient static final String BLUE = "#0000ff";
	public transient static final String YELLOW = "#aaaa00";
	public transient static final String CYAN = "#00ffff";
	public transient static final String MAGENTA = "#ff00ff";
	public transient static final String BLACK = "#000000";
	public transient static final List<String> COLORS = Arrays.asList(RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA, BLACK);
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
		players.stream().filter(e -> !e.getTeam().equals(this)).forEach(e -> e.setTeam(this));
		return this;
	}

	@Override
	public Team removeAll(Collection<? extends ImmutablePlayer> players) {
		super.removeAll(players);
		players.stream().filter(e -> e.getTeam().equals(this)).forEach(e -> e.setTeam(null));
		return this;
	}

	public Collection<Unit> getOwnedUnits(ModifiableGameBoard grid) {
		return grid.getUnits().parallelStream().filter(e -> e.getTeam().equals(this)).collect(Collectors.toSet());
	}

	public Collection<Cell> getVisibleCells() {
		throw new RuntimeException("Not yet implemented");
	}

	public Collection<Cell> getExploredCells() {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Team copy() {
		return new Team(getName(), getDescription(), getColorString(), getImgPath(), getAll().stream().map(ImmutablePlayer::copy).map(Player.class::cast).collect(Collectors.toList()));
	}

	public String getColorString() {
		return colorString;
	}
}
