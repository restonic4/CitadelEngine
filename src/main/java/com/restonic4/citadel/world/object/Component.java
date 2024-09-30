package com.restonic4.citadel.world.object;

import com.restonic4.citadel.util.UniqueIdentifierManager;

public abstract class Component {
    private final int id;
    public GameObject gameObject = null;

    public Component() {
        this.id = UniqueIdentifierManager.generateUniqueID();
    }

    public void start() {

    }

    public abstract void update();

    public int getId() {
        return this.id;
    }
}
