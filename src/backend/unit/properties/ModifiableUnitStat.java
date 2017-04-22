package backend.unit.properties;

import backend.util.ModifiableVoogaObject;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Created by th174 on 4/6/2017.
 */
public class ModifiableUnitStat<T extends Comparable<T>> extends ModifiableVoogaObject<ModifiableUnitStat<T>> implements UnitStat<T>, Serializable {
	//TODO ResourceBundlify
	//For units
	public transient static final ModifiableUnitStat<Double> HITPOINTS = new ModifiableUnitStat<Double>("Hitpoints")
			.setMinValue(0.0)
			.setMaxValue(50.0)
			.setDescription("Units lose HP when taking damage. When a unit's hitpoints reach 0, the unit dies.")
			.setImgPath("<3.png");
	public transient static final ModifiableUnitStat<Integer> MOVEPOINTS = new ModifiableUnitStat<Integer>("Movepoints")
			.setMinValue(0)
			.setMaxValue(5)
			.setDescription("Movepoints are consumed by moving on the map. Difficult terrain costs more movepoints, while more mobile units have more to spare.")
			.setImgPath("Boot.png");
	public transient static final ModifiableUnitStat<Double> ENERGY = new ModifiableUnitStat<Double>("Energy")
			.setMinValue(0.0)
			.setMaxValue(100.0)
			.setDescription("Energy points are required for and consumed by some powerful abilities.")
			.setImgPath("NRG.png");
	//For players
	public transient static final ModifiableUnitStat<Double> GOLD = new ModifiableUnitStat<Double>("Gold")
			.setMinValue(0.0)
			.setMaxValue(Double.MAX_VALUE)
			.setDescription("Players have a limited amount of gold to spend on their units.")
			.setImgPath("SHINY~~~.png");

	private T maxValue;
	private T minValue;
	private T currentValue;

	public ModifiableUnitStat(String name) {
		this(name, "", "");
	}

	public ModifiableUnitStat(String name, String description, String imgPath) {
		this(name, null, null, description, imgPath);
	}

	public ModifiableUnitStat(String name, T minValue, T maxValue, String description, String imgPath) {
		super(name, description, imgPath);
		setMaxValue(maxValue);
		setMinValue(minValue);
		setCurrentValue(maxValue);
	}

	@Deprecated
	public static Collection<ModifiableUnitStat> getPredefinedUnitStats() {
		return getPredefined(ModifiableUnitStat.class);
	}

	@Override
	public ModifiableUnitStat<T> copy() {
		return new ModifiableUnitStat<>(getName(), minValue, maxValue, getDescription(), getImgPath());
	}

	@Override
	public T getMaxValue() {
		return maxValue;
	}

	public ModifiableUnitStat<T> setMaxValue(T maxValue) {
		this.maxValue = maxValue;
		return this;
	}

	@Override
	public T getCurrentValue() {
		return currentValue;
	}

	@Override
	public ModifiableUnitStat<T> setCurrentValue(T currentValue) {
		this.currentValue = currentValue;
		return this;
	}

	@Override
	public T getMinValue() {
		return minValue;
	}

	public ModifiableUnitStat<T> setMinValue(T minValue) {
		this.minValue = minValue;
		return this;
	}
}
