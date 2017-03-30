package backend.unit.properties;

import backend.Game;
import backend.GameObject;

/**
 * @author Created by th174 on 3/28/2017.
 */
public class UnitStat<T> extends GameObject {
    private final T initialValue;
    private T currentValue;

    public UnitStat(String name, T initialValue, String description, String imgPath, Game game) {
        this(name, initialValue, initialValue, description, imgPath, game);
    }

    public UnitStat(String name, T currentValue, T initialValue, String description, String imgPath, Game game) {
        super(name, description, imgPath, game);
        this.currentValue = currentValue;
        this.initialValue = initialValue;
    }

    public void set(T newValue) {
        currentValue = newValue;
    }

    public T getInitialValue() {
        return initialValue;
    }

    public T getCurrentValue() {
        return currentValue;
    }

    public void resetValue() {
        set(getInitialValue());
    }
}
