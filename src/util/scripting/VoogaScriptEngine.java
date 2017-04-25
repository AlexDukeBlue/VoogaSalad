package util.scripting;

import backend.game_engine.ResultQuadPredicate;
import backend.player.ImmutablePlayer;
import backend.unit.Unit;
import backend.unit.properties.ActiveAbility;
import backend.unit.properties.InteractionModifier;
import backend.util.*;
import util.io.Serializer;
import util.io.Unserializer;

import java.io.Serializable;
import java.util.*;

/**
 * @author Created by th174 on 4/7/2017.
 */
public interface VoogaScriptEngine extends Serializer, Unserializer, InteractionModifier.Modifier, TriggeredEffect.Effect, ActiveAbility.AbilityEffect, ResultQuadPredicate, Requirement.SerializableBiPredicate, Actionable.SerializableBiConsumer, Serializable {
	ResourceBundle RESOURCES = ResourceBundle.getBundle("resources/Scripting", Locale.US);

	VoogaScriptEngine setScript(String script) throws VoogaScriptException;

	default String getDefaultText() {
		return RESOURCES.getString(getLanguage() + "DefaultText");
	}

	default String getLanguage() {
		return RESOURCES.getString(getClass().getSimpleName().replaceAll("Vooga|Engine", ""));
	}

	Object eval(Map<String, Object> bindings) throws VoogaScriptException;

	@Override
	default ResultQuadPredicate.Result determine(ImmutablePlayer player, GameplayState gameState) {
		try {
			return ResultQuadPredicate.Result.valueOf((String) eval(createBindings("player", player, "gameState", gameState)));
		} catch (ClassCastException e) {
			throw new VoogaScriptException(e);
		}
	}

	@Override
	default Object doUnserialize(Serializable serializableObject) throws VoogaScriptException {
		try {
			return eval(createBindings("serializable", serializableObject));
		} catch (ClassCastException e) {
			throw new VoogaScriptException(e);
		}
	}

	@Override
	default Serializable doSerialize(Object object) throws VoogaScriptException {
		try {
			return (Serializable) eval(createBindings("object", object));
		} catch (ClassCastException e) {
			throw new VoogaScriptException(e);
		}
	}

	@Override
	default void affect(Unit unit, Event event, GameplayState gameState) {
		eval(createBindings("unit", unit, "event", event, "gameState", gameState));
	}

	@Override
	default void useAbility(Unit user, VoogaEntity target, GameplayState gameState) {
		eval(createBindings("user", user, "target", target, "gameState", gameState));
	}

	@Override
	default Object modify(Object originalValue, Unit agent, Unit target, GameplayState gameState) {
		return eval(createBindings("originalValue", originalValue, "agent", agent, "target", target, "gameState", gameState));
	}

	@Override
	default boolean test(ImmutablePlayer player, ReadonlyGameplayState gameState) {
		Object nonBooleanValue = eval(createBindings("player", player, "gameState", gameState));
		if (nonBooleanValue instanceof String) {
			return !nonBooleanValue.equals("");
		} else if (nonBooleanValue instanceof Boolean) {
			return (Boolean) nonBooleanValue;
		} else {
			return Objects.nonNull(nonBooleanValue);
		}
	}

	@Override
	default void accept(ImmutablePlayer player, ReadonlyGameplayState gameState) {
		eval(createBindings("player", player, "gameState", gameState));
	}

	static HashMap<String, Object> createBindings(Object... params) {
		HashMap<String, Object> bindings = new LinkedHashMap<>();
		try {
			for (int i = 0; i < params.length; i += 2) {
				bindings.put((String) params[i], params[i + 1]);
			}
		} catch (ClassCastException | IndexOutOfBoundsException e) {
			throw new Error("Invalid arguments, must be name, binding, name, binding, name binding etc.");
		}
		return bindings;
	}
}
