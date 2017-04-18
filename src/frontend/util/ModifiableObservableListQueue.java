package frontend.util;

import javafx.collections.ModifiableObservableListBase;

import java.util.*;

/**
 * @author Created by th174 on 4/4/2017.
 */
class ModifiableObservableListQueue<T> extends ModifiableObservableListBase<T> implements Queue<T> {
    private List<T> delegate;

    public ModifiableObservableListQueue() {
        this(Collections.emptyList());
    }

    public ModifiableObservableListQueue(Collection<? extends T> delegate) {
        super();
        this.delegate = new ArrayList<>(delegate);
    }

    @Override
    public T get(int index) {
        return delegate.get(index);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    protected void doAdd(int index, T element) {
        delegate.add(index, element);
    }

    @Override
    protected T doSet(int index, T element) {
        return delegate.set(index, element);
    }

    @Override
    protected T doRemove(int index) {
        return delegate.remove(index);
    }

    @Override
    public boolean offer(T t) {
        try {
            doAdd(0, t);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public T remove() {
        return doRemove(0);
    }

    @Override
    public T poll() {
        return delegate.isEmpty() ? null : doRemove(0);
    }

    @Override
    public T element() {
        return delegate.get(0);
    }

    @Override
    public T peek() {
        return delegate.isEmpty() ? null : delegate.get(0);
    }
}
