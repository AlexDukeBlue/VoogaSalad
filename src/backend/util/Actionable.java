package backend.util;

import java.util.function.BiConsumer;

import backend.player.ImmutablePlayer;

public class Actionable {
	
	private BiConsumer<ImmutablePlayer, GameplayState> biCon;
	private String nameOf;
	
	/**
	 * Pass a BiConsumer<ImmutablePlayer, GameplayState> with a name so that it can later be removed if necessary.
	 * 
	 * @param biconsumer
	 * @param name
	 */
	public Actionable(BiConsumer<ImmutablePlayer, GameplayState> bi, String name){
		biCon = bi;
		nameOf = name;
	}
	
	/**
	 * Returns the Actionable's BiConsumer.
	 * 
	 * @return BiConsumer<ImmutablePlayer, GameplayState>
	 */
	public BiConsumer<ImmutablePlayer, GameplayState> getBiConsumer() {
		return biCon;
	}
	
	/**
	 * Returns the name of the BiConsumer.
	 * 
	 * @return String
	 */
	public String getNameOf() {
		return nameOf;
	}
}
