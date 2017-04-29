package backend.util;

import backend.player.ImmutablePlayer;

import java.io.Serializable;
import java.util.function.BiPredicate;

public class Requirement extends ImmutableVoogaObject<Requirement>{
	private static final long serialVersionUID = 1L;

	private SerializableBiPredicate biPred;

	/**
	 * Pass a BiPredicate<ImmutablePlayer, GameplayState> with a name so that it can later be removed if necessary.
	 *
	 * @param bipredicate
	 * @param name
	 */
	public Requirement(SerializableBiPredicate bi, String name, String description) {
		this(bi, name, description, "");
		
	}
	
	public Requirement(SerializableBiPredicate bi, String name, String description, String imgPath) {
		super(name, description, imgPath);
		biPred = bi;
	}

	/**
	 * Returns the Requirement's BiConsumer.
	 *
	 * @return BiPredicate
	 */
	public boolean test(ImmutablePlayer player, ReadonlyGameplayState gameState) {
		return biPred.test(player, gameState);
	}

	@Override
	public Requirement copy() {
		return new Requirement(biPred, getName(), getDescription(), getImgPath());
	}
	
	public interface SerializableBiPredicate extends BiPredicate<ImmutablePlayer, ReadonlyGameplayState>, Serializable{}
}
