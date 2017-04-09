package backend.unit.properties;

import backend.util.VoogaCollection;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 4/9/2017.
 */
public class OffensiveModifierSet extends VoogaCollection<InteractionModifier<Double>, OffensiveModifierSet> {
	@SafeVarargs
	public OffensiveModifierSet(InteractionModifier<Double>... modifiers) {
		this(Arrays.asList(modifiers));
	}

	public OffensiveModifierSet(Collection<? extends InteractionModifier<Double>> modifiers) {
		super("Offensive Modifiers", "Each unit has a set of offensive modifiers that can change the amount of damage the unit deals under different conditions.", "", modifiers);
	}

	@Override
	public OffensiveModifierSet copy() {
		return new OffensiveModifierSet(getAll().stream().map(InteractionModifier::copy).collect(Collectors.toList()));
	}

	@Override
	public List<InteractionModifier<Double>> getAll() {
		return Collections.unmodifiableList(new ArrayList<>(super.getAll()));
	}
}
