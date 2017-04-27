package backend.unit.properties;

import backend.util.ModifiableVoogaCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Created by th174 on 4/9/2017.
 */
public class ModifierSet extends ModifiableVoogaCollection<InteractionModifier<Double>, ModifierSet> {
	@SafeVarargs
	public ModifierSet(String name, String description, String imgPath, InteractionModifier<Double>... modifiers) {
		this(name, description, imgPath, Arrays.asList(modifiers));
	}

	public ModifierSet(String name, String description, String imgPath, Collection<? extends InteractionModifier<Double>> modifiers) {
		super(name, description, imgPath,modifiers);
	}

	@Override
	public ModifierSet addAll(Collection<? extends InteractionModifier<Double>> elements) {
		return super.addAll(elements.stream().map(InteractionModifier::copy).collect(Collectors.toList()));
	}

	@Override
	public ModifierSet copy() {
		return new ModifierSet(getName(), getDescription(), getImgPath(),getAll().stream().map(InteractionModifier::copy).collect(Collectors.toList()));
	}

	@Override
	public List<InteractionModifier<Double>> getAll() {
		return new ArrayList<>(super.getAll());
	}
}
