package backend.util;

import backend.player.ImmutablePlayer;

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
	 * Returns the Actionable's BiConsumer. //TODO make correct
	 *
	 * @return BiConsumer<ImmutablePlayer, GameplayState>
	 */
	public void accept(ImmutablePlayer player, ReadonlyGameplayState gameState) {
		biCon.accept(player, gameState);
	}

	@Override
	public Actionable copy() {
		return new Actionable(biCon, getName(), getDescription(), getImgPath());
	}
	
	public interface SerializableBiConsumer extends BiConsumer<ImmutablePlayer, ReadonlyGameplayState>, Serializable{}
}
