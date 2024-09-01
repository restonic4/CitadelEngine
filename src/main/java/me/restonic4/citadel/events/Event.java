package me.restonic4.citadel.events;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Event<T> {
    private final List<T> listeners = new ArrayList<>();
    private final Function<T[], T> invokerFactory;
    private final Class<T> type;
    private T invoker;

    public Event(Function<T[], T> invokerFactory, Class<T> type) {
        this.invokerFactory = invokerFactory;
        this.type = type;
        this.invoker = invokerFactory.apply(createArray(0));
    }

    public void register(T listener) {
        listeners.add(listener);
        updateInvoker();
    }

    private void updateInvoker() {
        invoker = invokerFactory.apply(listeners.toArray(createArray(listeners.size())));
    }

    public T invoker() {
        return invoker;
    }

    @SuppressWarnings("unchecked")
    private T[] createArray(int length) {
        return (T[]) Array.newInstance(type, length);
    }
}