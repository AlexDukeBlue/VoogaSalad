package backend.util;

import backend.player.ImmutablePlayer;

import java.util.function.BiPredicate;

public class Requirement {

	private BiPredicate<ImmutablePlayer, GameplayState> biPred;
	private String nameOf;

	/**
	 * Pass a BiPredicate<ImmutablePlayer, GameplayState> with a name so that it can later be removed if necessary.
	 *
	 * @param bipredicate
	 * @param name
	 */
	public Requirement(BiPredicate<ImmutablePlayer, GameplayState> bi, String name) {
		biPred = bi;
		nameOf = name;
	}

	/**
	 * Returns the Requirement's BiConsumer.
	 *
	 * @return BiPredicate
	 */
	public BiPredicate<ImmutablePlayer, GameplayState> getBiPredicate() {
		return biPred;
	}

	/**
	 * Returns the name of the BiPredicate.
	 *
	 * @return String
	 */
	public String getNameOf() {
		return nameOf;
	}
}
