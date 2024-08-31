package me.restonic4.citadel.registries.built_in.types;

import me.restonic4.citadel.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class Event extends RegistryObject {
    private final boolean isCancelable;
    protected List<> listeners = new ArrayList<>();

    public Event() {
        this.isCancelable = false;
    }

    public Event(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }

    public void fire() {

    }

    public void listen( listener) {
        listeners.add(listener);
    }

    public boolean isCancelable() {
        return this.isCancelable;
    }
}
