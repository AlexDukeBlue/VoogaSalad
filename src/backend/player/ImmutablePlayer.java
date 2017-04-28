package backend.player;

import backend.cell.Cell;
import backend.grid.ModifiableGameBoard;
import backend.unit.Unit;
import backend.util.VoogaEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ImmutablePlayer extends VoogaEntity {
	Optional<Team> getTeam();

	ImmutablePlayer setTeam(Team team);

	List<ChatMessage> getChatLog();

	Collection<Unit> getOwnedUnits(ModifiableGameBoard grid);

	Collection<Cell> getVisibleCells();

	Collection<Cell> getExploredCells();

	void receiveMessage(ChatMessage message);
}
