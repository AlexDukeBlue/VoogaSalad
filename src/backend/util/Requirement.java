package backend.util;

import java.util.function.BiPredicate;

import backend.player.ImmutablePlayer;

public class Requirement {

	private BiPredicate<ImmutablePlayer, GameplayState> biPred;
	private String nameOf;
	
	public Requirement(BiPredicate<ImmutablePlayer, GameplayState> bi, String name){
		biPred = bi;;
		nameOf = name;
	}
	
	public BiPredicate<ImmutablePlayer, GameplayState> getBiPredicate() {
		return biPred;
	}
	
	public String getNameOf() {
		return nameOf;
	}
}
