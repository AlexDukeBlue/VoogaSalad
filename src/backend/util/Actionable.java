package backend.util;

import backend.player.Team;

import java.io.Serializable;
import java.util.function.BiConsumer;

public class Actionable extends ImmutableVoogaObject<Actionable> {
	private static final long serialVersionUID = 1L;

	private SerializableBiConsumer biCon;

	/**
	 * Pass a BiConsumer<ImmutablePlayer, GameplayState> with a name so that it can later be removed if necessary.
	 *
	 * @param biconsumer
	 * @param name
	 */
	public Actionable(SerializableBiConsumer bi, String name, String description) {
		this(bi, name, description, "");
	}

	public Actionable(SerializableBiConsumer bi, String name, String description, String imgPath) {
		super(name, description, imgPath);
		biCon = bi;
	}

	/**
	 * Returns the Actionable's BiConsumer.
	 *
	 * @return BiConsumer<ImmutablePlayer, GameplayState>
	 */
	public void accept(Team team, ReadonlyGameplayState gameState) {
		biCon.accept(team, gameState);
	}

	@Override
	public Actionable copy() {
		return new Actionable(biCon, getName(), getDescription(), getImgPath());
	}

	public interface SerializableBiConsumer extends BiConsumer<Team, ReadonlyGameplayState>, Serializable {
	}
}
