package backend.util;

import java.util.function.BiConsumer;

import backend.player.ImmutablePlayer;

public class Actionable {
	
	private BiConsumer<ImmutablePlayer, GameplayState> biCon;
	private String nameOf;
	
	public Actionable(BiConsumer<ImmutablePlayer, GameplayState> bi, String name){
		biCon = bi;
		nameOf = name;
	}
	
	public BiConsumer<ImmutablePlayer, GameplayState> getBiConsumer() {
		return biCon;
	}
	
	public String getNameOf() {
		return nameOf;
	}
}
