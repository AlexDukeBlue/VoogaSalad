package backend.player;

import backend.cell.Cell;
import backend.grid.ModifiableGameBoard;
import backend.unit.Unit;
import backend.util.VoogaEntity;

import java.util.Collection;
import java.util.List;

public interface ImmutablePlayer extends VoogaEntity {
	Team getTeam();

	ImmutablePlayer setTeam(Team team);

	Collection<Unit> getOwnedUnits(ModifiableGameBoard grid);

	Collection<Cell> getVisibleCells();

	Collection<Cell> getExploredCells();

	List<ChatMessage> getChatLog();

	void receiveMessage(ChatMessage message);
}
