package controller;

import backend.util.GameplayState;
import util.net.Modifier;

/**
 * @author Created by th174 on 4/12/2017.
 */
@FunctionalInterface
public interface GameplayModifierBuilder {
	Modifier<GameplayState> buildFrom(Object... args);
}
